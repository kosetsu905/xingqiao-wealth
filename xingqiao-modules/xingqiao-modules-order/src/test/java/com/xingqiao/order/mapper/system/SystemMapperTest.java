package com.xingqiao.order.mapper.system;

import com.xingqiao.order.domain.system.CustomerInfoExt;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.Resource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SystemMapper测试类
 *
 * @author xingqiao
 * @date 2025-09-21
 */
@SpringBootTest
@ActiveProfiles("dev") // 使用开发环境配置
public class SystemMapperTest {

    @Resource
    private SystemMapper systemMapper;

    /**
     * 测试查询客户信息列表
     */
    @Test
    public void testSelectCustomerInfoList() {
        // 创建测试条件
        CustomerInfoExt query = new CustomerInfoExt();
        query.setUserType("02"); // 客户类型
        query.setStatus("1");    // 审核通过

        // 执行查询
        List<CustomerInfoExt> result = systemMapper.selectCustomerInfoList(query);

        // 验证结果
        assertNotNull(result, "查询结果不应为null");
        System.out.println("查询到的客户信息数量: " + result.size());
        
        // 如果有数据，打印第一条记录的基本信息
        if (!result.isEmpty()) {
            CustomerInfoExt firstCustomer = result.get(0);
            System.out.println("第一条客户信息: ");
            System.out.println("客户ID: " + firstCustomer.getUserId());
            System.out.println("客户姓名: " + firstCustomer.getFullName());
            System.out.println("手机号码: " + firstCustomer.getPhoneNumber());
        }
    }

    /**
     * 测试使用空条件查询所有客户信息
     */
    @Test
    public void testSelectCustomerInfoWithEmptyCondition() {
        // 创建空条件
        CustomerInfoExt query = new CustomerInfoExt();

        // 执行查询
        List<CustomerInfoExt> result = systemMapper.selectCustomerInfoList(query);
        System.out.println("查询到的客户信息数量: " + result.size());
        // 验证结果
        assertNotNull(result, "查询结果不应为null");
    }

    /**
     * 测试查询特定客户信息
     */
    @Test
    public void testSelectSpecificCustomerInfo() {
        // 创建测试条件 - 假设系统中存在用户ID为1的客户
        CustomerInfoExt query = new CustomerInfoExt();
        query.setUserId(1L);

        // 执行查询
        List<CustomerInfoExt> result = systemMapper.selectCustomerInfoList(query);

        // 验证结果
        assertNotNull(result, "查询结果不应为null");
        
        // 如果找到相关记录，验证其用户ID
        if (!result.isEmpty()) {
            for (CustomerInfoExt customer : result) {
                assertEquals(1L, customer.getUserId(), "返回的客户ID应与查询条件匹配");
            }
        }
    }
}