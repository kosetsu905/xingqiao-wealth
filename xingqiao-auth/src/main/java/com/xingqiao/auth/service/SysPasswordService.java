package com.xingqiao.auth.service;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.xingqiao.common.core.constant.CacheConstants;
import com.xingqiao.common.core.constant.Constants;
import com.xingqiao.common.core.exception.ServiceException;
import com.xingqiao.common.redis.service.RedisService;
import com.xingqiao.common.security.utils.SecurityUtils;
import com.xingqiao.system.api.domain.SysUser;

/**
 * 登录密码方法
 * 
 * @author xingqiao
 */
@Component
public class SysPasswordService
{
    @Autowired
    private RedisService redisService;

    private int maxRetryCount = CacheConstants.PASSWORD_MAX_RETRY_COUNT;

    private Long lockTime = CacheConstants.PASSWORD_LOCK_TIME;

    @Autowired
    private SysRecordLogService recordLogService;

    /**
     * 登录账户密码错误次数缓存键名
     * 
     * @param account 用户名
     * @return 缓存键key
     */
    private String getCacheKey(String account)
    {
        return CacheConstants.PWD_ERR_CNT_KEY + account;
    }

    public void validate(String oldPassword,String account, String password)
    {

        Integer retryCount = redisService.getCacheObject(getCacheKey(account));

        if (retryCount == null)
        {
            retryCount = 0;
        }

        if (retryCount >= Integer.valueOf(maxRetryCount).intValue())
        {
            String errMsg = String.format("密码输入错误%s次，帐户锁定%s分钟", maxRetryCount, lockTime);
            recordLogService.recordLogininfor(account, Constants.LOGIN_FAIL,errMsg);
            throw new ServiceException(errMsg);
        }

        if (!matches(oldPassword, password))
        {
            retryCount = retryCount + 1;
            recordLogService.recordLogininfor(account, Constants.LOGIN_FAIL, String.format("密码输入错误%s次", retryCount));
            redisService.setCacheObject(getCacheKey(account), retryCount, lockTime, TimeUnit.MINUTES);
            throw new ServiceException("用户不存在/密码错误");
        }
        else
        {
            clearLoginRecordCache(account);
        }
    }

    public boolean matches(String password, String rawPassword)
    {
        return SecurityUtils.matchesPassword(rawPassword, password);
    }

    public void clearLoginRecordCache(String loginName)
    {
        if (redisService.hasKey(getCacheKey(loginName)))
        {
            redisService.deleteObject(getCacheKey(loginName));
        }
    }
}
