package com.xingqiao.order.rocketmq.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 行情消息通知实体类
 * 用于在RocketMQ中传递订单信息
 *
 * @author xingqiao
 * @date 2025-09-22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuoteMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;

    private String sessionId;

    private String dataType;
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 备注信息
     */
    private String remark;
}
