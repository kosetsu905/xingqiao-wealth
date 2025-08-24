-- ----------------------------
-- 员工信息表
-- ----------------------------
DROP TABLE IF EXISTS sys_employee_info;
CREATE TABLE sys_employee_info (
    -- 主键ID
    id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    user_id           bigint(20)      not null     comment '用户ID',
    user_type       varchar(2)   default '00' null comment '用户类型（00系统用户,01:合作商用户,02:客户）',
    -- 证件信息
    id_type VARCHAR(20) NOT NULL COMMENT '证件类型（枚举：passport-护照, id_card-身份证, driver_license-驾驶证, other-其他）',
    id_number VARCHAR(100) NOT NULL COMMENT '证件号码',
    issue_date DATE COMMENT '证件颁发日期',
    expiry_date DATE COMMENT '证件有效期截止日期',
    avatar VARCHAR(500) COMMENT '头像 URL',
    front_id_file_url VARCHAR(500) COMMENT '证件正面照URL',
    back_id_file_url VARCHAR(500) COMMENT '证件反面照URL',
    identity_confirmed TINYINT(1) DEFAULT 0 COMMENT '身份是否确认（0-未确认，1-已确认）',

    -- 专业资质信息
    professional_experience TEXT COMMENT '专业经验描述',
    
    -- 个人信息
    account_name VARCHAR(100) COMMENT '账户名称（登录用）',
    full_name VARCHAR(100) NOT NULL COMMENT '全名（真实姓名）',
    age VARCHAR(10) COMMENT '年龄（字符串，兼容非数字输入）',
    phone_number VARCHAR(20) COMMENT '手机号码',
    email VARCHAR(100) COMMENT '电子邮箱',
    manager VARCHAR(100) COMMENT '上级经理ID（或账号）',
    address VARCHAR(500) COMMENT '联系地址',
    avatar_url VARCHAR(500) COMMENT '个人头像URL',
    country_code VARCHAR(10) COMMENT '手机区号',
    gender CHAR(1) COMMENT '性别（0-未知, 1-男, 2-女）',
    birthday DATE COMMENT '出生年月日',
    -- 基础字段（继承自BaseEntity）
    status            char(1)         default '0'                comment '部门状态（0正常 1停用）',
    remark VARCHAR(500) COMMENT '备注',
    create_by         varchar(64)     default ''                 comment '创建者',
    create_time 	    datetime                                   comment '创建时间',
    update_by         varchar(64)     default ''                 comment '更新者',
    update_time       datetime                                   comment '更新时间',
    
    -- 主键约束
    PRIMARY KEY (id),
    
    -- 索引
    KEY idx_id_number (id_number),
    KEY idx_user_id (user_id),
    KEY idx_account_name (account_name),
    KEY idx_full_name (full_name),
    KEY idx_create_time (create_time)
) ENGINE=InnoDB AUTO_INCREMENT=1000 COMMENT='员工信息表';

-- ----------------------------
-- 员工资质认证表
-- ----------------------------

DROP TABLE IF EXISTS sys_employee_qualifications;
CREATE TABLE sys_employee_qualifications (
    -- 主键ID
    id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    user_id           bigint(20)      not null     comment '用户ID',
    user_type       varchar(2)   default '00' null comment '用户类型（00系统用户,01:合作商用户,02:客户）',
    employee_info_id BIGINT(20) NOT NULL COMMENT '员工信息表表ID',
    -- 专业资质信息
    qualification_type VARCHAR(50) COMMENT '资质类型（枚举：cfp-金融理财师(CFP), cfa-特许金融分析师(CFA), cpa-注册会计师(CPA), other-其他资质）',
    qualification_name VARCHAR(50) COMMENT '资质类型名称',
    certificate_number VARCHAR(100) COMMENT '资质证书编号',
    issuing_authority VARCHAR(200) COMMENT '颁发机构',
    certificate_issue_date DATE COMMENT '证书颁发日期',
    certificate_expiry_date DATE COMMENT '证书有效期截止日期',
    years_of_practice VARCHAR(20) COMMENT '执业年限（字符串，兼容非数字输入）',
    professional_confirmed TINYINT(1) DEFAULT 0 COMMENT '资质是否确认（0-未确认，1-已确认）',

    -- 基础字段（继承自BaseEntity）
    status            char(1)         default '0'                comment '部门状态（0正常 1停用）',
    remark VARCHAR(500) COMMENT '备注',
    create_by         varchar(64)     default ''                 comment '创建者',
    create_time 	    datetime                                   comment '创建时间',
    update_by         varchar(64)     default ''                 comment '更新者',
    update_time       datetime                                   comment '更新时间',

    -- 主键约束
    PRIMARY KEY (id),

    -- 索引
    KEY idx_user_id (user_id),
    KEY idx_create_time (create_time)
) ENGINE=InnoDB AUTO_INCREMENT=1000 COMMENT='员工资质认证表';





-- ----------------------------
-- 文件存储表
-- ----------------------------
DROP TABLE IF EXISTS sys_file;
CREATE TABLE sys_file (
    id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    user_id           bigint(20)      not null     comment '用户ID',
    business_id BIGINT(20) NOT NULL COMMENT '业务主表ID',
    business_type VARCHAR(20) NOT NULL COMMENT '业务类型',
    file_type VARCHAR(20) NOT NULL COMMENT '文件类型',
    file_url VARCHAR(500) NOT NULL COMMENT '文件URL',
    sort_order INT(4) DEFAULT 0 COMMENT '排序',
    remark VARCHAR(500) COMMENT '备注',
    create_by         varchar(64)     default ''                 comment '创建者',
    create_time 	    datetime                                   comment '创建时间',
    update_by         varchar(64)     default ''                 comment '更新者',
    update_time       datetime                                   comment '更新时间',
    PRIMARY KEY (id),
    KEY idx_user_id_type (user_id,business_type,file_type),
    KEY idx_business_id (business_id)
) ENGINE=InnoDB COMMENT='系统文件管理';



-- ----------------------------
-- 系统审核审核记录表
-- ----------------------------
DROP TABLE IF EXISTS sys_audit;
CREATE TABLE sys_audit (
    id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT(20) NOT NULL COMMENT '用户ID',
    user_type       varchar(2)   default '00' null comment '用户类型（00系统用户,01:合作商用户,02:客户）',
    business_id BIGINT(20) NOT NULL COMMENT '业务主表ID',
    business_type VARCHAR(20) NOT NULL COMMENT '业务类型',
    auditor_id BIGINT(20) NOT NULL COMMENT '审核人员ID',
    auditor_name VARCHAR(100) NOT NULL COMMENT '审核人员姓名',
    audit_status TINYINT(1) NOT NULL COMMENT '审核状态（0-待审核, 1-审核通过, 2-审核拒绝）',
    audit_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '审核时间',

    -- 基础字段（继承自BaseEntity）
    remark VARCHAR(500) COMMENT '备注',
    create_by         varchar(64)     default ''                 comment '创建者',
    create_time 	    datetime                                   comment '创建时间',
    update_by         varchar(64)     default ''                 comment '更新者',
    update_time       datetime                                   comment '更新时间',
    
    PRIMARY KEY (id),
    KEY idx_user_id (user_id),
    KEY idx_business_id (business_id),
    KEY idx_auditor_id (auditor_id),
    KEY idx_audit_time (audit_time)
) ENGINE=InnoDB COMMENT='系统审核记录表';



