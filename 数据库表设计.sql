

DROP DATABASE IF EXISTS `xingqiao-trade`;
CREATE DATABASE  `xingqiao-trade` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;


-- 客户资金账户表
DROP TABLE IF EXISTS customer_accounts;
CREATE TABLE customer_accounts (
    id    bigint auto_increment comment '主键ID' primary key,
    user_id   bigint    null comment '客户ID',
    currency CHAR(10) NOT NULL COMMENT '币种 (如 CNY, USD)',
    total_balance DECIMAL(18,6) NOT NULL DEFAULT 0.000000 COMMENT '总资金',
    available_balance DECIMAL(18,6) NOT NULL DEFAULT 0.000000 COMMENT '可用资金',
    frozen_balance DECIMAL(18,6) NOT NULL DEFAULT 0.000000 COMMENT '冻结资金',
    withdawable_balance DECIMAL(18,6) NOT NULL DEFAULT 0.000000 COMMENT '可取资金',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 1-正常, 2-冻结, 3-销户',
    remark                     varchar(500)                  null comment '备注',
    create_by                  varchar(64)      default ''   null comment '创建者',
    create_time                datetime                      null comment '创建时间',
    update_by                  varchar(64)      default ''   null comment '更新者',
    update_time                datetime                      null comment '更新时间'
    -- 索引
    INDEX idx_user_id (user_id),
    INDEX idx_currency (currency),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='客户资金账户表';


-- 证券信息表
DROP TABLE IF EXISTS t_securities;
CREATE TABLE t_securities (
    id    bigint auto_increment comment '主键ID' primary key,
    security_code VARCHAR(20) NOT NULL COMMENT '证券代码 (如 600519)',
    security_name VARCHAR(100) NOT NULL COMMENT '证券名称 (如 贵州茅台)',
    exchange_code VARCHAR(10) NOT NULL COMMENT '交易所代码 (如 SSE, SZSE)',
    security_type TINYINT NOT NULL COMMENT '证券类型: 1-股票, 2-基金, 3-债券, 4-期货',
    currency CHAR(3) NOT NULL COMMENT '交易币种',
    lot_size INT NOT NULL DEFAULT 1 COMMENT '交易单位 (每手股数)',
    tick_size DECIMAL(10,6) NOT NULL COMMENT '最小变动价位',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 1-交易, 2-停牌, 3-退市',
    listed_date datetime null COMMENT '上市日期',
    delisted_date datetime null COMMENT '退市日期',
    remark         varchar(500)                  null comment '备注',
    create_by       varchar(64)      default ''   null comment '创建者',
    create_time      datetime                      null comment '创建时间',
    update_by      varchar(64)      default ''   null comment '更新者',
    update_time     datetime                      null comment '更新时间'
    -- 业务主键: 代码 + 交易所
    UNIQUE KEY uk_security_exchange (security_code, exchange_code),
    -- 索引
    INDEX idx_security_code (security_code),
    INDEX idx_exchange_code (exchange_code),
    INDEX idx_security_type (security_type),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='证券信息表';



-- 客户持仓表
DROP TABLE IF EXISTS customer_positions;
CREATE TABLE customer_positions (
    id    bigint auto_increment comment '主键ID' primary key,
    user_id   bigint    null comment '客户ID',
    account_id BIGINT UNSIGNED NOT NULL COMMENT '资金账户ID',
    security_id BIGINT UNSIGNED NOT NULL COMMENT '证券ID',
    quantity DECIMAL(18,6) NOT NULL DEFAULT 0.000000 COMMENT '总持仓数量',
    available_quantity DECIMAL(18,6) NOT NULL DEFAULT 0.000000 COMMENT '可用数量',
    frozen_quantity DECIMAL(18,6) NOT NULL DEFAULT 0.000000 COMMENT '冻结数量',
    avg_cost_price DECIMAL(18,6) NOT NULL DEFAULT 0.000000 COMMENT '持仓成本价',
    market_value DECIMAL(18,6) NOT NULL DEFAULT 0.000000 COMMENT '当前市值',
    remark         varchar(500)                  null comment '备注',
    create_by       varchar(64)      default ''   null comment '创建者',
    create_time      datetime                      null comment '创建时间',
    update_by      varchar(64)      default ''   null comment '更新者',
    update_time     datetime                      null comment '更新时间'
    -- 业务主键: 账户 + 证券
    UNIQUE KEY uk_account_security (account_id, security_id),
    
    -- 索引
    INDEX idx_user_id (user_id),
    INDEX idx_account_id (account_id),
    INDEX idx_security_id (security_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='客户持仓表';




-- 成交单表
DROP TABLE IF EXISTS customer_trade_orders;
CREATE TABLE customer_trade_orders (
    id    bigint auto_increment comment '主键ID' primary key,
    user_id   bigint    null comment '客户ID',
    account_id BIGINT UNSIGNED NOT NULL COMMENT '资金账户ID',
    security_id BIGINT UNSIGNED NOT NULL COMMENT '证券ID',
    side TINYINT NOT NULL COMMENT '买卖方向: 1-买, 2-卖',
    price DECIMAL(18,6) NOT NULL COMMENT '成交价格',
    quantity DECIMAL(18,6) NOT NULL COMMENT '成交数量',
    amount DECIMAL(18,6) NOT NULL COMMENT '成交金额 (price * quantity)',
    fee DECIMAL(18,6) NOT NULL DEFAULT 0.000000 COMMENT '手续费',
    tax DECIMAL(18,6) NOT NULL DEFAULT 0.000000 COMMENT '税费',
    trade_time DATETIME(3) NOT NULL COMMENT '成交时间',
    source TINYINT NOT NULL COMMENT '成交来源: 1-交易所, 2-内部撮合',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '订单状态: 0-待报, 1-已报, 2-部分成交, 3-完全成交, 4-部分撤单, 5-完全撤单, 6-废单',
    filled_quantity DECIMAL(18,6) NOT NULL DEFAULT 0.000000 COMMENT '已成交数量',
    filled_amount DECIMAL(18,6) NOT NULL DEFAULT 0.000000 COMMENT '已成交金额',
    avg_filled_price DECIMAL(18,6) NOT NULL DEFAULT 0.000000 COMMENT '平均成交价格',
    remark         varchar(500)                  null comment '备注',
    create_by       varchar(64)      default ''   null comment '创建者',
    create_time      datetime                      null comment '创建时间',
    update_by      varchar(64)      default ''   null comment '更新者',
    update_time     datetime                      null comment '更新时间'
    -- 索引
    INDEX idx_account_id (account_id),
    INDEX idx_user_id (user_id),
    INDEX idx_security_id (security_id),
    INDEX idx_order_time (order_time),
    INDEX idx_trade_time (trade_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='客户成交单表';




-- 账户资金流水表
DROP TABLE IF EXISTS account_fund_flows;
CREATE TABLE account_fund_flows (
    id    bigint auto_increment comment '主键ID' primary key,
    account_id BIGINT UNSIGNED NOT NULL COMMENT '关联资金账户ID',
    user_id   bigint    null comment '客户ID',
    currency CHAR(3) NOT NULL COMMENT '币种',
    amount DECIMAL(18,6) NOT NULL COMMENT '变动金额 (正:流入, 负:流出)',
    balance_after DECIMAL(18,6) NOT NULL COMMENT '变动后余额',
    flow_type TINYINT NOT NULL COMMENT '流水类型: 1-入金, 2-出金, 3-买入扣款, 4-卖出收款, 5-手续费, 6-税费, 7-利息, 8-分红, 9-融资融券利息, 10-其他',
    related_id BIGINT UNSIGNED NULL COMMENT '关联业务ID (如 trade_id, dividend_id)',
    related_type TINYINT NOT NULL COMMENT '关联业务类型: 1-fund_transfers, 2-trades, 3-dividends',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 1-成功, 2-处理中, 3-失败',
    remark         varchar(500)                  null comment '备注',
    create_by       varchar(64)      default ''   null comment '创建者',
    create_time      datetime                      null comment '创建时间',
    update_by      varchar(64)      default ''   null comment '更新者',
    update_time     datetime                      null comment '更新时间'
    -- 索引
    INDEX idx_account_time (account_id, create_time), -- 按账户和时间查询流水
    INDEX idx_user_time (user_id, create_time),
    INDEX idx_flow_type (flow_type),
    INDEX idx_related (related_type, related_id) -- 反向查询
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='账户资金流水表';




