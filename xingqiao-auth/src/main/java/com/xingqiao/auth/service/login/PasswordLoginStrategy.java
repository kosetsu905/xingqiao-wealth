package com.xingqiao.auth.service.login;

import com.xingqiao.auth.form.LoginReqDTO;
import com.xingqiao.auth.service.LoginStrategy;
import com.xingqiao.auth.service.SysPasswordService;
import com.xingqiao.common.core.constant.CacheConstants;
import com.xingqiao.common.core.constant.SecurityConstants;
import com.xingqiao.common.core.constant.UserConstants;
import com.xingqiao.common.core.domain.R;
import com.xingqiao.common.core.enums.UserStatus;
import com.xingqiao.common.core.exception.ServiceException;
import com.xingqiao.common.core.text.Convert;
import com.xingqiao.common.core.utils.StringUtils;
import com.xingqiao.common.core.utils.ip.IpUtils;
import com.xingqiao.common.redis.service.RedisService;
import com.xingqiao.common.security.service.TokenService;
import com.xingqiao.system.api.RemoteTocUserService;
import com.xingqiao.system.api.domain.SysUser;
import com.xingqiao.system.api.enums.LoginType;
import com.xingqiao.system.api.model.LoginUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class PasswordLoginStrategy implements LoginStrategy {
    private static final Logger log = LoggerFactory.getLogger(PasswordLoginStrategy.class);
    @Resource
    private RemoteTocUserService remoteTocUserService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private SysPasswordService passwordService;
    @Autowired
    private TokenService tokenService;
    @Override
    public Map<String, Object> login(LoginReqDTO request) {
        // 用户名或密码为空 错误
        if (StringUtils.isAnyBlank(request.getUserName(), request.getPassword()))
        {
            log.warn("账号/密码必须填写");
            throw new ServiceException("账号/密码必须填写");
        }
        // 密码如果不在指定范围内 错误
        if (request.getPassword().length() < UserConstants.PASSWORD_MIN_LENGTH
                || request.getPassword().length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            log.warn("密码长度必须在5到20个字符之间");
            throw new ServiceException("用户密码不在指定范围");
        }
        // 账号不在指定范围内 错误
        if (request.getUserName().length() < UserConstants.USERNAME_MIN_LENGTH
                || request.getUserName().length() > UserConstants.USERNAME_MAX_LENGTH)
        {
           log.warn("账号不在指定范围");
            throw new ServiceException("账号不在指定范围");
        }
        // IP黑名单校验
        String blackStr = Convert.toStr(redisService.getCacheObject(CacheConstants.CLIENT_LOGIN_BLACKIPLIST));
        if (IpUtils.isMatchedIp(blackStr, IpUtils.getIpAddr()))
        {
            log.warn("访问IP已被列入系统黑名单");
            throw new ServiceException("很遗憾，访问IP已被列入系统黑名单");
        }
        // 查询用户信息
        R<LoginUser> userResult = remoteTocUserService.getUserInfo(request.getUserName(),request.getUserType(), SecurityConstants.INNER);

        if (R.FAIL == userResult.getCode())
        {
            throw new ServiceException(userResult.getMsg());
        }

        LoginUser userInfo = userResult.getData();
        SysUser user = userResult.getData().getUser();
        if (UserStatus.DELETED.getCode().equals(user.getDelFlag()))
        {
            throw new ServiceException("对不起，您的账号：" + request.getUserName() + " 已被删除");
        }
        if (UserStatus.DISABLE.getCode().equals(user.getStatus()))
        {
            throw new ServiceException("对不起，您的账号：" + request.getUserName() + " 已停用");
        }
        passwordService.validate(user.getPassword(),user.getUserName(), request.getPassword());

        return tokenService.createToken(userInfo);
    }



    @Override
    public boolean supports(LoginType type) {
        return LoginType.PASSWORD == type;
    }
}
