package com.xingqiao.order.service;

import com.xingqiao.api.trade.domain.QueryStockQuote;
import com.xingqiao.common.redis.service.RedisService;
import com.xingqiao.order.config.TradeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.websocket.CloseReason;
import javax.websocket.Session;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;

/**
 * WebSocket服务类
 * 管理WebSocket连接和消息发送
 *
 * @author xingqiao
 * @date 2025-09-24
 */
@Service
@EnableScheduling
public class WebSocketService {

    private static final Logger log = LoggerFactory.getLogger(WebSocketService.class);

    @Autowired
    private RedisService redisService;

    /**
     * 不活跃会话超时时间（毫秒）
     * 设为90秒，考虑到前端应该每隔30-45秒发送一次心跳
     */
    private static final long INACTIVE_TIMEOUT = 90000;

    /**
     * 所有活动的WebSocket会话（本地存储，用于实时连接管理）
     */
    private final CopyOnWriteArraySet<Session> sessions = new CopyOnWriteArraySet<>();

    /**
     * 会话ID与会话的映射（本地存储，Session对象不适合序列化到Redis）
     */
    private final Map<String, Session> sessionMap = new ConcurrentHashMap<>();

    /**
     * 会话ID与最后活动时间的映射（本地存储）
     */
    private final Map<String, Long> sessionActivityMap = new ConcurrentHashMap<>();

    /**
     * 添加会话
     * @param session WebSocket会话
     */
    public void addSession(Session session) {
        sessions.add(session);
        sessionMap.put(session.getId(), session);
        // 初始化会话活动时间
        long now = System.currentTimeMillis();
        sessionActivityMap.put(session.getId(), now);
        // 将会话ID存储到Redis，设置过期时间
        redisService.addCacheSet(TradeConstants.SESSION_KEY_PREFIX , session.getId());
        redisService.expire(TradeConstants.SESSION_KEY_PREFIX, TradeConstants.EXPIRE_TIME, TimeUnit.SECONDS);
        log.info("WebSocket连接已建立，会话ID：{}，当前在线人数：{}", session.getId(), sessions.size());
    }

    /**
     * 移除会话
     * @param session WebSocket会话
     */
    public void removeSession(Session session) {
        try {
            String sessionId = session.getId();
            sessions.remove(session);
            sessionMap.remove(sessionId);
            sessionActivityMap.remove(sessionId);
            // 从Redis中移除相关数据
            Long userId = getUserBySessionId(sessionId);
            if (userId != null) {
                redisService.deleteObject(TradeConstants.USER_SESSION_KEY_PREFIX + userId);
            }
            redisService.deleteObject(TradeConstants.SESSION_USER_KEY_PREFIX + sessionId);
            //删除会话id
            redisService.removeCacheSet(TradeConstants.SESSION_KEY_PREFIX , sessionId);
            // 从会话-股票映射中移除
            redisService.deleteObject(TradeConstants.STOCK_GLOBAL_INDICES_INFO_PREFIX + sessionId);
            redisService.deleteObject(TradeConstants.STOCK_HOT_INFO_PREFIX + sessionId);
            log.info("WebSocket连接已关闭，会话ID：{}，当前在线人数：{}", sessionId, sessions.size());
        }catch (Exception e){
            log.error("处理用户 {} 断开连接异常：{}", session.getId(), e.getMessage());
        }

    }

    /**
     * 绑定用户ID与会话
     * @param userId 用户ID
     * @param session WebSocket会话
     */
    public void bindUserSession(Long userId, Session session) {
        // 将会话ID存储到Redis，与用户ID关联
        redisService.setCacheObject(TradeConstants.USER_SESSION_KEY_PREFIX + userId, session.getId(), TradeConstants.EXPIRE_TIME, TimeUnit.SECONDS);
        // 存储反向映射，方便通过会话ID查找用户ID
        redisService.setCacheObject(TradeConstants.SESSION_USER_KEY_PREFIX + session.getId(), userId, TradeConstants.EXPIRE_TIME, TimeUnit.SECONDS);
    }

    /**
     * 根据会话ID获取用户ID
     * @param sessionId 会话ID
     * @return 用户ID
     */
    public Long getUserBySessionId(String sessionId) {
        return (Long)redisService.getCacheObject(TradeConstants.SESSION_USER_KEY_PREFIX + sessionId);
    }

    /**
     * 更新会话最后活动时间
     * @param sessionId 会话ID
     * @param lastActivityTime 最后活动时间
     */
    public void updateLastActivityTime(String sessionId, long lastActivityTime) {
        sessionActivityMap.put(sessionId, lastActivityTime);
    }

    /**
     * 发送消息给指定会话
     * @param session WebSocket会话
     * @param message 消息内容
     */
    public void sendMessage(Session session, String message) {
        if (session != null && session.isOpen()) {
            try {
                session.getBasicRemote().sendText(message);
                // 更新会话活动时间
                updateLastActivityTime(session.getId(), System.currentTimeMillis());
                log.debug("发送WebSocket消息成功，会话ID：{}", session.getId());
            } catch (Exception e) {
                log.error("发送WebSocket消息失败：{}", e.getMessage());
                // 发送失败时自动清理失效会话
                try {
                    log.warn("检测到失效会话，自动清理，会话ID：{}", session.getId());
                    removeSession(session);
                } catch (Exception ex) {
                    log.error("清理失效会话异常：{}", ex.getMessage());
                }
            }
        } else {
            log.warn("会话已关闭，无法发送消息");
            // 会话已关闭但仍在会话池中，需要清理
            if (session != null) {
                try {
                    removeSession(session);
                } catch (Exception e) {
                    log.error("清理已关闭会话异常：{}", e.getMessage());
                }
            }
        }
    }

    /**
     * 发送消息给指定用户
     * @param userId 用户ID
     * @param message 消息内容
     */
    public void sendMessageToUser(Long userId, String message) {
        // 从Redis获取用户对应的会话ID
        String sessionId = redisService.getCacheObject(TradeConstants.USER_SESSION_KEY_PREFIX + userId);
        if (sessionId != null) {
            // 从本地获取会话对象并发送消息
            Session session = getSessionById(sessionId);
            sendMessage(session, message);
        } else {
            log.warn("用户 {} 未找到对应的WebSocket会话", userId);
        }
    }

    /**
     * 广播消息给所有会话
     * @param message 消息内容
     */
    public void broadcastMessage(String message) {
        for (Session session : sessions) {
            sendMessage(session, message);
        }
    }

    /**
     * 获取当前在线人数
     * @return 在线人数
     */
    public int getOnlineCount() {
        return sessions.size();
    }

    /**
     * 根据会话ID获取会话
     * @param sessionId 会话ID
     * @return 会话对象
     */
    public Session getSessionById(String sessionId) {
        return sessionMap.get(sessionId);
    }

    /**
     * 根据会话ID发送消息
     * @param sessionId 会话ID
     * @param message 消息内容
     */
    public void sendMessageBySessionId(String sessionId, String message) {
        Session session = getSessionById(sessionId);
        sendMessage(session, message);
    }

    /**
     * 定时清理不活跃的会话
     * 每隔30秒执行一次
     */
    @Scheduled(fixedRate = 30000)
    public void cleanupInactiveSessions() {
        long currentTime = System.currentTimeMillis();
        int cleanedCount = 0;

        try {
            for (Map.Entry<String, Long> entry : new HashMap<>(sessionActivityMap).entrySet()) {
                String sessionId = entry.getKey();
                Long lastActivityTime = entry.getValue();

                // 检查会话是否超时
                if (currentTime - lastActivityTime > INACTIVE_TIMEOUT) {
                    Session session = sessionMap.get(sessionId);
                    if (session != null) {
                        log.warn("检测到不活跃会话，会话ID：{}，最后活动时间：{}，准备清理",
                                sessionId, new Date(lastActivityTime));
                        try {
                            // 先尝试正常关闭连接
                            if (session.isOpen()) {
                                session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "会话超时"));
                            }
                        } catch (Exception e) {
                            log.error("关闭不活跃会话异常：{}", e.getMessage());
                        } finally {
                            // 无论关闭是否成功，都从会话池中移除
                            removeSession(session);
                            cleanedCount++;
                        }
                    } else {
                        // 会话已不存在，但活动记录还在，清理活动记录
                        sessionActivityMap.remove(sessionId);
                    }
                }
            }

            if (cleanedCount > 0) {
                log.info("清理完成，共清理 {} 个不活跃会话，当前在线人数：{}", cleanedCount, sessions.size());
            } else {
                log.debug("当前没有需要清理的不活跃会话，在线人数：{}", sessions.size());
            }
        } catch (Exception e) {
            log.error("清理不活跃会话时发生异常：{}", e.getMessage());
        }
    }
}
