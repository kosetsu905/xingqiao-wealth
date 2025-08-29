package com.xingqiao.system.api;

import com.xingqiao.common.core.constant.SecurityConstants;
import com.xingqiao.common.core.constant.ServiceNameConstants;
import com.xingqiao.common.core.domain.R;
import com.xingqiao.system.api.domain.CustomerQueryInnerRequest;
import com.xingqiao.system.api.domain.SysUser;
import com.xingqiao.system.api.factory.RemoteTocUserFallbackFactory;
import com.xingqiao.system.api.model.CustomerListInnerResponse;
import com.xingqiao.system.api.model.LoginUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户服务
 * 
 * @author xingqiao
 */
@FeignClient(contextId = "remoteTocUserService", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteTocUserFallbackFactory.class)
public interface RemoteTocUserService
{
    /**
     * 通过用户名查询用户信息
     *
     * @param userName 登录账号
     * @param source 请求来源
     * @return 结果
     */
    @GetMapping("/client/user/info/{userName}/{userType}")
    public R<LoginUser> getUserInfo(@PathVariable("userName") String userName,@PathVariable("userType") String userType, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 注册用户信息
     *
     * @param sysUser 用户信息
     * @param source 请求来源
     * @return 结果
     */
    @PostMapping("/client/user/register")
    public R<Boolean> registerUserInfo(@RequestBody SysUser sysUser, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);


    @PostMapping("/agency/customer/customerList")
    R<List<CustomerListInnerResponse>> getCustomerInnerList(@RequestBody  CustomerQueryInnerRequest request);

}