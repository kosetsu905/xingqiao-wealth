package com.xingqiao.order.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.util.*;

@Service
public class PolygonService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String apiKey;
    private final String baseUrl;


    public PolygonService(
            @Value("${polygon.api.key}") String apiKey,
            @Value("${polygon.api.base-url:https://api.polygon.io}") String baseUrl) {
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public Map<String, Object> getMarketData(String stockCode) {
        try {
            String url = String.format("%s/v3/snapshot/indices?ticker=%s&apiKey=%s",
                    baseUrl, stockCode, apiKey);

            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Polygon API 请求失败: " + response.getStatusCode());
            }

            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode results = root.get("results");
            if (results == null || !results.isArray() || results.isEmpty()) {
                throw new RuntimeException("未获取到行情: " + root);
            }

            JsonNode tickerNode = results.get(0);
            JsonNode session = tickerNode.get("session");
            if (session == null) {
                throw new RuntimeException("未获取到 session 数据: " + tickerNode);
            }

            double currentPrice = session.get("close").asDouble(0);
            double prevClose = session.get("previous_close").asDouble(0);
            double change = session.get("change").asDouble(currentPrice - prevClose);
            double changePercent = session.get("change_percent").asDouble(
                    prevClose != 0 ? change / prevClose * 100 : 0
            );

            DecimalFormat df = new DecimalFormat("#,##0.00");
            DecimalFormat dfPercent = new DecimalFormat("+#0.00%;-#0.00%");

            String changeClass = change >= 0 ? "text-green-600" : "text-red-600";

            Map<String, Object> result = new HashMap<>();
            result.put("stockCode", stockCode);
            result.put("currentPrice", df.format(currentPrice));
            result.put("priceChange", (change >= 0 ? "+" : "") + df.format(change));
            result.put("priceChangePercent", dfPercent.format(changePercent / 100));
            result.put("changeClass", changeClass);
            result.put("bgClass", changeClass);

            return result;

        } catch (Exception e) {
            throw new RuntimeException("获取市场行情失败: " + e.getMessage(), e);
        }
    }

}
