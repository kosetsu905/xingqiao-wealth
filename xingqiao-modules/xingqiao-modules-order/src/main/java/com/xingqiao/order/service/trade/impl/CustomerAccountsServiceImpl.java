package com.xingqiao.order.service.trade.impl;

import com.xingqiao.common.security.utils.SecurityUtils;
import com.xingqiao.order.domain.CustomerAccounts;
import com.xingqiao.order.mapper.CustomerAccountsMapper;
import com.xingqiao.order.service.trade.ICustomerAccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

import com.xingqiao.common.core.constant.UserConstants;
import com.xingqiao.common.core.text.Convert;
import com.xingqiao.order.domain.CustomerAccounts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Slf4j
@Service
public class CustomerAccountsServiceImpl implements ICustomerAccountsService {

    @Autowired
    private CustomerAccountsMapper customerAccountsMapper;

    @Override
    public List<CustomerAccounts> selectCustomerAccountsList(CustomerAccounts customerAccounts) {
        return customerAccountsMapper.selectCustomerAccountsList(customerAccounts);
    }

    @Override
    public CustomerAccounts selectCustomerAccountsById(Long id) {
        return customerAccountsMapper.selectCustomerAccountsById(id);
    }

    @Override
    public CustomerAccounts selectCustomerAccountsByUserId(Long userId) {
        return customerAccountsMapper.selectCustomerAccountsById(userId);
    }

    @Override
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
        account.setRemark(remark);
        account.setCreateBy(SecurityUtils.getUsername()); // 若依当前登录用户

        return customerAccountsMapper.insertCustomerAccounts(account);
    }

    @Override
    public int updateCustomerAccounts(CustomerAccounts customerAccounts) {
        customerAccounts.setUpdateBy(SecurityUtils.getUsername());
        return customerAccountsMapper.updateCustomerAccounts(customerAccounts);
    }

    @Override
    public int deleteCustomerAccountsByIds(Long[] ids) {
        // 直接调用 Mapper 删除
        return customerAccountsMapper.deleteCustomerAccountsByIds(ids);
    }

    @Override
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

    @Override
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

    @Override
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