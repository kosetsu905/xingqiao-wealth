package com.xingqiao.system.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xingqiao.common.core.annotation.Excel;
import lombok.Data;

import java.util.Date;

@Data
public class AuditInfoResp {
    /** 主键ID */
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 用户类型（00系统用户,01:合作商用户,02:客户） */
    private String userType;

    /** 业务主表ID */
    private Long businessId;

    /** 业务类型 */
    private String businessType;

    /** 审核人员ID */
    private Long auditorId;

    /** 审核人员姓名 */
    private String auditorName;

    /** 审核状态（0-待审核, 1-审核通过, 2-审核拒绝） */
    private Integer auditStatus;

    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date auditTime;

    //审核备注
    private String remark;


}
