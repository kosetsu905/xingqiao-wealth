package com.xingqiao.order.base;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

/**
 * 公共测试父类
 * 
 * @author lingma
 */
@SpringBootTest
@ActiveProfiles("dev") // 使用开发环境配置/
public class BaseTest {
    @DynamicPropertySource
    static void setTestEnv(DynamicPropertyRegistry registry) {
        // 动态设置 test.env=true
        registry.add("test.env", () -> "true");
    }

}