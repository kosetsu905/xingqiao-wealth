
-- 代理端

-- ----------------------------
-- 客户信息表
-- ----------------------------
DROP TABLE IF EXISTS sys_customer_info;
CREATE TABLE sys_customer_info (
                                   id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                   user_id           bigint(20)          comment '客户ID',
                                   user_temp_id          varchar(20)      not null     comment '客户临时ID',
                                   employee_id           bigint(20)      not null     comment '业务员ID',
                                   user_type       varchar(2)   default '00' null comment '用户类型（00系统用户,01:合作商用户,02:客户）',
                                   id_type VARCHAR(20) default NULL COMMENT '证件类型（枚举：passport-护照, id_card-身份证, driver_license-驾驶证, other-其他）',
                                   id_number VARCHAR(100) default NULL COMMENT '证件号码',
                                   issue_date DATE COMMENT '证件颁发日期',
                                   expiry_date DATE COMMENT '证件有效期截止日期',
                                   avatar VARCHAR(500) COMMENT '头像 URL',
                                   front_id_file_url VARCHAR(500) COMMENT '证件正面照URL',
                                   back_id_file_url VARCHAR(500) COMMENT '证件反面照URL',
                                   identity_confirmed TINYINT(1) DEFAULT 0 COMMENT '身份是否确认（0-未确认，1-已确认）',
                                   full_name VARCHAR(100) COMMENT '全名（真实姓名）',
                                   age VARCHAR(10) COMMENT '年龄（字符串，兼容非数字输入）',
                                   phone_number VARCHAR(20) COMMENT '手机号码',
                                   email VARCHAR(100) COMMENT '电子邮箱',
                                   manager VARCHAR(100) COMMENT '上级经理ID（或账号）',
                                   address VARCHAR(500) COMMENT '联系地址',
                                   avatar_url VARCHAR(500) COMMENT '个人头像URL',
                                   country_code VARCHAR(10) COMMENT '手机区号',
                                   gender CHAR(1) COMMENT '性别（0-未知, 1-男, 2-女）',
                                   marital_status VARCHAR(20) COMMENT '婚姻状况：未婚：SINGLE, 已婚：MARRIED, 离异:DIVORCED, 丧偶:WIDOWED, 其他：OTHER',
                                   birth_day DATE COMMENT '出生年月日',
                                   child_count TINYINT UNSIGNED DEFAULT 0   COMMENT '子女数量',
                                   status            char(1)         default '0'                comment '部门状态（0正常 1停用）',
                                   remark VARCHAR(500) COMMENT '备注',
                                   create_by         varchar(64)     default ''                 comment '创建者',
                                   create_time 	    datetime                                   comment '创建时间',
                                   update_by         varchar(64)     default ''                 comment '更新者',
                                   update_time       datetime                                   comment '更新时间',
                                   PRIMARY KEY (id),
                                   KEY idx_user_id (user_id),
                                   KEY idx_user_temp_id (user_temp_id),
                                   KEY idx_employee_id (employee_id),
                                   KEY idx_id_number (id_number),
                                   KEY idx_full_name (full_name),
                                   KEY idx_create_time (create_time)

) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='客户信息表';



DROP TABLE IF EXISTS sys_customer_financial;
CREATE TABLE sys_customer_financial (
                                        id INT AUTO_INCREMENT PRIMARY KEY,
                                        user_id           bigint(20)          comment '客户ID',
                                        user_temp_id          varchar(20)      not null     comment '客户临时ID',
                                        family_total_asset DECIMAL(15,2) COMMENT '家庭总资产(万元)',
                                        family_debt DECIMAL(15,2) COMMENT '家庭负债(万元)',
                                        family_annual_income DECIMAL(15,2) COMMENT '家庭年收入(万元)',
                                        new_investment_amount DECIMAL(15,2) COMMENT '新投资额度(万元)',
    -- 基础字段（继承自BaseEntity）
                                        status            char(1)         default '0'                comment '状态（0正常 1停用）',
                                        remark VARCHAR(500) COMMENT '备注',
                                        create_by         varchar(64)     default ''                 comment '创建者',
                                        create_time 	    datetime                                   comment '创建时间',
                                        update_by         varchar(64)     default ''                 comment '更新者',
                                        update_time       datetime   comment '更新时间',
                                        KEY idx_user_temp_id (user_temp_id),
                                        KEY idx_user_id (user_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='客户财务信息表';




DROP TABLE IF EXISTS sys_customer_investment_preference;
CREATE TABLE sys_customer_investment_preference (
                                                    id INT AUTO_INCREMENT PRIMARY KEY,
                                                    user_id           bigint(20)          comment '客户ID',
                                                    user_temp_id          varchar(20)      not null     comment '客户临时ID',
                                                    product_type VARCHAR(20) COMMENT '产品类型',
                                                    status            char(1)         default '0'                comment '状态（0正常 1停用）',
                                                    remark VARCHAR(500) COMMENT '备注',
                                                    create_by         varchar(64)     default ''                 comment '创建者',
                                                    create_time 	    datetime                                   comment '创建时间',
                                                    update_by         varchar(64)     default ''                 comment '更新者',
                                                    update_time       datetime   comment '更新时间',
                                                    UNIQUE KEY uk_customer_product (user_temp_id, product_type),
                                                    KEY idx_user_id (user_id),
                                                    KEY idx_user_temp_id (user_temp_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='客户投资产品表';




DROP TABLE IF EXISTS sys_address;
-- 系统地址簿表
CREATE TABLE sys_address (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             user_id           bigint(20)          comment '客户ID',
                             user_temp_id          varchar(30)      not null     comment '客户临时ID',
                             recipient_name VARCHAR(50)  COMMENT '收件人姓名',
                             recipient_phone VARCHAR(20)  COMMENT '收件人电话',
                             country VARCHAR(50) DEFAULT '中国' COMMENT '国家',
                             province VARCHAR(50)  COMMENT '省',
                             city VARCHAR(50)  COMMENT '市',
                             district VARCHAR(50) COMMENT '区',
                             detail_address VARCHAR(500)  COMMENT '详细地址',
                             address_type TINYINT(1) DEFAULT 0 COMMENT '地址类型：0：住址,:1：公司, 2：收件地址,:3：身份证地址',
                             is_default TINYINT(1) DEFAULT 0 COMMENT '是否默认地址：0-否，1-是',
                             zip_code VARCHAR(20) COMMENT '邮政编码',
                             address_label VARCHAR(100) COMMENT '地址别名，如“公司”、“家里”',
                             is_deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除：0-否，1-是',
                             status            char(1)         default '0'                comment '状态（0正常 1停用）',
                             remark VARCHAR(500) COMMENT '备注',
                             create_by         varchar(64)     default ''                 comment '创建者',
                             create_time 	    datetime                                   comment '创建时间',
                             update_by         varchar(64)     default ''                 comment '更新者',
                             update_time       datetime    comment '更新时间',
                             KEY idx_user_id (user_id),
                             KEY idx_user_temp_id (user_temp_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='系统地址簿表';




DROP TABLE IF EXISTS sales_opportunity;

CREATE TABLE sales_opportunity (
                                   id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
                                   user_id           bigint(20)          comment '客户ID',
                                   user_temp_id          varchar(30)      not null     comment '客户临时ID',
                                   employee_id           bigint(20)      not null     comment '业务员ID',
                                   full_name VARCHAR(100)  COMMENT '客户姓名（必填）',
                                   phoneNumber VARCHAR(100)  COMMENT '联系手机号',
                                   email VARCHAR(100)  COMMENT '联系邮箱',
                                   investment_amount DECIMAL(15,2)  DEFAULT 0.00 COMMENT '投资金额（必填，单位：元）',
                                   investment_time_intent VARCHAR(50)  COMMENT '投资时间意向（如：1个月内、半年后等）（必填）',
                                   interested_products VARCHAR(255) COMMENT '感兴趣的金融产品，如：基金投资,股票投资 或 fund,stock',
                                   remark VARCHAR(500) COMMENT '备注信息',
                                   status CHAR(1) DEFAULT '0' COMMENT '状态：0-待跟进，1-已跟进，2-已成交，3-已关闭',
                                   create_by VARCHAR(64) DEFAULT '' COMMENT '创建人',
                                   create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   update_by VARCHAR(64) DEFAULT '' COMMENT '更新人',
                                   update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                   INDEX idx_user_id (user_id),
                                   INDEX idx_user_temp_id (user_temp_id),
                                   INDEX idx_full_name (full_name),
                                   INDEX idx_create_time (create_time),
                                   INDEX idx_status (status)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='客户意向记录表';








-- auto-generated definition

drop table if exists customer_info;

create table customer_info
(
    id                 bigint auto_increment comment '主键ID'
        primary key,
    user_id            bigint                        null comment '客户ID',
    user_temp_id       varchar(20)                   not null comment '客户临时ID',
    employee_id        bigint                        not null comment '业务员ID',
    user_type          varchar(2)       default '00' null comment '用户类型（00系统用户,01:合作商用户,02:客户）',
    id_type            varchar(20)                   null comment '证件类型（枚举：passport-护照, id_card-身份证, driver_license-驾驶证, other-其他）',
    id_number          varchar(100)                  null comment '证件号码',
    issue_date         date                          null comment '证件颁发日期',
    expiry_date        date                          null comment '证件有效期截止日期',
    avatar             varchar(500)                  null comment '头像 URL',
    front_id_file_url  varchar(500)                  null comment '证件正面照URL',
    back_id_file_url   varchar(500)                  null comment '证件反面照URL',
    full_name          varchar(100)                  null comment '全名（真实姓名）',
    age                varchar(10)                   null comment '年龄（字符串，兼容非数字输入）',
    phone_number       varchar(20)                   null comment '手机号码',
    email              varchar(100)                  null comment '电子邮箱',
    manager            varchar(100)                  null comment '上级经理ID（或账号）',
    address            varchar(500)                  null comment '联系地址',
    avatar_url         varchar(500)                  null comment '个人头像URL',
    country_code       varchar(10)                   null comment '手机区号',
    gender             char                          null comment '性别（0-未知, 1-男, 2-女）',
    marital_status     varchar(20)                   null comment '婚姻状况：未婚：SINGLE, 已婚：MARRIED, 离异:DIVORCED, 丧偶:WIDOWED, 其他：OTHER',
    birth_day          date                          null comment '出生年月日',
    child_count        tinyint unsigned default '0'  null comment '子女数量',
    certify_id         varchar(200)           null comment '三方id',
    face_verify_status tinyint          default 0    null comment '人脸验证状态:0-未验证,1-验证中,2-验证成功,3-验证失败',
    face_verify_time   datetime                      null comment '人脸验证时间',
    face_verify_score  decimal(5, 2)                 null comment '人脸比对分数',
    face_image_url     varchar(255)                  null comment '人脸照片URL',
    status             char(2)          default '0'  null comment '审核状态:0-待审核,1-审核通过,2-审核拒绝',
    review_time        datetime                      null comment '审核时间',
    review_operator    varchar(64)                   null comment '审核操作员',
    review_comment     varchar(500)                  null comment '审核意见',
    remark             varchar(500)                  null comment '备注',
    create_by          varchar(64)      default ''   null comment '创建者',
    create_time        datetime                      null comment '创建时间',
    update_by          varchar(64)      default ''   null comment '更新者',
    update_time        datetime                      null comment '更新时间'
)
    comment '客户信息表' charset = utf8mb4;

create index idx_customer_info_create_time
    on customer_info (create_time);

create index idx_customer_info_employee_id
    on customer_info (employee_id);

create index idx_customer_info_full_name
    on customer_info (full_name);

create index idx_customer_info_id_number
    on customer_info (id_number);

create index idx_customer_info_user_id
    on customer_info (user_id);

create index idx_customer_info_user_temp_id
    on customer_info (user_temp_id);



