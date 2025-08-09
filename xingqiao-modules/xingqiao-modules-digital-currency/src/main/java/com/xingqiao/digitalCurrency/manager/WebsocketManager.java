package com.xingqiao.digitalCurrency.manager;

import com.xingqiao.digitalCurrency.service.WebsocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * WebSocket管理器
 * 
 * @author xingqiao
 */
@Slf4j
@Service
public class WebsocketManager {

    /**
     * 发送消息给所有连接的客户端
     * 
     * @param message 消息内容
     */
    public void sendToAll(String message) {
        CopyOnWriteArraySet<WebsocketService> webSocketSet = getWebSocketSet();
        for (WebsocketService item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                log.error("发送消息异常", e);
            }
        }
    }

    /**
     * 获取当前连接数
     * 
     * @return 连接数
     */
    public int getOnlineCount() {
        CopyOnWriteArraySet<WebsocketService> webSocketSet = getWebSocketSet();
        return webSocketSet.size();
    }

    /**
     * 通过反射获取WebSocket连接集合
     * 
     * @return WebSocket连接集合
     */
    private CopyOnWriteArraySet<WebsocketService> getWebSocketSet() {
        // 这里使用反射获取WebsocketService中的webSocketSet静态变量
        try {
            java.lang.reflect.Field field = WebsocketService.class.getDeclaredField("webSocketSet");
            field.setAccessible(true);
            return (CopyOnWriteArraySet<WebsocketService>) field.get(null);
        } catch (Exception e) {
            log.error("获取WebSocket连接集合异常", e);
            return new CopyOnWriteArraySet<>();
        }
    }
}