package com.xingqiao.system.api.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xingqiao.common.core.annotation.Excel;
import com.xingqiao.common.core.web.domain.BaseEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;


@Data
public class AuthAgencyEkyc {
    private static final long serialVersionUID = 1L;

    private Long id; // 主键ID

    //代理人id
    private Long userId;


    private String userType;

    /** 审核备注 */
    private String remark;

    private Integer auditStatus;

    private Date auditTime;
    /** 审核人员ID */
    private Long auditorId;
    /** 审核人员姓名 */
    private String auditorName;


}

