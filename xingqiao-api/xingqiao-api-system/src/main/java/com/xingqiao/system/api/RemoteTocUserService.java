package com.xingqiao.system.api;

import com.xingqiao.common.core.constant.SecurityConstants;
import com.xingqiao.common.core.constant.ServiceNameConstants;
import com.xingqiao.common.core.domain.R;
import com.xingqiao.system.api.domain.CUser;
import com.xingqiao.system.api.factory.RemoteUserFallbackFactory;
import com.xingqiao.system.api.model.LoginUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 用户服务
 * 
 * @author xingqiao
 */
@FeignClient(contextId = "remoteTocUserService", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteUserFallbackFactory.class)
public interface RemoteTocUserService
{
    /**
     * 通过用户名查询用户信息
     *
     * @param account 登录账号
     * @param source 请求来源
     * @return 结果
     */
    @GetMapping("/client/user/info/{account}/{userType}")
    public R<LoginUser> getUserInfo(@PathVariable("account") String account,@PathVariable("userType") String userType, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 注册用户信息
     *
     * @param sysUser 用户信息
     * @param source 请求来源
     * @return 结果
     */
    @PostMapping("/client/user/register")
    public R<Boolean> registerUserInfo(@RequestBody CUser sysUser, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);


}
