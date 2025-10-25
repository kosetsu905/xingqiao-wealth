package com.xingqiao.order.service.trade.impl;

import com.xingqiao.api.trade.domain.StockQuote;
import com.xingqiao.api.trade.domain.QueryStockQuote;
import com.xingqiao.common.core.domain.R;
import com.xingqiao.order.service.QuoteApiService;
import com.xingqiao.order.quote.QuoteApiStrategy;
import com.xingqiao.order.quote.QuoteApiStrategyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service("quoteApiService")
public class QuoteApiServiceImpl  implements QuoteApiService {

    @Autowired
    private QuoteApiStrategyFactory quoteApiStrategyFactory;


    /**
     * 获取单只股票行情
     * @return 股票行情信息
     */
    @Override
    public R<StockQuote> getStockQuote(QueryStockQuote queryStockQuote) {
        try {
            return quoteApiStrategyFactory.getStockQuote(queryStockQuote);
        } catch (Exception e) {
            return R.fail("获取股票行情失败：" + e.getMessage());
        }
    }

    /**
     * 批量获取股票行情
     * @return 股票行情列表
     */
    @Override
    public R<List<StockQuote>> getStockQuoteList(List<QueryStockQuote> list) {
        try {
            // 直接调用策略工厂的批量获取方法，该方法会遍历所有策略并处理返回结果
            return quoteApiStrategyFactory.getStockQuoteList(list);
        } catch (Exception e) {
            return R.fail("批量获取股票行情失败：" + e.getMessage());
        }
    }

    /**
     * 获取区间行情数据
     */
    @Override
    public R<List<StockQuote>> getStockQuoteChartList(List<QueryStockQuote> list) {
        try {
            return quoteApiStrategyFactory.getStockQuoteChartList(list);
        } catch (Exception e) {
            return R.fail("获取历史行情数据失败：" + e.getMessage());
        }
    }


    /**
     * 根据市场代码获取行情类型
     */
    private String getQuoteType(QueryStockQuote queryStockQuote) {
        // 根据市场代码选择对应的行情API策略
        return "default";
    }
}
