package com.xingqiao.api.trade.domain;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

@Data
public class SubscribeRequest {
    private String sessionId;
    // 用户ID
    private Long userId;
    private String dataType;
    private JSONObject msgObj;
}
