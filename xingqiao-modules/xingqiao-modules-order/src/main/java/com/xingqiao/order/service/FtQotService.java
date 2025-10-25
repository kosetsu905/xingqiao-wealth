package com.xingqiao.order.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.futu.openapi.FTAPI;
import com.futu.openapi.FTAPI_Conn;
import com.futu.openapi.FTAPI_Conn_Qot;
import com.futu.openapi.FTSPI_Conn;
import com.futu.openapi.FTSPI_Qot;
import com.futu.openapi.pb.Common;
import com.futu.openapi.pb.QotCommon;
import com.futu.openapi.pb.QotGetBasicQot;
import com.futu.openapi.pb.QotGetSubInfo;
import com.futu.openapi.pb.QotSub;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;

@Slf4j
@Service
public class FtQotService implements FTSPI_Conn, FTSPI_Qot {

    private final FTAPI_Conn_Qot qot = new FTAPI_Conn_Qot();

    // 存储每个请求对应的 CompletableFuture，用序列号关联
    private final Map<Integer, CompletableFuture<String>> pendingRequests = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        log.info("🟢 正在初始化行情连接服务...");
        
        qot.setClientInfo("javaclient", 1);
        qot.setConnSpi(this);
        qot.setQotSpi(this);

        log.info("📡 正在连接行情服务器: 127.0.0.1:11111");
        try {
            qot.initConnect("127.0.0.1", (short) 11111, false);
        } catch (Exception e) {
            log.error("❌ 连接行情服务器时发生异常", e);
        }
    }

    @PreDestroy
    public void destroy() {
        log.info("🛑 正在关闭行情连接...");
        try {
            qot.close();
            log.info("✅ 行情连接已安全断开");
        } catch (Exception e) {
            log.error("❌ 关闭连接时发生异常", e);
        }
    }

    /**
     * 获取股票基础行情
     */
    public CompletableFuture<String> getBasicQot(String code, int market) {
        CompletableFuture<String> future = new CompletableFuture<>();

        QotCommon.Security sec = QotCommon.Security.newBuilder()
                .setMarket(market)
                .setCode(code)
                .build();
        
        QotGetBasicQot.C2S c2s = QotGetBasicQot.C2S.newBuilder()
                .addSecurityList(sec)
                .build();
        
        QotGetBasicQot.Request req = QotGetBasicQot.Request.newBuilder().setC2S(c2s).build();
        
        int seqNo = qot.getBasicQot(req);
        log.info("📤 发送 getBasicQot 请求 | seqNo={} | code={}", seqNo, code);

        if (seqNo <= 0) {
            future.completeExceptionally(new RuntimeException("获取行情请求失败: seqNo=" + seqNo));
        } else {
            pendingRequests.put(seqNo, future);
        }

        log.info("📊 待处理行情请求数量 | seqNo={} | 剩余={}", seqNo, pendingRequests.size());
        return future;
    }

    /**
     * 订阅股票行情
     */
    public CompletableFuture<String> subscribeQot(String code, int market) {
        CompletableFuture<String> future = new CompletableFuture<>();

        QotCommon.Security sec = QotCommon.Security.newBuilder()
                .setMarket(market)
                .setCode(code)
                .build();
        
        QotSub.C2S c2s = QotSub.C2S.newBuilder()
                .addSecurityList(sec)
                .addSubTypeList(QotCommon.SubType.SubType_Basic_VALUE)
                .setIsSubOrUnSub(true)
                .build();
        
        QotSub.Request req = QotSub.Request.newBuilder().setC2S(c2s).build();
        
        int seqNo = qot.sub(req);
        log.info("📤 发送 QotSub 请求 | seqNo={} | code={}", seqNo, code);

        if (seqNo <= 0) {
            future.completeExceptionally(new RuntimeException("订阅行情请求失败: seqNo=" + seqNo));
        } else {
            pendingRequests.put(seqNo, future);
        }

        log.info("📊 待处理订阅请求数量 | seqNo={} | 剩余={}", seqNo, pendingRequests.size());
        return future;
    }

    // --- 连接回调方法 ---
    @Override
    public void onInitConnect(FTAPI_Conn client, long errCode, String desc) {
        log.info("✅ 行情连接初始化结果 | ret={} | desc={} | connID={}", errCode, desc, client.getConnectID());
    }

    @Override
    public void onDisconnect(FTAPI_Conn client, long errCode) {
        log.warn("⚠️ 与行情服务器断开连接！ConnID={}, 错误码={}", client.getConnectID(), errCode);
        //todo: 断线重连机制
    }

    // --- 行情回调方法 ---
    @Override
    public void onReply_Sub(FTAPI_Conn client, int nSerialNo, QotSub.Response rsp) {
        CompletableFuture<String> future = pendingRequests.remove(nSerialNo);
        if (future == null) {
            log.warn("⚠️ 收到订阅响应但无对应请求 | seqNo={}", nSerialNo);
            return;
        }

        if (rsp.getRetType() != Common.RetType.RetType_Succeed_VALUE) {
            String errorMsg = rsp.getRetMsg();
            log.warn("❌ 订阅行情失败 | seqNo={} | msg={}", nSerialNo, errorMsg);
            future.completeExceptionally(new RuntimeException(errorMsg));
        } else {
            try {
                String json = JsonFormat.printer().print(rsp);
                log.info("✅ 订阅行情成功！| seqNo={} | {}", nSerialNo, json.substring(0, Math.min(json.length(), 100)) + "...");
                future.complete(json);
            } catch (InvalidProtocolBufferException e) {
                log.error("❌ Protobuf 转 JSON 失败", e);
                future.completeExceptionally(e);
            }
        }

        log.info("📊 待处理订阅请求数量 | seqNo={} | 剩余={}", nSerialNo, pendingRequests.size());
    }

    @Override
    public void onReply_GetBasicQot(FTAPI_Conn client, int nSerialNo, QotGetBasicQot.Response rsp) {
        CompletableFuture<String> future = pendingRequests.remove(nSerialNo);
        if (future == null) {
            log.warn("⚠️ 收到行情响应但无对应请求 | seqNo={}", nSerialNo);
            return;
        }

        if (rsp.getRetType() != Common.RetType.RetType_Succeed_VALUE) {
            String errorMsg = rsp.getRetMsg();
            log.warn("❌ 获取基础行情失败 | seqNo={} | msg={}", nSerialNo, errorMsg);
            future.completeExceptionally(new RuntimeException(errorMsg));
        } else {
            try {
                String json = JsonFormat.printer().print(rsp);
                log.info("✅ 成功获取基础行情 | seqNo={} | {}", nSerialNo, json.substring(0, Math.min(json.length(), 100)) + "...");
                future.complete(json);
            } catch (InvalidProtocolBufferException e) {
                log.error("❌ Protobuf 转 JSON 失败", e);
                future.completeExceptionally(e);
            }
        }

        log.info("📊 待处理行情请求数量 | seqNo={} | 剩余={}", nSerialNo, pendingRequests.size());
    }

}