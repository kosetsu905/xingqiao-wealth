package com.xingqiao.system.api.factory;

import com.xingqiao.common.core.domain.R;
import com.xingqiao.system.api.RemoteTocUserService;
import com.xingqiao.system.api.domain.SysUser;
import com.xingqiao.system.api.model.LoginUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 用户服务降级处理
 * 
 * @author xingqiao
 */
@Component
public class RemoteTocUserFallbackFactory implements FallbackFactory<RemoteTocUserService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteTocUserFallbackFactory.class);

    @Override
    public RemoteTocUserService create(Throwable throwable) {
        log.error("用户服务调用失败:{}", throwable.getMessage());
        return new RemoteTocUserService() {
            @Override
            public R<LoginUser> getUserInfo(String userName, String userType, String source) {
                return R.fail("获取用户失败:" + throwable.getMessage());
            }

            @Override
            public R<Boolean> registerUserInfo(SysUser sysUser, String source) {
                return R.fail("注册用户失败:" + throwable.getMessage());
            }
        };
    }
}