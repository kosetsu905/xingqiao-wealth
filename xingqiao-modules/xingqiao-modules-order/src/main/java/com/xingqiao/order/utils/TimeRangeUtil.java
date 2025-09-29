package com.xingqiao.order.utils;

import com.xingqiao.order.config.StockTradingHoursConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.*;

/**
 * 时间范围工具类，用于判断市场开市时间
 * 
 * @author xingqiao
 * @date 2025-09-23
 */
@Component
public class TimeRangeUtil {
    
    private static final Logger log = LoggerFactory.getLogger(TimeRangeUtil.class);
    
    // 存储各市场开市时间范围的映射
    private static final Map<String, List<TimeRange>> marketOpenTimeRanges = new HashMap<>();
    
    private final StockTradingHoursConfig stockTradingHoursConfig;
    
    @Autowired
    public TimeRangeUtil(StockTradingHoursConfig stockTradingHoursConfig) {
        this.stockTradingHoursConfig = stockTradingHoursConfig;
        // 初始化开市时间范围
        initializeMarketOpenTimeRanges();
    }
    
    /**
     * 初始化各市场开市时间范围
     */
    public void initializeMarketOpenTimeRanges() {
        if (stockTradingHoursConfig == null || stockTradingHoursConfig.getOpens() == null) {
            log.warn("股票开市时间配置未初始化或为空");
            return;
        }
        
        // 遍历所有市场的开市时间配置
        for (Map.Entry<String, String> entry : stockTradingHoursConfig.getOpens().entrySet()) {
            String marketCode = entry.getKey();
            String timeRangeStr = entry.getValue();
            List<TimeRange> timeRanges = parseTimeRanges(timeRangeStr);
            if (!timeRanges.isEmpty()) {
                marketOpenTimeRanges.put(marketCode, timeRanges);
                log.info("初始化市场 {} 开市时间: {}", marketCode, timeRangeStr);
            }
        }
        
        log.info("市场开市时间初始化完成，共配置 {} 个市场", marketOpenTimeRanges.size());
    }
    
    /**
     * 解析时间范围字符串为TimeRange对象列表
     * 格式如: "09:15:00-11:30:00,13:00:00-15:00:00"
     */
    public List<TimeRange> parseTimeRanges(String timeRangesStr) {
        List<TimeRange> timeRanges = new ArrayList<>();
        if (timeRangesStr == null || timeRangesStr.isEmpty()) {
            return timeRanges;
        }

        String[] ranges = timeRangesStr.split(",");
        for (String range : ranges) {
            String[] times = range.split("-");
            if (times.length == 2) {
                try {
                    LocalTime startTime = LocalTime.parse(times[0]);
                    LocalTime endTime = LocalTime.parse(times[1]);
                    timeRanges.add(new TimeRange(startTime, endTime));
                } catch (Exception e) {
                    log.error("解析时间格式错误: {}", range, e);
                }
            }
        }
        return timeRanges;
    }
    
    /**
     * 判断当前时间是否在指定市场的开市时间内
     * 
     * @param marketCode 市场代码
     * @return 是否在开市时间内
     */
    public boolean isInTradingHours(String marketCode) {
        if (marketCode == null) {
            return false;
        }

        // 优先从内存缓存中获取时间范围
        List<TimeRange> timeRanges = marketOpenTimeRanges.get(marketCode);
        
        // 如果内存缓存中没有，则从配置中动态获取并解析
        if (timeRanges == null || timeRanges.isEmpty()) {
            String timeRangeStr = stockTradingHoursConfig.getOpenTimeByMarketCode(marketCode);
            if (timeRangeStr != null) {
                timeRanges = parseTimeRanges(timeRangeStr);
                if (!timeRanges.isEmpty()) {
                    marketOpenTimeRanges.put(marketCode, timeRanges);
                }
            }
        }
        
        if (timeRanges == null || timeRanges.isEmpty()) {
            log.warn("未配置市场 {} 的开市时间，默认为非交易时间", marketCode);
            return false;
        }

        LocalTime currentTime = LocalTime.now();
        for (TimeRange range : timeRanges) {
            if (range.contains(currentTime)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 时间范围类
     */
    public static class TimeRange {
        private final LocalTime startTime;
        private final LocalTime endTime;

        public TimeRange(LocalTime startTime, LocalTime endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
        }

        /**
         * 判断给定时间是否在时间范围内
         */
        public boolean contains(LocalTime time) {
            return !time.isBefore(startTime) && !time.isAfter(endTime);
        }
        
        @Override
        public String toString() {
            return startTime + "-" + endTime;
        }
    }
}