-- ----------------------------
-- 客户表结构（包含索引）
-- ----------------------------
DROP TABLE IF EXISTS c_user;
CREATE TABLE c_user (
                              user_id          BIGINT(20)     NOT NULL AUTO_INCREMENT    COMMENT '用户ID',
                              user_name         VARCHAR(30)     NOT NULL                   COMMENT '用户账号',
                              nick_name         VARCHAR(30)     NOT NULL                   COMMENT '用户昵称',
                              user_type         VARCHAR(2)      DEFAULT '01'               COMMENT '用户类型（00系统用户，01客户，02业务员）',
                              email             VARCHAR(50)     DEFAULT ''                COMMENT '用户邮箱',
                              phone_number      VARCHAR(20)     DEFAULT ''                COMMENT '手机号码',
                              sex               CHAR(1)         DEFAULT '0'               COMMENT '用户性别（0男 1女 2未知）',
                              avatar            VARCHAR(100)    DEFAULT ''                 COMMENT '头像地址',
                              password          VARCHAR(100)    DEFAULT ''                 COMMENT '密码',
                              status            CHAR(1)         DEFAULT '0'               COMMENT '账号状态（0正常 1停用）',
                              del_flag          CHAR(1)         DEFAULT '0'               COMMENT '删除标志（0代表存在 2代表删除）',
                              login_ip          VARCHAR(128)    DEFAULT ''                 COMMENT '最后登录IP',
                              login_date        DATETIME                                   COMMENT '最后登录时间',
                              pwd_update_date   DATETIME                                   COMMENT '密码最后更新时间',
                              create_by         VARCHAR(64)     DEFAULT ''                 COMMENT '创建者',
                              create_time       DATETIME                                   COMMENT '创建时间',
                              update_by         VARCHAR(64)     DEFAULT ''                 COMMENT '更新者',
                              update_time       DATETIME                                   COMMENT '更新时间',
                              remark            VARCHAR(500)    DEFAULT NULL              COMMENT '备注',
                              PRIMARY KEY (user_id),
                              UNIQUE INDEX idx_user_phone (phone_number),
                              INDEX idx_user_username (user_name),
                              UNIQUE INDEX idx_user_email (email),
                              INDEX idx_user_status_type (status, user_type),
                              INDEX idx_user_create_time_status (create_time, status)
) ENGINE=INNODB AUTO_INCREMENT=1 COMMENT='客户信息表';


#新增手机国家区号 String countryCode字段
ALTER TABLE c_user ADD COLUMN country_code VARCHAR(10) COMMENT '手机国家区号';

ALTER TABLE c_user MODIFY user_name VARCHAR(50) NULL;


ALTER TABLE c_user MODIFY nick_name VARCHAR(50) NULL;


ALTER TABLE c_user ADD COLUMN account VARCHAR(50) COMMENT '登录账号';
#修改sys_user 表的phonenumber为phone_number
ALTER TABLE sys_user CHANGE COLUMN phonenumber phone_number VARCHAR(20) COMMENT '手机号码';