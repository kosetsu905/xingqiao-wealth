package com.xingqiao.order.websocket;

import com.alibaba.fastjson2.JSONObject;
import java.util.List;
import com.xingqiao.api.trade.domain.QueryStockQuote;
import com.xingqiao.api.trade.domain.QueryStockQuoteList;
import com.xingqiao.order.service.QuoteApiService;
import com.xingqiao.order.service.WebSocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.net.URI;
import java.util.Map;

/**
 * 股票行情WebSocket端点
 * 处理股票行情订阅相关的WebSocket连接
 * 
 * @author xingqiao
 * @date 2025-09-24
 */
@Component
@ServerEndpoint(value = "/ws/stock/{userId}")
public class StockQuoteWebSocket {

    private static final Logger log = LoggerFactory.getLogger(StockQuoteWebSocket.class);

    // 由于WebSocket是多线程的，这里使用静态变量并通过Spring上下文获取Bean
    private static WebSocketService webSocketService;
    private static QuoteApiService quoteApiService;


    // 当前会话
    private Session session;

    // 用户ID
    private Long userId;

    /**
     * 注入WebSocketService
     * 由于WebSocket是多线程的，需要使用静态方法注入
     */
    @Autowired
    public void setWebSocketService(WebSocketService webSocketService,QuoteApiService quoteApiService) {
        StockQuoteWebSocket.webSocketService = webSocketService;
        StockQuoteWebSocket.quoteApiService = quoteApiService;
        log.info("WebSocketService已注入到StockQuoteWebSocket");
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") Long userId) {
        // 增加详细的连接日志，记录连接路径和参数
        URI requestUri = session.getRequestURI();
        Map<String, List<String>> parameterMap = session.getRequestParameterMap();
        log.info("收到WebSocket连接请求，URI: {}, 参数: {}, 用户ID: {}", requestUri, parameterMap, userId);
        
        this.session = session;
        this.userId = userId;
        
        try {
            // 添加会话到服务管理
            webSocketService.addSession(session);
            webSocketService.bindUserSession(userId, session);
            log.info("用户 {} 连接WebSocket成功，会话ID：{}，当前在线人数：{}", userId, session.getId(), webSocketService.getOnlineCount());
            // 发送连接成功消息
            JSONObject response = new JSONObject();
            response.put("type", "connect_success");
            response.put("userId", userId);
            response.put("message", "WebSocket连接成功");
            sendMessage(response.toJSONString());
        } catch (Exception e) {
            log.error("用户 {} 连接WebSocket失败：{}", userId, e.getMessage(), e);
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        try {
            webSocketService.removeSession(session);
            log.info("用户 {} 断开WebSocket连接，会话ID：{}，当前在线人数：{}", userId, session.getId(), webSocketService.getOnlineCount());
        } catch (Exception e) {
            log.error("处理用户 {} 断开连接异常：{}", userId, e.getMessage());
        }
    }

    /**
     * 收到客户端消息后调用的方法
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.debug("收到用户 {} 的消息：{}", userId, message);
        try {
            // 解析消息内容
            JSONObject msgObj = JSONObject.parseObject(message);
            msgObj.put("userId", userId);
            String action = msgObj.getString("action");
            
            // 处理心跳消息
            if ("ping".equals(action)) {
                // 回复心跳消息
                JSONObject pongResponse = new JSONObject();
                pongResponse.put("type", "pong");
                pongResponse.put("timestamp", System.currentTimeMillis());
                sendMessage(pongResponse.toJSONString());
                log.debug("用户 {} 的心跳消息已响应", userId);
                return;
            }
            
            if (action == null || action.isEmpty()) {
                log.error("消息类型不能为空");
                sendErrorMessage("消息类型不能为空");
                return;
            }
            switch (action) {
                case "subscribe":
                    // 处理订阅请求，新格式：{"action":"subscribe", "params":[{"stockCode":"","marketCode":""},...]}
                    subscribeStock(msgObj);
                    break;
                case "unsubscribe":
                    // 处理取消订阅请求，支持相同的params格式
                    unsubscribeStock(msgObj);
                    break;
                default:
                    log.error("未知消息类型：{}", action);
                    sendErrorMessage("未知消息类型：" + action);
            }
        } catch (Exception e) {
            log.error("处理消息异常：{}", e.getMessage());
            sendErrorMessage("处理消息异常：" + e.getMessage());
        }
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("WebSocket发生错误，会话ID：{}，用户ID：{}，错误信息：{}", session.getId(), userId, error.getMessage(), error);

        // 避免在错误处理中发送消息，因为这可能导致循环错误
        // 直接关闭连接即可
        try {
            if (session.isOpen()) {
                session.close();
            }
        } catch (Exception e) {
            log.error("关闭错误会话异常：{}", e.getMessage());
        }
    }

    /**
     * 订阅股票行情
     */
    private void subscribeStock(JSONObject msgObj) {
        try {
            List<QueryStockQuote> list = msgObj.getList("params", QueryStockQuote.class);
            // 将会话与股票代码绑定
            QueryStockQuoteList queryStockQuoteList=new QueryStockQuoteList();
            queryStockQuoteList.setList(list);
            queryStockQuoteList.setSessionId(session.getId());
            queryStockQuoteList.setUserId(userId);
            queryStockQuoteList.setDataType(msgObj.getString("dataType"));
            quoteApiService.subscribeStockQuote(queryStockQuoteList);
            // 发送订阅成功消息
            JSONObject response = new JSONObject();
            response.put("type", "subscribe_success");
            response.put("message", "订阅股票行情成功");
            
            sendMessage(response.toJSONString());
            log.info("用户 {} 订阅股票成功", userId);
        } catch (Exception e) {
            log.error("用户 {} 订阅股票失败：{}", userId, e.getMessage());
            sendErrorMessage("订阅股票行情失败：" + e.getMessage());
        }
    }

    /**
     * 取消订阅股票行情
     */
    private void unsubscribeStock(JSONObject msgObj) {
        try {
            List<QueryStockQuote> list = msgObj.getList("params", QueryStockQuote.class);

            QueryStockQuoteList queryStockQuoteList=new QueryStockQuoteList();
            queryStockQuoteList.setList(list);
            queryStockQuoteList.setSessionId(session.getId());
            queryStockQuoteList.setUserId(userId);
            quoteApiService.unsubscribeStockQuote(queryStockQuoteList);

            // 发送取消订阅成功消息
            JSONObject response = new JSONObject();
            response.put("type", "unsubscribe_success");
            response.put("message", "取消订阅股票行情成功");

            sendMessage(response.toJSONString());
            log.info("用户 {} 取消订阅股票成功", userId);
        } catch (Exception e) {
            log.error("用户 {} 取消订阅股票失败：{}", userId, e.getMessage());
            sendErrorMessage("取消订阅股票行情失败：" + e.getMessage());
        }
    }

    /**
     * 发送消息到客户端
     */
    private void sendMessage(String message) {
        if (session != null && session.isOpen()) {
            webSocketService.sendMessage(session, message);
        } else {
            log.warn("会话已关闭，无法发送消息");
        }
    }
    
    /**
     * 发送错误消息到客户端
     */
    private void sendErrorMessage(String errorMessage) {
        JSONObject error = new JSONObject();
        error.put("type", "error");
        error.put("message", errorMessage);
        sendMessage(error.toJSONString());
    }
}
