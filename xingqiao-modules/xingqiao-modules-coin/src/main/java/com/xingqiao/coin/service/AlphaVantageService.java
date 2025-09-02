package com.xingqiao.coin.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class AlphaVantageService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String apiKey;
    private final String baseUrl;

    public AlphaVantageService(
            @Value("${alphavantage.api.key:''}") String apiKey,
            @Value("${alphavantage.api.base-url:https://www.alphavantage.co/query}") String baseUrl) {
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * 获取指定股票的K线数据
     * @param symbol 股票代码（如 AAPL）
     * @param interval 时间间隔（如 5min, 15min, 60min, daily, weekly, monthly）
     */
    public List<Map<String, Object>> getStockData(String symbol, String interval) {
        try {
            String function;
            String key;

            // 根据 interval 判断调用哪个 API function
            switch (interval) {
                case "daily":
                    function = "TIME_SERIES_DAILY";
                    key = "Time Series (Daily)";
                    break;
                case "weekly":
                    function = "TIME_SERIES_WEEKLY";
                    key = "Weekly Time Series";
                    break;
                case "monthly":
                    function = "TIME_SERIES_MONTHLY";
                    key = "Monthly Time Series";
                    break;
                default:
                    // intraday: 5min, 15min, 60min
                    function = "TIME_SERIES_INTRADAY";
                    key = "Time Series (" + interval + ")";
                    break;
            }

            // 构造请求 URL
            String url = String.format(
                    "%s?function=%s&symbol=%s&apikey=%s",
                    baseUrl, function, symbol, apiKey
            );

            // intraday 需要加 interval 参数
            if (function.equals("TIME_SERIES_INTRADAY")) {
                url += "&interval=" + interval;
            }

            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("请求 Alpha Vantage API 失败: " + response.getStatusCode());
            }

            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode timeSeries = root.get(key);

            if (timeSeries == null) {
                throw new RuntimeException("未找到股票数据: " + root.toString());
            }

            List<Map<String, Object>> result = new ArrayList<>();
            Iterator<String> fieldNames = timeSeries.fieldNames();

            while (fieldNames.hasNext()) {
                String time = fieldNames.next();
                JsonNode data = timeSeries.get(time);

                Map<String, Object> point = new HashMap<>();
                point.put("time", time);
                point.put("open", data.get("1. open").asDouble());
                point.put("high", data.get("2. high").asDouble());
                point.put("low", data.get("3. low").asDouble());
                point.put("close", data.get("4. close").asDouble());
                point.put("volume", data.get("5. volume").asLong());

                result.add(point);
            }

            return result;
        } catch (Exception e) {
            throw new RuntimeException("获取股票数据失败: " + e.getMessage(), e);
        }
    }
}
