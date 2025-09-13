package com.xingqiao.order.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import com.futu.openapi.FTAPI;
import com.futu.openapi.pb.*;
import com.futu.openapi.*;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;

@Slf4j
@Service
public class FtTrdService implements FTSPI_Conn, FTSPI_Trd{

    private final FTAPI_Conn_Trd trd = new FTAPI_Conn_Trd();

    // 存储每个请求对应的 CompletableFuture，用序列号关联
    private final Map<Integer, CompletableFuture<String>> pendingRequests = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        log.info("🟢 正在初始化交易连接服务...");
        FTAPI.init();
        log.debug("🔧 FTAPI SDK 已初始化");

        trd.setClientInfo("javaclient", 1);
        trd.setConnSpi(this);
        trd.setTrdSpi(this);

        log.info("📡 正在连接交易服务器: 127.0.0.1:11111");
        try {
            trd.initConnect("127.0.0.1", (short) 11111, false);
        } catch (Exception e) {
            log.error("❌ 连接交易服务器时发生异常", e);
        }
    }

    @PreDestroy
    public void destroy() {
        log.info("🛑 正在关闭交易连接...");
        try {
            trd.close();
            log.info("✅ 交易连接已安全断开");
        } catch (Exception e) {
            log.error("❌ 关闭连接时发生异常", e);
        }
    }

    @Override
    public void onInitConnect(FTAPI_Conn client, long errCode, String desc) {
        if (errCode == 0) {
            log.info("✅ 交易连接成功！ConnID={}", client.getConnectID());
        } else {
            log.error("❌ 交易连接失败！错误码={}, 描述={}", errCode, desc);
        }
    }

    @Override
    public void onDisconnect(FTAPI_Conn client, long errCode) {
        log.warn("⚠️ 与交易服务器断开连接！ConnID={}, 错误码={}", client.getConnectID(), errCode);

        //todo: 断线重连机制
    }



    /**
     * 提供给 Controller 调用的方法：获取账户列表
     */
    public CompletableFuture<String> getAccountList(long userId) {
        CompletableFuture<String> future = new CompletableFuture<>();

        TrdGetAccList.C2S c2s = TrdGetAccList.C2S.newBuilder()
                .setUserID(userId)
                .setTrdCategory(TrdCommon.TrdCategory.TrdCategory_Security_VALUE)
                .setNeedGeneralSecAccount(true)
                .build();

        TrdGetAccList.Request req = TrdGetAccList.Request.newBuilder().setC2S(c2s).build();

        int seqNo = trd.getAccList(req);
        log.info("📤 发送 getAccList 请求 | seqNo={}", seqNo);

        if (seqNo <= 0) {
            future.completeExceptionally(new RuntimeException("发送请求失败: seqNo=" + seqNo));
        } else {
            pendingRequests.put(seqNo, future); // 记录等待结果
        }

        // ✅ 关键：记录处理完本次响应后，剩余的待处理请求数量
        log.info("📊 待处理请求数量 | seqNo={} | 剩余={}", seqNo, pendingRequests.size());

        return future;
    }


    // --- 回调方法 ---
    @Override
    public void onReply_GetAccList(FTAPI_Conn client, int nSerialNo, TrdGetAccList.Response rsp) {

        CompletableFuture<String> future = pendingRequests.remove(nSerialNo);
        if (future == null) {
            log.warn("⚠️ 收到响应但无对应请求 | seqNo={}", nSerialNo);
            return;
        }

        if (rsp.getRetType() != 0) {
            log.warn("❌ getAccList 失败 | seqNo={} | msg={}", nSerialNo, rsp.getRetMsg());
            future.completeExceptionally(new RuntimeException(rsp.getRetMsg()));
        } else {
            try {
                String json = JsonFormat.printer().print(rsp);
                log.info("✅ 收到账户列表 | seqNo={} | {}", nSerialNo, json.substring(0, Math.min(json.length(), 100)) + "...");
                future.complete(json);
            } catch (InvalidProtocolBufferException e) {
                log.error("❌ Protobuf 转 JSON 失败", e);
                future.completeExceptionally(e);
            }
        }

        // ✅ 关键：记录处理完本次响应后，剩余的待处理请求数量
        log.info("📊 待处理请求数量 | seqNo={} | 剩余={}", nSerialNo, pendingRequests.size());
    }




    /**
     * 提供给 Controller 调用的方法：获取账户资金
     *
     * @param accId    账户 ID
     * @param trdEnv   交易环境：0-模拟，1-真实
     * @param currency 货币类型：HKD=1, USD=2, CNY=3
     * @return CompletableFuture<String> 返回 JSON 格式的资金信息
     */
    public CompletableFuture<String> getFunds(long accId, int trdEnv, int currency) {
        CompletableFuture<String> future = new CompletableFuture<>();

        // 构建交易头
        TrdCommon.TrdHeader header = TrdCommon.TrdHeader.newBuilder()
                .setAccID(accId)
                .setTrdEnv(trdEnv)  // TrdEnv_Real = 1, TrdEnv_Simulate = 0
                .setTrdMarket(TrdCommon.TrdMarket.TrdMarket_HK_VALUE) // 港股市场
                .build();

        // 构建 C2S
        TrdGetFunds.C2S c2s = TrdGetFunds.C2S.newBuilder()
                .setHeader(header)
                .setCurrency(currency) // HKD = 1
                .build();

        // 构建 Request
        TrdGetFunds.Request req = TrdGetFunds.Request.newBuilder().setC2S(c2s).build();

        // 发送请求
        int seqNo = trd.getFunds(req);
        log.info("📤 发送 getFunds 请求 | seqNo={} | accId={}", seqNo, accId);

        if (seqNo <= 0) {
            future.completeExceptionally(new RuntimeException("发送请求失败: seqNo=" + seqNo));
        } else {
            pendingRequests.put(seqNo, future); // 记录等待结果
        }

        // ✅ 关键：记录处理完本次响应后，剩余的待处理请求数量
        log.info("📊 待处理请求数量 | seqNo={} | 剩余={}", seqNo, pendingRequests.size());

        return future;
    }


    @Override
    public void onReply_GetFunds(FTAPI_Conn client, int nSerialNo, TrdGetFunds.Response rsp) {
        CompletableFuture<String> future = pendingRequests.remove(nSerialNo);

        if (future == null) {
            log.warn("⚠️ 收到资金响应但无对应请求 | seqNo={}", nSerialNo);
            return;
        }

        if (rsp.getRetType() != 0) {
            String errorMsg = rsp.getRetMsg();
            log.warn("❌ 获取资金失败 | seqNo={} | msg={}", nSerialNo, errorMsg);
            future.completeExceptionally(new RuntimeException(errorMsg));
        } else {
            try {
                String json = JsonFormat.printer().print(rsp);
                log.info("✅ 成功获取资金信息 | seqNo={} | {}", nSerialNo, json.substring(0, Math.min(json.length(), 100)) + "...");
                future.complete(json);
            } catch (InvalidProtocolBufferException e) {
                log.error("❌ Protobuf 转 JSON 失败", e);
                future.completeExceptionally(e);
            }
        }

        // ✅ 关键：记录处理完本次响应后，剩余的待处理请求数量
        log.info("📊 待处理请求数量 | seqNo={} | 剩余={}", nSerialNo, pendingRequests.size());
    }


    /**
     * 提供给 Controller 调用的方法：获取指定股票在当前价格下的最大可买卖数量
     *
     * @param accId       账户 ID
     * @param trdEnv      交易环境：0-模拟，1-真实
     * @param trdMarket   交易市场：如 TrdCommon.TrdMarket.TrdMarket_HK_VALUE
     * @param orderType   订单类型：如 TrdCommon.OrderType.OrderType_Normal_VALUE
     * @param code        股票代码，如 "00700"
     * @param price       价格（单位：元）
     * @param secMarket   证券市场：如 TrdCommon.TrdSecMarket.TrdSecMarket_HK_VALUE
     * @return CompletableFuture<String> 返回 JSON 格式的最大可交易数量信息
     */
    public CompletableFuture<String> getMaxTrdQtys(
            long accId,
            int trdEnv,
            int trdMarket,
            int orderType,
            String code,
            double price,
            int secMarket) {

        CompletableFuture<String> future = new CompletableFuture<>();

        // 构建交易头
        TrdCommon.TrdHeader header = TrdCommon.TrdHeader.newBuilder()
                .setAccID(accId)
                .setTrdEnv(trdEnv)
                .setTrdMarket(trdMarket)
                .build();

        // 构建 C2S
        TrdGetMaxTrdQtys.C2S c2s = TrdGetMaxTrdQtys.C2S.newBuilder()
                .setHeader(header)
                .setOrderType(orderType)
                .setCode(code)
                .setPrice(price)
                .setSecMarket(secMarket)
                .build();

        // 构建 Request
        TrdGetMaxTrdQtys.Request req = TrdGetMaxTrdQtys.Request.newBuilder().setC2S(c2s).build();

        // 发送请求
        int seqNo = trd.getMaxTrdQtys(req);
        log.info("📤 发送 getMaxTrdQtys 请求 | seqNo={} | accId={} | code={} | price={}", seqNo, accId, code, price);

        if (seqNo <= 0) {
            future.completeExceptionally(new RuntimeException("发送请求失败: seqNo=" + seqNo));
        } else {
            pendingRequests.put(seqNo, future);
        }

        log.info("📊 待处理请求数量 | seqNo={} | 剩余={}", seqNo, pendingRequests.size());
        return future;
    }


    @Override
    public void onReply_GetMaxTrdQtys(FTAPI_Conn client, int nSerialNo, TrdGetMaxTrdQtys.Response rsp) {
        CompletableFuture<String> future = pendingRequests.remove(nSerialNo);
        if (future == null) {
            log.warn("⚠️ 收到 getMaxTrdQtys 响应但无对应请求 | seqNo={}", nSerialNo);
            return;
        }

        if (rsp.getRetType() != 0) {
            String errorMsg = rsp.getRetMsg();
            log.warn("❌ 获取最大可买卖数量失败 | seqNo={} | msg={}", nSerialNo, errorMsg);
            future.completeExceptionally(new RuntimeException(errorMsg));
        } else {
            try {
                String json = JsonFormat.printer().print(rsp);
                log.info("✅ 成功获取最大可买卖数量 | seqNo={} | {}", nSerialNo, json.substring(0, Math.min(json.length(), 100)) + "...");
                future.complete(json);
            } catch (InvalidProtocolBufferException e) {
                log.error("❌ Protobuf 转 JSON 失败", e);
                future.completeExceptionally(e);
            }
        }

        log.info("📊 待处理请求数量 | seqNo={} | 剩余={}", nSerialNo, pendingRequests.size());
    }


    /**
     * 查询指定账户的持仓列表
     *
     * @param accId     账户 ID，例如 16097643
     * @param trdEnv    交易环境：0=模拟，1=真实
     * @param trdMarket 交易市场：1=港股，2=A股，3=美股等
     * @return CompletableFuture<String> 返回 Protobuf 转 JSON 的持仓数据
     */
    public CompletableFuture<String> getPositionList(long accId, int trdEnv, int trdMarket) {
        CompletableFuture<String> future = new CompletableFuture<>();

        // 构建交易头
        TrdCommon.TrdHeader header = TrdCommon.TrdHeader.newBuilder()
                .setAccID(accId)
                .setTrdEnv(trdEnv)
                .setTrdMarket(trdMarket)
                .build();

        // 构建 C2S
        TrdGetPositionList.C2S c2s = TrdGetPositionList.C2S.newBuilder()
                .setHeader(header)
                .build();

        // 构建 Request
        TrdGetPositionList.Request req = TrdGetPositionList.Request.newBuilder().setC2S(c2s).build();

        // 发送请求
        int seqNo = trd.getPositionList(req);
        log.info("📊 发送查询持仓请求 | seqNo={} | accId={} | env={} | market={}", seqNo, accId, trdEnv, trdMarket);

        if (seqNo <= 0) {
            future.completeExceptionally(new RuntimeException("请求发送失败: seqNo=" + seqNo));
        } else {
            pendingRequests.put(seqNo, future);
        }

        log.info("📊 待处理请求数量 | seqNo={} | 剩余={}", seqNo, pendingRequests.size());
        return future;
    }

    @Override
    public void onReply_GetPositionList(FTAPI_Conn client, int nSerialNo, TrdGetPositionList.Response rsp) {
        CompletableFuture<String> future = pendingRequests.remove(nSerialNo);
        if (future == null) {
            log.warn("⚠️ 收到持仓响应但无对应请求 | seqNo={}", nSerialNo);
            return;
        }

        if (rsp.getRetType() != 0) {
            String errorMsg = rsp.getRetMsg();
            log.warn("❌ 查询持仓失败 | seqNo={} | msg={}", nSerialNo, errorMsg);
            future.completeExceptionally(new RuntimeException(errorMsg));
        } else {
            try {
                String json = JsonFormat.printer().print(rsp);
                log.info("✅ 成功获取持仓列表 | seqNo={} | {}", nSerialNo, json.substring(0, Math.min(json.length(), 100)) + "...");
                future.complete(json);
            } catch (InvalidProtocolBufferException e) {
                log.error("❌ Protobuf 转 JSON 失败", e);
                future.completeExceptionally(e);
            }
        }

        log.info("📊 待处理请求数量 | seqNo={} | 剩余={}", nSerialNo, pendingRequests.size());
    }


    /**
     * 提供给 Controller 调用的方法：获取单个股票的融资融券比率
     *
     * @param accId      账户 ID
     * @param trdEnv     交易环境：0-模拟，1-真实
     * @param trdMarket  交易市场，如 TrdCommon.TrdMarket.TrdMarket_HK_VALUE
     * @param code       股票代码，如 "00700"
     * @param secMarket  证券市场，如 QotCommon.QotMarket.QotMarket_HK_Security_VALUE
     * @return CompletableFuture<String> 返回 JSON 格式的融资融券比率信息
     */
    public CompletableFuture<String> getMarginRatio(
            long accId,
            int trdEnv,
            int trdMarket,
            String code,
            int secMarket) {

        CompletableFuture<String> future = new CompletableFuture<>();

        // 构建交易头
        TrdCommon.TrdHeader header = TrdCommon.TrdHeader.newBuilder()
                .setAccID(accId)
                .setTrdEnv(trdEnv)
                .setTrdMarket(trdMarket)
                .build();

        // 构建单个 Security
        QotCommon.Security security = QotCommon.Security.newBuilder()
                .setCode(code)
                .setMarket(secMarket)
                .build();

        // 构建 C2S
        TrdGetMarginRatio.C2S c2s = TrdGetMarginRatio.C2S.newBuilder()
                .setHeader(header)
                .addSecurityList(security)  // 只添加一个
                .build();

        // 构建 Request
        TrdGetMarginRatio.Request req = TrdGetMarginRatio.Request.newBuilder().setC2S(c2s).build();

        // 发送请求
        int seqNo = trd.getMarginRatio(req);
        log.info("📤 发送 getMarginRatio 请求 | seqNo={} | accId={} | code={}", seqNo, accId, code);

        if (seqNo <= 0) {
            future.completeExceptionally(new RuntimeException("发送请求失败: seqNo=" + seqNo));
        } else {
            pendingRequests.put(seqNo, future);
        }

        log.info("📊 待处理请求数量 | seqNo={} | 剩余={}", seqNo, pendingRequests.size());
        return future;
    }

    @Override
    public void onReply_GetMarginRatio(FTAPI_Conn client, int nSerialNo, TrdGetMarginRatio.Response rsp) {
        CompletableFuture<String> future = pendingRequests.remove(nSerialNo);
        if (future == null) {
            log.warn("⚠️ 收到 getMarginRatio 响应但无对应请求 | seqNo={}", nSerialNo);
            return;
        }

        if (rsp.getRetType() != 0) {
            String errorMsg = rsp.getRetMsg();
            log.warn("❌ 获取融资融券比率失败 | seqNo={} | msg={}", nSerialNo, errorMsg);
            future.completeExceptionally(new RuntimeException(errorMsg));
        } else {
            try {
                String json = JsonFormat.printer().print(rsp);
                log.info("✅ 成功获取融资融券比率 | seqNo={} | {}", nSerialNo, json.substring(0, Math.min(json.length(), 100)) + "...");
                future.complete(json);
            } catch (InvalidProtocolBufferException e) {
                log.error("❌ Protobuf 转 JSON 失败", e);
                future.completeExceptionally(e);
            }
        }

        log.info("📊 待处理请求数量 | seqNo={} | 剩余={}", nSerialNo, pendingRequests.size());
    }



    /**
     * 获取指定账户在某一清算日的交易流水摘要（如成交、费用、资金变动等）
     *
     * @param accId         账户 ID，例如 16097643
     * @param trdEnv        交易环境：0=模拟，1=真实
     * @param trdMarket     交易市场：1=港股，2=A股等
     * @param clearingDate  清算日期，格式 "YYYY-MM-DD"，如 "2025-02-18"
     * @return CompletableFuture<String> 返回 JSON 格式的响应结果
     */
    public CompletableFuture<String> getFlowSummary(
            long accId,
            int trdEnv,
            int trdMarket,
            String clearingDate) {

        CompletableFuture<String> future = new CompletableFuture<>();

        // 构建交易头
        TrdCommon.TrdHeader header = TrdCommon.TrdHeader.newBuilder()
                .setAccID(accId)
                .setTrdEnv(trdEnv)                 // 0: 模拟, 1: 真实
                .setTrdMarket(trdMarket)           // 1: 港股
                .build();

        // 构建 C2S
        TrdFlowSummary.C2S c2s = TrdFlowSummary.C2S.newBuilder()
                .setHeader(header)
                .setClearingDate(clearingDate)     // 必须是 YYYY-MM-DD 格式
                .build();

        // 构建 Request
        TrdFlowSummary.Request req = TrdFlowSummary.Request.newBuilder().setC2S(c2s).build();

        // 发送请求
        int seqNo = trd.getFlowSummary(req);
        log.info("📤 发送 getFlowSummary 请求 | seqNo={} | accId={} | date={}", seqNo, accId, clearingDate);

        if (seqNo <= 0) {
            future.completeExceptionally(new RuntimeException("发送请求失败: seqNo=" + seqNo));
        } else {
            pendingRequests.put(seqNo, future);
        }

        log.info("📊 待处理请求数量 | seqNo={} | 剩余={}", seqNo, pendingRequests.size());
        return future;
    }


    @Override
    public void onReply_GetFlowSummary(FTAPI_Conn client, int nSerialNo, TrdFlowSummary.Response rsp) {
        CompletableFuture<String> future = pendingRequests.remove(nSerialNo);
        if (future == null) {
            log.warn("⚠️ 收到 getFlowSummary 响应但无对应请求 | seqNo={}", nSerialNo);
            return;
        }

        if (rsp.getRetType() != 0) {
            String errorMsg = rsp.getRetMsg();
            log.warn("❌ 获取交易流水摘要失败 | seqNo={} | msg={}", nSerialNo, errorMsg);
            future.completeExceptionally(new RuntimeException(errorMsg));
        } else {
            try {
                String json = JsonFormat.printer().print(rsp);
                log.info("✅ 成功获取交易流水摘要 | seqNo={} | {}", nSerialNo, json.substring(0, Math.min(json.length(), 100)) + "...");
                future.complete(json);
            } catch (InvalidProtocolBufferException e) {
                log.error("❌ Protobuf 转 JSON 失败", e);
                future.completeExceptionally(e);
            }
        }

        log.info("📊 待处理请求数量 | seqNo={} | 剩余={}", nSerialNo, pendingRequests.size());
    }



    /**
     * 下单（买入或卖出）
     *
     * @param accId       账户 ID，例如 16097643
     * @param trdEnv      交易环境：0=模拟，1=真实
     * @param trdMarket   交易市场：1=港股，2=美股等
     * @param trdSide     买卖方向：1=买入，2=卖出（TrdCommon.TrdSide.TrdSide_Buy_VALUE = 1）
     * @param orderType   订单类型：1=市价，2=限价，3=止损等（常用 2=限价）
     * @param secMarket   证券市场：1=港股，3=美股
     * @param code        股票代码，如 "00700"
     * @param price       委托价格（单位：元）
     * @param qty         委托数量（股数）
     * @return CompletableFuture<String> 返回下单结果 JSON
     */
    public CompletableFuture<String> placeOrder(
            long accId,
            int trdEnv,
            int trdMarket,
            int trdSide,
            int orderType,
            int secMarket,
            String code,
            double price,
            long qty) {

        CompletableFuture<String> future = new CompletableFuture<>();

        // 构建交易头
        TrdCommon.TrdHeader header = TrdCommon.TrdHeader.newBuilder()
                .setAccID(accId)
                .setTrdEnv(trdEnv)
                .setTrdMarket(trdMarket)
                .build();

        // 构建 C2S
        TrdPlaceOrder.C2S c2s = TrdPlaceOrder.C2S.newBuilder()
                .setPacketID(trd.nextPacketID())  // 必须调用，防止重复提交
                .setHeader(header)
                .setTrdSide(trdSide)
                .setOrderType(orderType)
                .setSecMarket(secMarket)
                .setCode(code)
                .setPrice((float) price)
                .setQty(qty)
                .build();

        // 构建 Request
        TrdPlaceOrder.Request req = TrdPlaceOrder.Request.newBuilder().setC2S(c2s).build();

        // 发送请求
        int seqNo = trd.placeOrder(req);
        log.info("🛒 发送下单请求 | seqNo={} | accId={} | {} {}手 @{}港元", seqNo, accId, code, qty, price);

        if (seqNo <= 0) {
            future.completeExceptionally(new RuntimeException("下单请求发送失败: seqNo=" + seqNo));
        } else {
            pendingRequests.put(seqNo, future);
        }

        log.info("📊 待处理请求数量 | seqNo={} | 剩余={}", seqNo, pendingRequests.size());
        return future;
    }

    @Override
    public void onReply_PlaceOrder(FTAPI_Conn client, int nSerialNo, TrdPlaceOrder.Response rsp) {
        CompletableFuture<String> future = pendingRequests.remove(nSerialNo);
        if (future == null) {
            log.warn("⚠️ 收到下单响应但无对应请求 | seqNo={}", nSerialNo);
            return;
        }

        if (rsp.getRetType() != 0) {
            String errorMsg = rsp.getRetMsg();
            log.warn("❌ 下单失败 | seqNo={} | msg={}", nSerialNo, errorMsg);
            future.completeExceptionally(new RuntimeException(errorMsg));
        } else {
            try {
                String json = JsonFormat.printer().print(rsp);
                log.info("✅ 下单成功！| seqNo={} | {}", nSerialNo, json.substring(0, Math.min(json.length(), 100)) + "...");
                future.complete(json);
            } catch (InvalidProtocolBufferException e) {
                log.error("❌ Protobuf 转 JSON 失败", e);
                future.completeExceptionally(e);
            }
        }

        log.info("📊 待处理请求数量 | seqNo={} | 剩余={}", nSerialNo, pendingRequests.size());
    }


    // ===================================================================================
    // 🛠️ 修改订单（撤单 / 改单）
    // ===================================================================================

    /**
     * 修改订单（包括撤单、改价、改量）
     *
     * @param accId           账户 ID
     * @param trdEnv          交易环境：0=模拟，1=真实
     * @param trdMarket       交易市场：1=港股，3=美股
     * @param modifyOrderOp   操作类型：
     *                        0=Normal（改价/改量），1=Cancel（撤单）
     * @param orderId         订单 ID（来自 placeOrder 响应）
     * @param price           新价格（仅改价时使用，撤单可传 0）
     * @param qty             新数量（仅改量时使用，撤单可传 0）
     * @return CompletableFuture<String> 返回结果 JSON
     */
    public CompletableFuture<String> modifyOrder(
            long accId,
            int trdEnv,
            int trdMarket,
            int modifyOrderOp,
            long orderId,
            Double price,
            Long qty) {

        CompletableFuture<String> future = new CompletableFuture<>();

        // 构建交易头
        TrdCommon.TrdHeader header = TrdCommon.TrdHeader.newBuilder()
                .setAccID(accId)
                .setTrdEnv(trdEnv)
                .setTrdMarket(trdMarket)
                .build();

        // 构建 C2S
        TrdModifyOrder.C2S.Builder c2sBuilder = TrdModifyOrder.C2S.newBuilder()
                .setPacketID(trd.nextPacketID())
                .setHeader(header)
                .setOrderID(orderId)
                .setModifyOrderOp(modifyOrderOp);

        // 只有在改价或改量时才设置
        if (price != null && modifyOrderOp == TrdCommon.ModifyOrderOp.ModifyOrderOp_Normal_VALUE) {
            c2sBuilder.setPrice(price.floatValue());
        }
        if (qty != null && qty > 0 && modifyOrderOp == TrdCommon.ModifyOrderOp.ModifyOrderOp_Normal_VALUE) {
            c2sBuilder.setQty(qty);
        }

        TrdModifyOrder.C2S c2s = c2sBuilder.build();
        TrdModifyOrder.Request req = TrdModifyOrder.Request.newBuilder().setC2S(c2s).build();

        // 发送请求
        int seqNo = trd.modifyOrder(req);
        log.info("🛠️ 发送修改订单请求 | seqNo={} | orderId={} | op={} | price={} | qty={}",
                seqNo, orderId, modifyOrderOp, price, qty);

        if (seqNo <= 0) {
            future.completeExceptionally(new RuntimeException("请求发送失败: seqNo=" + seqNo));
        } else {
            pendingRequests.put(seqNo, future);
        }

        log.info("📊 待处理请求数量 | seqNo={} | 剩余={}", seqNo, pendingRequests.size());
        return future;
    }

    @Override
    public void onReply_ModifyOrder(FTAPI_Conn client, int nSerialNo, TrdModifyOrder.Response rsp) {
        CompletableFuture<String> future = pendingRequests.remove(nSerialNo);
        if (future == null) {
            log.warn("⚠️ 收到修改订单响应但无对应请求 | seqNo={}", nSerialNo);
            return;
        }

        if (rsp.getRetType() != 0) {
            String errorMsg = rsp.getRetMsg();
            log.warn("❌ 修改订单失败 | seqNo={} | msg={}", nSerialNo, errorMsg);
            future.completeExceptionally(new RuntimeException(errorMsg));
        } else {
            try {
                String json = JsonFormat.printer().print(rsp);
                log.info("✅ 修改订单成功！| seqNo={} | {}", nSerialNo, json.substring(0, Math.min(json.length(), 100)) + "...");
                future.complete(json);
            } catch (InvalidProtocolBufferException e) {
                log.error("❌ Protobuf 转 JSON 失败", e);
                future.completeExceptionally(e);
            }
        }

        log.info("📊 待处理请求数量 | seqNo={} | 剩余={}", nSerialNo, pendingRequests.size());
    }

    // ===================================================================================
// 📋 查询当前订单列表 (getOrderList)
// ===================================================================================

    /**
     * 查询当前未完成的订单列表（如：已提交、部分成交，未撤单）
     *
     * @param accId     账户 ID，例如 16097643
     * @param trdEnv    交易环境：0=模拟，1=真实
     * @param trdMarket 交易市场：1=港股，2=A股，3=美股
     * @return CompletableFuture<String> 返回 Protobuf 转 JSON 的订单列表
     */
    public CompletableFuture<String> getOrderList(long accId, int trdEnv, int trdMarket) {
        CompletableFuture<String> future = new CompletableFuture<>();

        // 构建交易头
        TrdCommon.TrdHeader header = TrdCommon.TrdHeader.newBuilder()
                .setAccID(accId)
                .setTrdEnv(trdEnv)
                .setTrdMarket(trdMarket)
                .build();

        // 构建 C2S
        TrdGetOrderList.C2S c2s = TrdGetOrderList.C2S.newBuilder()
                .setHeader(header)
                .build();

        // 构建 Request
        TrdGetOrderList.Request req = TrdGetOrderList.Request.newBuilder().setC2S(c2s).build();

        // 发送请求
        int seqNo = trd.getOrderList(req);
        log.info("📋 发送查询订单列表请求 | seqNo={} | accId={} | env={} | market={}", seqNo, accId, trdEnv, trdMarket);

        if (seqNo <= 0) {
            future.completeExceptionally(new RuntimeException("请求发送失败: seqNo=" + seqNo));
        } else {
            pendingRequests.put(seqNo, future);
        }

        log.info("📊 待处理请求数量 | seqNo={} | 剩余={}", seqNo, pendingRequests.size());
        return future;
    }

    @Override
    public void onReply_GetOrderList(FTAPI_Conn client, int nSerialNo, TrdGetOrderList.Response rsp) {
        CompletableFuture<String> future = pendingRequests.remove(nSerialNo);
        if (future == null) {
            log.warn("⚠️ 收到订单列表响应但无对应请求 | seqNo={}", nSerialNo);
            return;
        }

        if (rsp.getRetType() != 0) {
            String errorMsg = rsp.getRetMsg();
            log.warn("❌ 查询订单列表失败 | seqNo={} | msg={}", nSerialNo, errorMsg);
            future.completeExceptionally(new RuntimeException(errorMsg));
        } else {
            try {
                String json = JsonFormat.printer().print(rsp);
                log.info("✅ 成功获取订单列表 | seqNo={} | {}", nSerialNo, json.substring(0, Math.min(json.length(), 100)) + "...");
                future.complete(json);
            } catch (InvalidProtocolBufferException e) {
                log.error("❌ Protobuf 转 JSON 失败", e);
                future.completeExceptionally(e);
            }
        }

        log.info("📊 待处理请求数量 | seqNo={} | 剩余={}", nSerialNo, pendingRequests.size());
    }



    // ===================================================================================
// 📅 查询历史订单列表（仅支持时间范围筛选）
// ===================================================================================

    /**
     * 查询历史订单列表，支持可选的时间范围筛选
     * 若不传时间，则使用服务器默认范围（如最近7天）
     *
     * @param accId     账户 ID
     * @param trdEnv    交易环境：0=模拟，1=真实
     * @param trdMarket 交易市场：1=港股，3=美股
     * @param beginTime 开始时间（可选），格式：yyyy-MM-dd HH:mm:ss
     * @param endTime   结束时间（可选），格式：yyyy-MM-dd HH:mm:ss
     * @return CompletableFuture<String> 返回 JSON 格式的响应
     */
    public CompletableFuture<String> getHistoryOrderList(
            long accId,
            int trdEnv,
            int trdMarket,
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime beginTime,
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {

        CompletableFuture<String> future = new CompletableFuture<>();

        // 构建交易头
        TrdCommon.TrdHeader header = TrdCommon.TrdHeader.newBuilder()
                .setAccID(accId)
                .setTrdEnv(trdEnv)
                .setTrdMarket(trdMarket)
                .build();

        // 构建过滤条件 - 只设置时间
        TrdCommon.TrdFilterConditions.Builder filterBuilder = TrdCommon.TrdFilterConditions.newBuilder();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if (beginTime != null) {
            filterBuilder.setBeginTime(beginTime.format(formatter));
        }
        if (endTime != null) {
            filterBuilder.setEndTime(endTime.format(formatter));
        }

        TrdCommon.TrdFilterConditions filter = filterBuilder.build();

        // 构建 C2S
        TrdGetHistoryOrderList.C2S c2s = TrdGetHistoryOrderList.C2S.newBuilder()
                .setHeader(header)
                .setFilterConditions(filter)
                .build();

        // 构建 Request
        TrdGetHistoryOrderList.Request req = TrdGetHistoryOrderList.Request.newBuilder().setC2S(c2s).build();

        // 发送请求
        int seqNo = trd.getHistoryOrderList(req);
        log.info("📅 发送查询历史订单请求 | seqNo={} | accId={} | env={} | market={} | time={} ~ {}",
                seqNo, accId, trdEnv, trdMarket, beginTime, endTime);

        if (seqNo <= 0) {
            future.completeExceptionally(new RuntimeException("请求发送失败: seqNo=" + seqNo));
        } else {
            pendingRequests.put(seqNo, future);
        }

        return future;
    }


    @Override
    public void onReply_GetHistoryOrderList(FTAPI_Conn client, int nSerialNo, TrdGetHistoryOrderList.Response rsp) {
        CompletableFuture<String> future = pendingRequests.remove(nSerialNo);
        if (future == null) {
            log.warn("⚠️ 收到历史订单响应但无对应请求 | seqNo={}", nSerialNo);
            return;
        }

        if (rsp.getRetType() != 0) {
            String errorMsg = rsp.getRetMsg();
            log.warn("❌ 查询历史订单失败 | seqNo={} | msg={}", nSerialNo, errorMsg);
            future.completeExceptionally(new RuntimeException(errorMsg));
        } else {
            try {
                String json = JsonFormat.printer().print(rsp);
                log.info("✅ 成功获取历史订单 | seqNo={} | {}", nSerialNo, json.substring(0, Math.min(json.length(), 100)) + "...");
                future.complete(json);
            } catch (InvalidProtocolBufferException e) {
                log.error("❌ Protobuf 转 JSON 失败", e);
                future.completeExceptionally(e);
            }
        }

        log.info("📊 待处理请求数量 | seqNo={} | 剩余={}", nSerialNo, pendingRequests.size());
    }


// ===================================================================================
// 🔔 订阅账户推送
// ===================================================================================

    //todo: api有问题
    public void subscribeAccountPush() {
        TrdSubAccPush.C2S c2s = TrdSubAccPush.C2S.newBuilder()
                .addAllAccIDList(Collections.singleton(16097643L)) // 添加所有需监听的账户
                .build();

        TrdSubAccPush.Request req = TrdSubAccPush.Request.newBuilder().setC2S(c2s).build();

        int seqNo = trd.subAccPush(req);
        if (seqNo > 0) {
            log.info("📤 已发送账户推送订阅请求 | seqNo={}", seqNo);
        } else {
            log.warn("⚠️ 账户推送订阅请求发送失败 | seqNo={}", seqNo);
        }
    }

    //todo: api有问题
    @Override
    public void onPush_UpdateOrder(FTAPI_Conn client, TrdUpdateOrder.Response rsp) {
        if (rsp.getRetType() != 0) {
            log.warn("🔔 订单更新推送失败 | msg={}", rsp.getRetMsg());
            return;
        }

        try {
            String json = JsonFormat.printer().print(rsp);
            log.info("🔔 收到订单更新推送 | {}", json.substring(0, Math.min(json.length(), 150)) + "...");
            // TODO: 可在此处触发事件、通知前端或更新本地缓存
        } catch (InvalidProtocolBufferException e) {
            log.error("❌ Protobuf 转 JSON 失败（订单更新推送）", e);
        }
    }

    //todo: api有问题
    @Override
    public void onReply_SubAccPush(FTAPI_Conn client, int nSerialNo, TrdSubAccPush.Response rsp) {
        CompletableFuture<String> future = pendingRequests.remove(nSerialNo);
        if (future == null) {
            log.warn("⚠️ 收到交易推送但无对应请求 | seqNo={}", nSerialNo);
            return;
        }

        if (rsp.getRetType() != 0) {
            String errorMsg = rsp.getRetMsg();
            log.warn("❌ 交易推送失败 | seqNo={} | msg={}", nSerialNo, errorMsg);
            future.completeExceptionally(new RuntimeException(errorMsg));
        } else {
            try {
                String json = JsonFormat.printer().print(rsp);
                log.info("✅ 成功获取交易推送 | seqNo={} | {}", nSerialNo, json.substring(0, Math.min(json.length(), 100)) + "...");
                future.complete(json);
            } catch (InvalidProtocolBufferException e) {
                log.error("❌ Protobuf 转 JSON 失败", e);
                future.completeExceptionally(e);
            }
        }
    }

    // ========================================
    // 🚨 实时成交推送回调
    // ========================================
    @Override
    public void onPush_UpdateOrderFill(FTAPI_Conn client, TrdUpdateOrderFill.Response rsp) {
        if (rsp.getRetType() != 0) {
            log.warn("❌ 成交推送失败: {}", rsp.getRetMsg());
            return;
        }

        try {
            String json = JsonFormat.printer().print(rsp);
            log.info("🔔 收到实时成交推送: {}", json);

            // 🔔 可在此添加额外处理：如存库、发 WebSocket、发消息队列等
            // handleOrderFillPush(rsp.getS2C().getFillListList());

        } catch (InvalidProtocolBufferException e) {
            log.error("❌ Protobuf 转 JSON 失败", e);
        }
    }


    // ===================================================================================
    // 获取成交列表 (GetOrderFillList)
    // ===================================================================================

    /**
     * 提供给 Controller 调用的方法：获取成交列表
     *
     * @param accId   账户 ID
     * @param trdEnv  交易环境：0-模拟，1-真实
     * @param market  交易市场：如 TrdCommon.TrdMarket.TrdMarket_HK_VALUE
     * @return CompletableFuture<String> 返回 JSON 格式的成交列表
     */
    public CompletableFuture<String> getOrderFillList(long accId, int trdEnv, int market) {
        CompletableFuture<String> future = new CompletableFuture<>();

        TrdCommon.TrdHeader header = TrdCommon.TrdHeader.newBuilder()
                .setAccID(accId)
                .setTrdEnv(trdEnv)
                .setTrdMarket(market)
                .build();

        TrdGetOrderFillList.C2S c2s = TrdGetOrderFillList.C2S.newBuilder()
                .setHeader(header)
                .build();

        TrdGetOrderFillList.Request req = TrdGetOrderFillList.Request.newBuilder().setC2S(c2s).build();

        int seqNo = trd.getOrderFillList(req);
        log.info("📤 发送 getOrderFillList 请求 | seqNo={} | accId={} | trdEnv={} | market={}", seqNo, accId, trdEnv, market);

        if (seqNo <= 0) {
            future.completeExceptionally(new RuntimeException("发送请求失败: seqNo=" + seqNo));
        } else {
            pendingRequests.put(seqNo, future);
        }

        log.info("📊 待处理请求数量 | seqNo={} | 剩余={}", seqNo, pendingRequests.size());
        return future;
    }

    @Override
    public void onReply_GetOrderFillList(FTAPI_Conn client, int nSerialNo, TrdGetOrderFillList.Response rsp) {
        CompletableFuture<String> future = pendingRequests.remove(nSerialNo);

        if (future == null) {
            log.warn("⚠️ 收到成交列表响应但无对应请求 | seqNo={}", nSerialNo);
            return;
        }

        if (rsp.getRetType() != 0) {
            String errorMsg = rsp.getRetMsg();
            log.warn("❌ 获取成交列表失败 | seqNo={} | msg={}", nSerialNo, errorMsg);
            future.completeExceptionally(new RuntimeException(errorMsg));
        } else {
            try {
                String json = JsonFormat.printer().print(rsp);
                log.info("✅ 成功获取成交列表 | seqNo={} | {}", nSerialNo, json.substring(0, Math.min(json.length(), 100)) + "...");
                future.complete(json);
            } catch (InvalidProtocolBufferException e) {
                log.error("❌ Protobuf 转 JSON 失败", e);
                future.completeExceptionally(e);
            }
        }

        log.info("📊 待处理请求数量 | seqNo={} | 剩余={}", nSerialNo, pendingRequests.size());
    }


    // 方法：获取历史成交列表
    public CompletableFuture<String> getHistoryOrderFillList(long accId, int trdEnv, int market, String beginTime, String endTime) {
        CompletableFuture<String> future = new CompletableFuture<>();

        TrdCommon.TrdHeader header = TrdCommon.TrdHeader.newBuilder()
                .setAccID(accId)
                .setTrdEnv(trdEnv)  // 0: 模拟, 1: 真实
                .setTrdMarket(market)
                .build();

        TrdCommon.TrdFilterConditions filter = TrdCommon.TrdFilterConditions.newBuilder()
                .setBeginTime(beginTime)  // 格式: "2025-09-01 00:00:00"
                .setEndTime(endTime)
                .build();

        TrdGetHistoryOrderFillList.C2S c2s = TrdGetHistoryOrderFillList.C2S.newBuilder()
                .setHeader(header)
                .setFilterConditions(filter)
                .build();

        TrdGetHistoryOrderFillList.Request req = TrdGetHistoryOrderFillList.Request.newBuilder().setC2S(c2s).build();

        int seqNo = trd.getHistoryOrderFillList(req);
        log.info("📤 发送 getHistoryOrderFillList 请求 | seqNo={} | accId={} | env={} | market={} | range=[{} ~ {}]",
                seqNo, accId, trdEnv, market, beginTime, endTime);

        if (seqNo <= 0) {
            future.completeExceptionally(new RuntimeException("发送请求失败: seqNo=" + seqNo));
        } else {
            pendingRequests.put(seqNo, future);
        }

        log.info("📊 待处理请求数量 | seqNo={} | 剩余={}", seqNo, pendingRequests.size());
        return future;
    }

    // 回调处理
    @Override
    public void onReply_GetHistoryOrderFillList(FTAPI_Conn client, int nSerialNo, TrdGetHistoryOrderFillList.Response rsp) {
        CompletableFuture<String> future = pendingRequests.remove(nSerialNo);

        if (future == null) {
            log.warn("⚠️ 收到历史成交响应但无对应请求 | seqNo={}", nSerialNo);
            return;
        }

        if (rsp.getRetType() != 0) {
            String errorMsg = rsp.getRetMsg();
            log.warn("❌ 获取历史成交失败 | seqNo={} | msg={}", nSerialNo, errorMsg);
            future.completeExceptionally(new RuntimeException(errorMsg));
        } else {
            try {
                String json = JsonFormat.printer().print(rsp);
                log.info("✅ 成功获取历史成交列表 | seqNo={} | {}", nSerialNo, json.substring(0, Math.min(json.length(), 100)) + "...");
                future.complete(json);
            } catch (InvalidProtocolBufferException e) {
                log.error("❌ Protobuf 转 JSON 失败", e);
                future.completeExceptionally(e);
            }
        }

        log.info("📊 待处理请求数量 | seqNo={} | 剩余={}", nSerialNo, pendingRequests.size());
    }

}
