ALTER TABLE customer_accounts
    ADD COLUMN account_type TINYINT DEFAULT 1 NOT NULL COMMENT '账户类型: 1-实盘账户, 2-虚拟账户';
ALTER TABLE customer_accounts
    ADD COLUMN account_name VARCHAR(100) NULL COMMENT '账户名称';
