

DROP DATABASE IF EXISTS `xingqiao-trade`;
CREATE DATABASE  `xingqiao-trade` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

use `xingqiao-trade`;
-- 客户资金账户表
DROP TABLE IF EXISTS customer_accounts;
CREATE TABLE customer_accounts (
    id    bigint auto_increment comment '主键ID' primary key,
    user_id   bigint    null comment '客户ID',
    currency CHAR(10) NOT NULL COMMENT '币种 (如 CNY, USD)',
    total_balance DECIMAL(18,6) NOT NULL DEFAULT 0.000000 COMMENT '总资金',
    available_balance DECIMAL(18,6) NOT NULL DEFAULT 0.000000 COMMENT '可用资金',
    frozen_balance DECIMAL(18,6) NOT NULL DEFAULT 0.000000 COMMENT '冻结资金',
    withdrawable_balance DECIMAL(18,6) NOT NULL DEFAULT 0.000000 COMMENT '可取资金',
    credit_balance DECIMAL(18,6) NOT NULL DEFAULT 0.000000 COMMENT '信用资金(融资融券)',
    version INT NOT NULL DEFAULT 0 COMMENT '版本号(用于乐观锁)',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 1-正常, 2-冻结, 3-销户',
    remark                     varchar(500)                  null comment '备注',
    create_by                  varchar(64)      default ''   null comment '创建者',
    create_time                datetime                      null comment '创建时间',
    update_by                  varchar(64)      default ''   null comment '更新者',
    update_time                datetime                      null comment '更新时间',
    -- 索引优化
    UNIQUE KEY uk_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='客户资金账户表';


-- 证券信息表
DROP TABLE IF EXISTS securities;
CREATE TABLE securities (
                            id BIGINT UNSIGNED AUTO_INCREMENT COMMENT '主键ID' PRIMARY KEY,
                            security_code VARCHAR(20) NOT NULL COMMENT '证券代码 (如 600519)',
                            security_name VARCHAR(100) NOT NULL COMMENT '证券名称 (如 贵州茅台)',
                            exchange_code VARCHAR(10) NOT NULL COMMENT '交易所代码 (如 SSE, SZSE)',
                            security_type TINYINT NOT NULL COMMENT '证券类型: 1-股票, 2-基金, 3-债券, 4-期货, 5-期权,6-数字货币',
                            currency CHAR(3) NOT NULL COMMENT '交易币种',
                            lot_size INT NOT NULL DEFAULT 100 COMMENT '交易单位 (每手股数)',
                            tick_size DECIMAL(10,6) NOT NULL COMMENT '最小变动价位',
                            price_limit DECIMAL(5,2) NULL COMMENT '涨跌幅限制(%)',
                            is_margin TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否支持融资融券: 0-否, 1-是',
                            status TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 1-交易, 2-停牌, 3-退市',
                            listed_date DATE NULL COMMENT '上市日期',
                            delisted_date DATE NULL COMMENT '退市日期',
                            remark VARCHAR(500) NULL COMMENT '备注',
                            create_by VARCHAR(64) NOT NULL DEFAULT '' COMMENT '创建者',
                            create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            update_by VARCHAR(64) NOT NULL DEFAULT '' COMMENT '更新者',
                            update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                             -- 业务主键: 代码
                            UNIQUE KEY uk_security_exchange (security_code),
                             -- 索引优化
                            INDEX idx_security_code (security_code),
                            INDEX idx_exchange_code (exchange_code),
                            INDEX idx_security_type (security_type),
                            INDEX idx_status (status),
                            INDEX idx_is_margin (is_margin)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='证券信息表';



-- 客户持仓表
DROP TABLE IF EXISTS customer_positions;
CREATE TABLE customer_positions (
                                    id BIGINT UNSIGNED AUTO_INCREMENT COMMENT '主键ID' PRIMARY KEY,
                                    user_id BIGINT UNSIGNED NOT NULL COMMENT '客户ID',
                                    account_id BIGINT UNSIGNED NOT NULL COMMENT '资金账户ID',
                                    security_id BIGINT UNSIGNED NOT NULL COMMENT '证券ID',
                                    position_type TINYINT NOT NULL DEFAULT 1 COMMENT '持仓类型: 1-普通, 2-融资, 3-融券',
                                    quantity DECIMAL(18,6) NOT NULL DEFAULT 0.000000 COMMENT '总持仓数量',
                                    available_quantity DECIMAL(18,6) NOT NULL DEFAULT 0.000000 COMMENT '可用数量',
                                    frozen_quantity DECIMAL(18,6) NOT NULL DEFAULT 0.000000 COMMENT '冻结数量',
                                    avg_cost_price DECIMAL(18,6) NOT NULL DEFAULT 0.000000 COMMENT '持仓成本价',
                                    market_value DECIMAL(18,6) NOT NULL DEFAULT 0.000000 COMMENT '当前市值',
                                    floating_pl DECIMAL(18,6) NOT NULL DEFAULT 0.000000 COMMENT '浮动盈亏',
                                    floating_pl_ratio DECIMAL(10,4) NOT NULL DEFAULT 0.0000 COMMENT '浮动盈亏比例(%)',
                                    version INT NOT NULL DEFAULT 0 COMMENT '版本号(用于乐观锁)',
                                    remark VARCHAR(500) NULL COMMENT '备注',
                                    create_by VARCHAR(64) NOT NULL DEFAULT '' COMMENT '创建者',
                                    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    update_by VARCHAR(64) NOT NULL DEFAULT '' COMMENT '更新者',
                                    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                     -- 业务主键: 账户 + 证券 + 持仓类型
                                    UNIQUE KEY uk_account_security_type (account_id, security_id, position_type),
                                    -- 索引优化
                                    INDEX idx_user_id (user_id),
                                    INDEX idx_account_id (account_id),
                                    INDEX idx_security_id (security_id),
                                    INDEX idx_position_type (position_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='客户持仓表';




-- 成交单表
DROP TABLE IF EXISTS customer_trade_orders;
CREATE TABLE customer_trade_orders (
                              id BIGINT UNSIGNED AUTO_INCREMENT COMMENT '主键ID' PRIMARY KEY,
                              user_id BIGINT UNSIGNED NOT NULL COMMENT '客户ID',
                              account_id BIGINT UNSIGNED NOT NULL COMMENT '资金账户ID',
                              security_id BIGINT UNSIGNED NOT NULL COMMENT '证券ID',
    -- 订单基本信息
                              order_type TINYINT NOT NULL COMMENT '订单类型: 1-限价单, 2-市价单, 3-条件单',
                              direction TINYINT NOT NULL COMMENT '买卖方向: 1-买入, 2-卖出',
                              price DECIMAL(18,6) NOT NULL COMMENT '委托价格(市价单可为0)',
                              quantity DECIMAL(18,6) NOT NULL COMMENT '委托数量',
                              amount DECIMAL(18,6) NOT NULL COMMENT '委托金额(估算)',
    -- 订单状态
                              status TINYINT NOT NULL DEFAULT 0 COMMENT '订单状态: 0-待报, 1-已报, 2-部分成交, 3-完全成交, 4-部分撤单, 5-完全撤单, 6-废单',
                              filled_quantity DECIMAL(18,6) NOT NULL DEFAULT 0.000000 COMMENT '已成交数量',
                              filled_amount DECIMAL(18,6) NOT NULL DEFAULT 0.000000 COMMENT '已成交金额',
                              avg_filled_price DECIMAL(18,6) NOT NULL DEFAULT 0.000000 COMMENT '平均成交价格',
    -- 费用信息
                              estimated_fee DECIMAL(18,6) NOT NULL DEFAULT 0.000000 COMMENT '预估手续费',
                              estimated_tax DECIMAL(18,6) NOT NULL DEFAULT 0.000000 COMMENT '预估税费',
                              actual_fee DECIMAL(18,6) NOT NULL DEFAULT 0.000000 COMMENT '实际手续费',
                              actual_tax DECIMAL(18,6) NOT NULL DEFAULT 0.000000 COMMENT '实际税费',
    -- 时间信息
                              order_time DATETIME(3) NOT NULL COMMENT '订单时间',
                              expire_time DATETIME(3) NULL COMMENT '订单过期时间',
                              report_time DATETIME(3) NULL COMMENT '报单时间',
                              cancel_time DATETIME(3) NULL COMMENT '撤单时间',
                              finish_time DATETIME(3) NULL COMMENT '完成时间',
    -- 条件单相关
                              condition_type TINYINT NULL COMMENT '条件单类型: 1-价格条件, 2-时间条件',
                              condition_value DECIMAL(18,6) NULL COMMENT '条件值',
    -- 风控信息
                              risk_checked TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否通过风控检查: 0-否, 1-是',
                              risk_remark VARCHAR(500) NULL COMMENT '风控备注',
    -- 系统信息
                              version INT NOT NULL DEFAULT 0 COMMENT '版本号(用于乐观锁)',
                              remark VARCHAR(500) NULL COMMENT '备注',
                              create_by VARCHAR(64) NOT NULL DEFAULT '' COMMENT '创建者',
                              create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              update_by VARCHAR(64) NOT NULL DEFAULT '' COMMENT '更新者',
                              update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    -- 索引优化
                              INDEX idx_user_id (user_id),
                              INDEX idx_account_id (account_id),
                              INDEX idx_security_id (security_id),
                              INDEX idx_order_time (order_time),
                              INDEX idx_status (status),
                              INDEX idx_user_status (user_id, status),
                              INDEX idx_account_security (account_id, security_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='交易订单表';



-- 账户资金流水表
DROP TABLE IF EXISTS customer_account_fund_flows;
CREATE TABLE customer_account_fund_flows (
                                    id BIGINT UNSIGNED AUTO_INCREMENT COMMENT '主键ID' PRIMARY KEY,
                                    account_id BIGINT UNSIGNED NOT NULL COMMENT '关联资金账户ID',
                                    user_id BIGINT UNSIGNED NOT NULL COMMENT '客户ID',
                                    currency CHAR(3) NOT NULL COMMENT '币种',
                                    amount DECIMAL(18,6) NOT NULL COMMENT '变动金额 (正:流入, 负:流出)',
                                    balance_before DECIMAL(18,6) NOT NULL COMMENT '变动前余额',
                                    balance_after DECIMAL(18,6) NOT NULL COMMENT '变动后余额',
                                    flow_type TINYINT NOT NULL COMMENT '流水类型: 1-入金, 2-出金, 3-买入扣款, 4-卖出收款, 5-手续费, 6-税费, 7-利息, 8-分红, 9-融资融券利息, 10-其他',
                                    related_id BIGINT UNSIGNED NULL COMMENT '关联业务ID (如 trade_id, dividend_id)',
                                    related_type TINYINT NOT NULL COMMENT '关联业务类型: 1-fund_transfers, 2-trades, 3-dividends',
                                    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 1-成功, 2-处理中, 3-失败',
                                    remark VARCHAR(500) NULL COMMENT '备注',
                                    create_by VARCHAR(64) NOT NULL DEFAULT '' COMMENT '创建者',
                                    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    update_by VARCHAR(64) NOT NULL DEFAULT '' COMMENT '更新者',
                                    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    -- 索引优化
                                    INDEX idx_account_time (account_id, create_time),
                                    INDEX idx_user_time (user_id, create_time),
                                    INDEX idx_flow_type (flow_type),
                                    INDEX idx_related (related_type, related_id),
                                    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='账户资金流水表';


