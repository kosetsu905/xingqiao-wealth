package com.xingqiao.order.service.trade;

import com.xingqiao.common.security.utils.SecurityUtils;
import com.xingqiao.order.domain.CustomerAccounts;
import com.xingqiao.order.mapper.CustomerAccountsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
public class CustomerAccountsService {

    @Autowired
    private CustomerAccountsMapper customerAccountsMapper;

    /**
     * 查询账户列表
     */
    public List<CustomerAccounts> selectCustomerAccountsList(CustomerAccounts customerAccounts) {
        return customerAccountsMapper.selectCustomerAccountsList(customerAccounts);
    }

    /**
     * 根据ID获取账户
     */
    public CustomerAccounts selectCustomerAccountsById(Long id) {
        return customerAccountsMapper.selectCustomerAccountsById(id);
    }

    /**
     * 根据用户ID获取账户
     */
    public CustomerAccounts selectCustomerAccountsByUserId(Long userId) {
        return customerAccountsMapper.selectCustomerAccountsById(userId);
    }

    /**
     * 创建新账户（初始化余额为0）
     */
    public int createAccount(Long userId, String currency, String remark) {
        if (userId == null || currency == null || currency.trim().isEmpty()) {
            throw new IllegalArgumentException("用户ID和币种不能为空");
        }

        // 检查是否已存在该用户+币种的账户
        CustomerAccounts exists = customerAccountsMapper.selectCustomerAccountsById(userId);
        if (exists != null) {
            throw new IllegalArgumentException("该用户已存在资金账户");
        }

        CustomerAccounts account = new CustomerAccounts();
        account.setUserId(userId);
        account.setCurrency(currency.trim().toUpperCase());
        account.setTotalBalance(BigDecimal.ZERO);
        account.setAvailableBalance(BigDecimal.ZERO);
        account.setFrozenBalance(BigDecimal.ZERO);
        account.setWithdrawableBalance(BigDecimal.ZERO);
        account.setCreditBalance(BigDecimal.ZERO);
        account.setVersion(0L);
        account.setStatus(1L); // 正常
        account.setAccountType(1); // 默认实盘账户
        account.setAccountName(null); // 账户名称默认为空
        account.setRemark(remark);
        account.setCreateBy(SecurityUtils.getUsername()); // 若依当前登录用户

        return customerAccountsMapper.insertCustomerAccounts(account);
    }

    /**
     * 更新账户信息（带乐观锁）
     */
    public int updateCustomerAccounts(CustomerAccounts customerAccounts) {
        customerAccounts.setUpdateBy(SecurityUtils.getUsername());
        return customerAccountsMapper.updateCustomerAccounts(customerAccounts);
    }

    /**
     * 批量删除账户（ID数组）
     */
    public int deleteCustomerAccountsByIds(Long[] ids) {
        // 直接调用 Mapper 删除
        return customerAccountsMapper.deleteCustomerAccountsByIds(ids);
    }

    /**
     * 冻结账户（状态 -> 2）
     */
    public int freezeAccount(Long id) {
        CustomerAccounts account = selectCustomerAccountsById(id);
        if (account == null) {
            throw new IllegalArgumentException("账户不存在");
        }
        if (account.getStatus().equals(2)) {
            return 0; // 已冻结
        }
        account.setStatus(2L);
        account.setRemark("管理员冻结账户");
        return updateCustomerAccounts(account);
    }

    /**
     * 解冻账户（状态 -> 1）
     */
    public int unfreezeAccount(Long id) {
        CustomerAccounts account = selectCustomerAccountsById(id);
        if (account == null) {
            throw new IllegalArgumentException("账户不存在");
        }
        if (account.getStatus().equals(1)) {
            return 0; // 已正常
        }
        account.setStatus(1L);
        account.setRemark("解冻账户");
        return updateCustomerAccounts(account);
    }

    /**
     * 销户（状态 -> 3）
     */
    public int closeAccount(Long id) {
        CustomerAccounts account = selectCustomerAccountsById(id);
        if (account == null) {
            throw new IllegalArgumentException("账户不存在");
        }
        if (account.getStatus().equals(3)) {
            return 0; // 已销户
        }
        if (account.getTotalBalance().compareTo(BigDecimal.ZERO) > 0) {
            throw new IllegalArgumentException("账户余额不为零，无法销户");
        }
        account.setStatus(3L);
        account.setRemark("申请销户");
        return updateCustomerAccounts(account);
    }
}