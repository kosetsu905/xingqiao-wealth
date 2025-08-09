package com.xingqiao.digitalCurrency.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * WebSocket服务类
 * 
 * @author xingqiao
 */
@Component
@ServerEndpoint("/websocket")
public class WebsocketService {

    private static final Logger log = LoggerFactory.getLogger(WebsocketService.class);


    // concurrent包的线程安全Set，用来存放每个客户端对应的WebSocket对象
    private static CopyOnWriteArraySet<WebsocketService> webSocketSet = new CopyOnWriteArraySet<>();

    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);     // 加入set中
        log.info("有新连接加入！当前在线人数为{}", webSocketSet.size());
        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            log.error("IO异常", e);
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  // 从set中删除
        log.info("有一连接关闭！当前在线人数为{}", webSocketSet.size());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("来自客户端的消息:{}", message);
        // 群发消息
        for (WebsocketService item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                log.error("发送消息异常", e);
            }
        }
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误", error);
    }

    /**
     * 发送消息
     * 
     * @param message 消息内容
     * @throws IOException IO异常
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

}