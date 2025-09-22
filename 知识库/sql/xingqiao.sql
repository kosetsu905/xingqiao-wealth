

#新增手机国家区号 String countryCode字段
ALTER TABLE sys_user ADD COLUMN country_code VARCHAR(10) COMMENT '手机国家区号';

ALTER TABLE sys_user MODIFY user_name VARCHAR(50) NULL;


ALTER TABLE sys_user MODIFY nick_name VARCHAR(50) NULL;

