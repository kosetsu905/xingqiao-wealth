package com.xingqiao.order.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.*;
import java.time.format.DateTimeFormatter;
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
     * 获取公司名称
     */
    private String getCompanyName(String symbol) {
        try {
            String url = String.format(
                    "%s?function=SYMBOL_SEARCH&keywords=%s&apikey=%s",
                    baseUrl, symbol, apiKey
            );
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            if (!response.getStatusCode().is2xxSuccessful()) return symbol;

            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode bestMatches = root.get("bestMatches");
            if (bestMatches != null && bestMatches.isArray() && bestMatches.size() > 0) {
                JsonNode match = bestMatches.get(0);
                String name = match.get("2. name").asText();
                return (name != null && !name.isEmpty()) ? name : symbol;
            }
        } catch (Exception e) {
            System.err.println("获取公司名称失败: " + e.getMessage());
        }
        return symbol;
    }

    /**
     * 获取股票数据，包含 pre-market 数据
     */
    public Map<String, Object> getStockDataWithCompany(String symbol, String interval) {
        try {
            String function;
            String key;
            boolean isIntraday = false;

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
                    function = "TIME_SERIES_INTRADAY";
                    key = "Time Series (" + interval + ")";
                    isIntraday = true;
                    break;
            }

            String url = String.format("%s?function=%s&symbol=%s&apikey=%s", baseUrl, function, symbol, apiKey);
            if (isIntraday) {
                url += "&interval=" + interval + "&extended_hours=true";
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

            List<Map<String, Object>> regularData = new ArrayList<>();
            List<Map<String, Object>> preMarketData = new ArrayList<>();

            ZoneId et = ZoneId.of("America/New_York");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            Iterator<String> fieldNames = timeSeries.fieldNames();
            while (fieldNames.hasNext()) {
                String timeStr = fieldNames.next();
                JsonNode dataNode = timeSeries.get(timeStr);

                Map<String, Object> point = new HashMap<>();
                point.put("time", timeStr);
                point.put("open", dataNode.get("1. open").asDouble());
                point.put("high", dataNode.get("2. high").asDouble());
                point.put("low", dataNode.get("3. low").asDouble());
                point.put("close", dataNode.get("4. close").asDouble());
                point.put("volume", dataNode.get("5. volume").asLong());

                if (isIntraday) {
                    LocalDateTime dt = LocalDateTime.parse(timeStr, formatter);
                    int hour = dt.atZone(et).getHour();
                    int minute = dt.atZone(et).getMinute();
                    if (hour < 9 || (hour == 9 && minute < 30)) {
                        preMarketData.add(point);
                    } else {
                        regularData.add(point);
                    }
                } else {
                    regularData.add(point);
                }
            }

            if (isIntraday) {
                preMarketData.sort(Comparator.comparing(p -> (String)p.get("time")));
            }
            regularData.sort(Comparator.comparing(p -> (String)p.get("time")));

            Map<String, Object> res = new HashMap<>();
            res.put("symbol", symbol);
            res.put("companyName", getCompanyName(symbol));
            res.put("data", regularData);
            if (isIntraday) res.put("preMarketData", preMarketData);

            return res;

        } catch (Exception e) {
            throw new RuntimeException("获取股票数据失败: " + e.getMessage(), e);
        }
    }

}
