package com.xingqiao.system.api.domain;

import com.xingqiao.common.core.annotation.Excel;
import lombok.Data;

@Data
public class CustomerInvestmentPreference {
    /** 客户ID */
    @Excel(name = "客户ID")
    private Long userId;

    /** 客户临时ID */
    @Excel(name = "客户临时ID")
    private String userTempId;

    /** 产品类型 */
    @Excel(name = "产品类型")
    private String productType;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;
}
