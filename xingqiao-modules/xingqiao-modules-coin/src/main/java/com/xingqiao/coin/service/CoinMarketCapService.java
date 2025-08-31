package com.xingqiao.coin.service;

import com.xingqiao.coin.domain.vo.BitcoinPriceResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

@Service
public class CoinMarketCapService {
    private final RestTemplate restTemplate;
    private final String apiKey;
    private final String baseUrl;

    // 构造注入（从配置文件读取 API Key 和 Base URL）
    public CoinMarketCapService(@Value("${cmc.api.key:''}") String apiKey,
                                @Value("${cmc.api.base-url:''}") String baseUrl) {
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;

        // 创建带有超时配置的RestTemplate
        this.restTemplate = new RestTemplate();
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        // 连接超时时间设置为5秒
        factory.setConnectTimeout(5000);
        // 读取超时时间设置为10秒
        factory.setReadTimeout(10000);
        this.restTemplate.setRequestFactory(factory);
    }

    /**
     * 获取比特币实时价格（USD）
     */
    public double getBitcoinPrice() {
        try {
            // 构造 API 请求 URL
            String url = baseUrl + "/v1/cryptocurrency/quotes/latest";

            // 设置请求参数（比特币 ID=1，转换为 USD）
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                    .queryParam("id", 1)
                    .queryParam("convert", "USD");

            // 设置请求头（包含 API Key）
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-CMC_PRO_API_KEY", apiKey);
            headers.set("Accept", "application/json");

            // 发送 GET 请求并获取响应
            HttpEntity<Void> request = new HttpEntity<>(headers);
            ResponseEntity<BitcoinPriceResponse> response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    request,
                    BitcoinPriceResponse.class
            );

            // 解析价格（检查响应是否成功）
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody().getData().getQuote().getUsd().getPrice();
            } else {
                throw new RuntimeException("API 响应失败，状态码：" + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("获取比特币价格失败: " + e.getMessage() + "。请检查网络连接或稍后重试。", e);
        }
    }
}