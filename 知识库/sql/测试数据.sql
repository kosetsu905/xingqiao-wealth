-- 星桥财富系统测试数据
-- 包含客户资金账户、证券信息、客户持仓、交易订单和资金流水测试数据

-- 注意：运行前请确保已创建数据库和表结构
-- use `xingqiao-trade`;

-- 1. 客户资金账户表测试数据
INSERT INTO customer_accounts (user_id, currency, total_balance, available_balance, frozen_balance, withdrawable_balance, credit_balance, status, create_by, create_time, update_by, update_time)
VALUES 
(10001, 'CNY', 100000.000000, 90000.000000, 10000.000000, 80000.000000, 0.000000, 1, 'system', NOW(), 'system', NOW()),
(10002, 'CNY', 200000.000000, 180000.000000, 20000.000000, 160000.000000, 50000.000000, 1, 'system', NOW(), 'system', NOW()),
(10003, 'USD', 50000.000000, 45000.000000, 5000.000000, 40000.000000, 0.000000, 1, 'system', NOW(), 'system', NOW()),
(10004, 'CNY', 300000.000000, 250000.000000, 50000.000000, 200000.000000, 100000.000000, 1, 'system', NOW(), 'system', NOW()),
(10005, 'CNY', 150000.000000, 150000.000000, 0.000000, 150000.000000, 0.000000, 1, 'system', NOW(), 'system', NOW()),
(10006, 'CNY', 80000.000000, 70000.000000, 10000.000000, 60000.000000, 0.000000, 2, 'system', NOW(), 'system', NOW()),  -- 冻结账户
(10007, 'CNY', 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 3, 'system', NOW(), 'system', NOW());  -- 销户账户

-- 2. 证券信息表测试数据
INSERT INTO securities (security_code, security_name, exchange_code, security_type, currency, lot_size, tick_size, price_limit, is_margin, status, listed_date, create_by, create_time, update_by, update_time)
VALUES 
('600519', '贵州茅台', 'SSE', 1, 'CNY', 100, 0.01, 10.00, 1, 1, '2001-08-27', 'system', NOW(), 'system', NOW()),
('000858', '五粮液', 'SZSE', 1, 'CNY', 100, 0.01, 10.00, 1, 1, '1998-04-27', 'system', NOW(), 'system', NOW()),
('000001', '平安银行', 'SZSE', 1, 'CNY', 100, 0.01, 10.00, 1, 1, '1991-04-03', 'system', NOW(), 'system', NOW()),
('601318', '中国平安', 'SSE', 1, 'CNY', 100, 0.01, 10.00, 1, 1, '2007-03-01', 'system', NOW(), 'system', NOW()),
('300750', '宁德时代', 'SZSE', 1, 'CNY', 100, 0.01, 20.00, 1, 1, '2018-06-11', 'system', NOW(), 'system', NOW()),
('600036', '招商银行', 'SSE', 1, 'CNY', 100, 0.01, 10.00, 1, 1, '2002-04-09', 'system', NOW(), 'system', NOW()),
('AAPL', '苹果公司', 'NASDAQ', 1, 'USD', 1, 0.01, NULL, 0, 1, '1980-12-12', 'system', NOW(), 'system', NOW()),
('TSLA', '特斯拉', 'NASDAQ', 1, 'USD', 1, 0.01, NULL, 0, 1, '2010-06-29', 'system', NOW(), 'system', NOW()),
('000002', '万科A', 'SZSE', 1, 'CNY', 100, 0.01, 10.00, 1, 2, '1991-01-29', 'system', NOW(), 'system', NOW()),  -- 停牌
('510300', '沪深300ETF', 'SZSE', 2, 'CNY', 100, 0.001, 10.00, 1, 1, '2012-05-04', 'system', NOW(), 'system', NOW());

-- 3. 客户持仓表测试数据
-- 注意：这里需要使用实际的account_id和security_id
-- 先获取已插入的账户和证券ID
-- 假设account_id分别为1-7，security_id分别为1-10
INSERT INTO customer_positions (user_id, account_id, security_id, position_type, quantity, available_quantity, frozen_quantity, avg_cost_price, market_value, floating_pl, floating_pl_ratio, create_by, create_time, update_by, update_time)
VALUES 
(10001, 1, 1, 1, 100.000000, 100.000000, 0.000000, 1600.000000, 170000.000000, 10000.000000, 6.2500, 'system', NOW(), 'system', NOW()),
(10001, 1, 2, 1, 200.000000, 200.000000, 0.000000, 160.000000, 34000.000000, 2000.000000, 6.2500, 'system', NOW(), 'system', NOW()),
(10002, 2, 3, 1, 500.000000, 400.000000, 100.000000, 12.000000, 65000.000000, 5000.000000, 8.3300, 'system', NOW(), 'system', NOW()),
(10002, 2, 4, 1, 300.000000, 300.000000, 0.000000, 45.000000, 15000.000000, -1500.000000, -11.1100, 'system', NOW(), 'system', NOW()),
(10003, 3, 7, 1, 100.000000, 100.000000, 0.000000, 180.000000, 19500.000000, 1500.000000, 8.3300, 'system', NOW(), 'system', NOW()),
(10003, 3, 8, 1, 50.000000, 50.000000, 0.000000, 700.000000, 37500.000000, 2500.000000, 7.1400, 'system', NOW(), 'system', NOW()),
(10004, 4, 5, 1, 200.000000, 200.000000, 0.000000, 300.000000, 68000.000000, 8000.000000, 13.3300, 'system', NOW(), 'system', NOW()),
(10004, 4, 6, 1, 400.000000, 300.000000, 100.000000, 35.000000, 14000.000000, 0.000000, 0.0000, 'system', NOW(), 'system', NOW()),
(10001, 1, 9, 1, 1000.000000, 0.000000, 1000.000000, 15.000000, 16000.000000, 1000.000000, 6.6700, 'system', NOW(), 'system', NOW()),  -- 停牌股票，全部冻结
(10002, 2, 10, 1, 5000.000000, 5000.000000, 0.000000, 5.000000, 26000.000000, 1000.000000, 4.0000, 'system', NOW(), 'system', NOW());

-- 4. 交易订单表测试数据
INSERT INTO customer_trade_orders (user_id, account_id, security_id, order_type, direction, price, quantity, amount, status, filled_quantity, filled_amount, avg_filled_price, estimated_fee, estimated_tax, actual_fee, actual_tax, order_time, report_time, finish_time, risk_checked, create_by, create_time, update_by, update_time)
VALUES 
(10001, 1, 1, 1, 1, 1650.000000, 100.000000, 165000.000000, 3, 100.000000, 165000.000000, 1650.000000, 16.500000, 0.000000, 16.500000, 0.000000, NOW() - INTERVAL 1 DAY, NOW() - INTERVAL 1 DAY, NOW() - INTERVAL 1 DAY + INTERVAL 10 MINUTE, 1, 'system', NOW(), 'system', NOW()),
(10001, 1, 2, 1, 1, 165.000000, 200.000000, 33000.000000, 3, 200.000000, 33000.000000, 165.000000, 3.300000, 0.000000, 3.300000, 0.000000, NOW() - INTERVAL 2 DAY, NOW() - INTERVAL 2 DAY, NOW() - INTERVAL 2 DAY + INTERVAL 5 MINUTE, 1, 'system', NOW(), 'system', NOW()),
(10002, 2, 3, 1, 1, 12.500000, 500.000000, 6250.000000, 3, 500.000000, 6250.000000, 12.500000, 0.630000, 0.000000, 0.630000, 0.000000, NOW() - INTERVAL 3 DAY, NOW() - INTERVAL 3 DAY, NOW() - INTERVAL 3 DAY + INTERVAL 8 MINUTE, 1, 'system', NOW(), 'system', NOW()),
(10002, 2, 4, 1, 1, 48.000000, 300.000000, 14400.000000, 3, 300.000000, 14400.000000, 48.000000, 1.440000, 0.000000, 1.440000, 0.000000, NOW() - INTERVAL 4 DAY, NOW() - INTERVAL 4 DAY, NOW() - INTERVAL 4 DAY + INTERVAL 12 MINUTE, 1, 'system', NOW(), 'system', NOW()),
(10003, 3, 7, 1, 1, 190.000000, 100.000000, 19000.000000, 3, 100.000000, 19000.000000, 190.000000, 19.000000, 0.000000, 19.000000, 0.000000, NOW() - INTERVAL 5 DAY, NOW() - INTERVAL 5 DAY, NOW() - INTERVAL 5 DAY + INTERVAL 20 MINUTE, 1, 'system', NOW(), 'system', NOW()),
(10003, 3, 8, 1, 1, 720.000000, 50.000000, 36000.000000, 3, 50.000000, 36000.000000, 720.000000, 36.000000, 0.000000, 36.000000, 0.000000, NOW() - INTERVAL 6 DAY, NOW() - INTERVAL 6 DAY, NOW() - INTERVAL 6 DAY + INTERVAL 15 MINUTE, 1, 'system', NOW(), 'system', NOW()),
(10004, 4, 5, 1, 1, 310.000000, 200.000000, 62000.000000, 3, 200.000000, 62000.000000, 310.000000, 62.000000, 0.000000, 62.000000, 0.000000, NOW() - INTERVAL 7 DAY, NOW() - INTERVAL 7 DAY, NOW() - INTERVAL 7 DAY + INTERVAL 25 MINUTE, 1, 'system', NOW(), 'system', NOW()),
(10004, 4, 6, 1, 1, 35.000000, 400.000000, 14000.000000, 3, 400.000000, 14000.000000, 35.000000, 14.000000, 0.000000, 14.000000, 0.000000, NOW() - INTERVAL 8 DAY, NOW() - INTERVAL 8 DAY, NOW() - INTERVAL 8 DAY + INTERVAL 30 MINUTE, 1, 'system', NOW(), 'system', NOW()),
(10001, 1, 1, 1, 2, 1700.000000, 50.000000, 85000.000000, 1, 0.000000, 0.000000, 0.000000, 8.500000, 850.000000, 0.000000, 0.000000, NOW(), NOW(), NULL, 1, 'system', NOW(), 'system', NOW()),  -- 未成交卖出单
(10002, 2, 3, 1, 2, 13.200000, 100.000000, 13200.000000, 5, 0.000000, 0.000000, 0.000000, 1.320000, 132.000000, 0.000000, 0.000000, NOW() - INTERVAL 1 HOUR, NOW() - INTERVAL 1 HOUR, NOW() - INTERVAL 30 MINUTE, 1, 'system', NOW(), 'system', NOW());  -- 已撤单

-- 5. 账户资金流水表测试数据
INSERT INTO customer_account_fund_flows (account_id, user_id, currency, amount, balance_before, balance_after, flow_type, related_id, related_type, status, remark, create_by, create_time, update_by, update_time)
VALUES 
(1, 10001, 'CNY', 100000.000000, 0.000000, 100000.000000, 1, NULL, 1, 1, '初始入金', 'system', NOW(), 'system', NOW()),
(1, 10001, 'CNY', -165000.000000, 100000.000000, -65000.000000, 3, 1, 2, 1, '买入贵州茅台', 'system', NOW(), 'system', NOW()),
(1, 10001, 'CNY', 100000.000000, -65000.000000, 35000.000000, 1, NULL, 1, 1, '补充入金', 'system', NOW(), 'system', NOW()),
(1, 10001, 'CNY', -33000.000000, 35000.000000, 2000.000000, 3, 2, 2, 1, '买入五粮液', 'system', NOW(), 'system', NOW()),
(1, 10001, 'CNY', 150000.000000, 2000.000000, 152000.000000, 1, NULL, 1, 1, '大额入金', 'system', NOW(), 'system', NOW()),
(2, 10002, 'CNY', 200000.000000, 0.000000, 200000.000000, 1, NULL, 1, 1, '初始入金', 'system', NOW(), 'system', NOW()),
(2, 10002, 'CNY', -6250.000000, 200000.000000, 193750.000000, 3, 3, 2, 1, '买入平安银行', 'system', NOW(), 'system', NOW()),
(2, 10002, 'CNY', -14400.000000, 193750.000000, 179350.000000, 3, 4, 2, 1, '买入中国平安', 'system', NOW(), 'system', NOW()),
(3, 10003, 'USD', 50000.000000, 0.000000, 50000.000000, 1, NULL, 1, 1, '初始入金(USD)', 'system', NOW(), 'system', NOW()),
(3, 10003, 'USD', -19000.000000, 50000.000000, 31000.000000, 3, 5, 2, 1, '买入苹果公司', 'system', NOW(), 'system', NOW()),
(3, 10003, 'USD', -36000.000000, 31000.000000, -5000.000000, 3, 6, 2, 1, '买入特斯拉', 'system', NOW(), 'system', NOW()),
(3, 10003, 'USD', 40000.000000, -5000.000000, 35000.000000, 1, NULL, 1, 1, '补充入金(USD)', 'system', NOW(), 'system', NOW()),
(4, 10004, 'CNY', 300000.000000, 0.000000, 300000.000000, 1, NULL, 1, 1, '初始入金', 'system', NOW(), 'system', NOW()),
(4, 10004, 'CNY', -62000.000000, 300000.000000, 238000.000000, 3, 7, 2, 1, '买入宁德时代', 'system', NOW(), 'system', NOW()),
(4, 10004, 'CNY', -14000.000000, 238000.000000, 224000.000000, 3, 8, 2, 1, '买入招商银行', 'system', NOW(), 'system', NOW()),
(1, 10001, 'CNY', -16.500000, 152000.000000, 151983.500000, 5, 1, 2, 1, '贵州茅台交易手续费', 'system', NOW(), 'system', NOW()),
(1, 10001, 'CNY', -3.300000, 151983.500000, 151980.200000, 5, 2, 2, 1, '五粮液交易手续费', 'system', NOW(), 'system', NOW()),
(2, 10002, 'CNY', -0.630000, 179350.000000, 179349.370000, 5, 3, 2, 1, '平安银行交易手续费', 'system', NOW(), 'system', NOW()),
(2, 10002, 'CNY', -1.440000, 179349.370000, 179347.930000, 5, 4, 2, 1, '中国平安交易手续费', 'system', NOW(), 'system', NOW()),
(5, 10005, 'CNY', 150000.000000, 0.000000, 150000.000000, 1, NULL, 1, 1, '新用户入金', 'system', NOW(), 'system', NOW());