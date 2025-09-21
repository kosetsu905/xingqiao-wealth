package com.xingqiao.order.mapper.system;

import com.xingqiao.order.domain.CustomerAccounts;
import com.xingqiao.order.mapper.CustomerAccountsMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.Resource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("dev") // 使用开发环境配置
public class CustomerAccountsMapperTest {
    @Resource
    private CustomerAccountsMapper customerAccountsMapper;


    /**
     * 测试使用空条件查询所有客户信息
     */
    @Test
    public void testSelectWithEmptyCondition() {
        // 创建空条件
        CustomerAccounts query = new CustomerAccounts();

        // 执行查询
        List<CustomerAccounts> result = customerAccountsMapper.selectCustomerAccountsList(query);
        System.out.println("查询到的客户账户信息数量: " + result.size());
        // 验证结果
        assertNotNull(result, "查询结果不应为null");
    }
}
