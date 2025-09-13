package com.xingqiao.auth.service.login;

import com.xingqiao.auth.common.AuthConfig;
import com.xingqiao.auth.form.LoginReqDTO;
import com.xingqiao.auth.form.RegisterReqDTO;
import com.xingqiao.auth.service.LoginStrategy;
import com.xingqiao.auth.service.SysPasswordService;
import com.xingqiao.auth.utils.EmailValidator;
import com.xingqiao.common.core.constant.CacheConstants;
import com.xingqiao.common.core.constant.Constants;
import com.xingqiao.common.core.constant.SecurityConstants;
import com.xingqiao.common.core.constant.UserConstants;
import com.xingqiao.common.core.domain.R;
import com.xingqiao.common.core.enums.RegistrationStep;
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
import java.util.Map;
import com.xingqiao.system.api.model.LoginUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

@Component
public class EmailVerificationLoginStrategy implements LoginStrategy {
    private static final Logger log = LoggerFactory.getLogger(EmailVerificationLoginStrategy.class);

    @Resource
    private RemoteTocUserService remoteTocUserService;
    @Resource
    private RedisService redisService;
    @Resource
    private SysPasswordService passwordService;
    @Resource
    private TokenService tokenService;
    @Resource
    private AuthConfig authConfig;

    @Override
    public Map<String, Object> login(LoginReqDTO request) {
        // 用户名或密码为空 错误
        if (StringUtils.isAnyBlank(request.getUserName(), request.getCode()))
        {
            log.warn("邮箱/验证码必须填写");
            throw new ServiceException("邮箱/验证码必须填写");
        }
        //判断校验邮箱格式
        if (!EmailValidator.isValid(request.getUserName())){
            log.warn("邮箱格式不正确");
            throw new ServiceException("邮箱格式不正确");
        }

        // IP黑名单校验
        String blackStr = Convert.toStr(redisService.getCacheObject(CacheConstants.CLIENT_LOGIN_BLACKIPLIST));
        if (IpUtils.isMatchedIp(blackStr, IpUtils.getIpAddr()))
        {
            log.warn("访问IP已被列入系统黑名单");
            throw new ServiceException("很遗憾，访问IP已被列入系统黑名单");
        }
        // 查询用户信息
        R<LoginUser> userResult = remoteTocUserService.getUserInfoByEmail(request.getUserName(),request.getUserType(), SecurityConstants.INNER);

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
        boolean checkCode = validateVerificationCode(request);
        if (!checkCode) {
            throw new ServiceException("邮箱验证码不正确！");
        }

        return tokenService.createToken(userInfo);
    }

    @Override
    public boolean supports(LoginType type) {
        return LoginType.EMAIL_VERIFICATION == type;
    }


    private boolean validateVerificationCode(LoginReqDTO request) {
        // 检查邮箱验证码
        String emailCodeKey = Constants.CODE_KEY + RegistrationStep.EMAIL_LOGIN_VERIFICATION_CODE.getCode() + ":" + request.getUserName();
        String emailCode = redisService.getCacheObject(emailCodeKey);
        if (StringUtils.equals(request.getCode(), emailCode)) {
            return true;
        }

        // 检查后门验证码
        if (StringUtils.isNotEmpty(authConfig.getMockCode())&&StringUtils.equals(request.getCode(),
                authConfig.getMockCode())) {
            return true;
        }

        return false;
    }
}
