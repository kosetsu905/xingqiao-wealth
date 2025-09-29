package com.xingqiao.order.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 定时任务配置类
 * 启用Spring的定时任务功能
 * 
 * @author xingqiao
 * @date 2025-09-24
 */
@Configuration
@EnableScheduling
public class SchedulingConfig {
    
    // 此类仅用于启用定时任务功能，无需额外配置
}