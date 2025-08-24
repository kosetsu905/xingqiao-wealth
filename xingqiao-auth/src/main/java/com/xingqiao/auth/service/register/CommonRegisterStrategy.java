package com.xingqiao.auth.service.register;

import com.xingqiao.auth.common.AuthConfig;
import com.xingqiao.auth.form.RegisterReqDTO;
import com.xingqiao.auth.service.ReisterStrategy;
import com.xingqiao.auth.service.SysRecordLogService;
import com.xingqiao.common.core.constant.Constants;
import com.xingqiao.common.core.constant.SecurityConstants;
import com.xingqiao.common.core.constant.UserConstants;
import com.xingqiao.common.core.domain.R;
import com.xingqiao.common.core.enums.RegistrationStep;
import com.xingqiao.common.core.exception.ServiceException;
import com.xingqiao.common.core.utils.DateUtils;
import com.xingqiao.common.core.utils.StringUtils;
import com.xingqiao.common.redis.service.RedisService;
import com.xingqiao.common.security.utils.SecurityUtils;
import com.xingqiao.system.api.RemoteTocUserService;
import com.xingqiao.system.api.domain.SysUser;
import com.xingqiao.system.api.enums.RegisterType;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class CommonRegisterStrategy implements ReisterStrategy {
    @Autowired
    private RedisService redisService;
    @Autowired
    private SysRecordLogService recordLogService;

    @Resource
    private RemoteTocUserService remoteUserService;
    @Resource
    private AuthConfig authConfig;

    @Override
    public Map<String, Object> register(RegisterReqDTO request) {
        // 用户名或密码为空 错误
        if (StringUtils.isAnyBlank(request.getEmail(),
                request.getPhoneNumber(),
                request.getPassword(), request.getSendCode()))
        {
            throw new ServiceException("邮箱/手机号/密码/发送验证码必须填写");
        }

        if (request.getPassword().length() < UserConstants.PASSWORD_MIN_LENGTH
                || request.getPassword().length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            throw new ServiceException("密码长度必须在5到20个字符之间");
        }

        // 校验redis里面的验证码,或者配置后门
        // 满足以下任一条件即可通过验证：
        // 1. 邮箱验证码正确
        // 2. 手机验证码正确
        // 3. 后门验证码(mockCode)正确
        boolean checkCode = validateVerificationCode(request);
        if (!checkCode) {
            throw new ServiceException("验证码不正确！");
        }

        // 注册用户信息
        SysUser cUser = new SysUser();
        cUser.setCountryCode(request.getCountryCode());
        cUser.setPhoneNumber(request.getPhoneNumber());
        cUser.setUserType(request.getUserType());
        cUser.setUserName(request.getUserName());
        cUser.setEmail(request.getEmail());
        cUser.setNickName(request.getNickName());
        cUser.setPwdUpdateDate(DateUtils.getNowDate());
        cUser.setPassword(SecurityUtils.encryptPassword(request.getPassword()));
        R<?> registerResult = remoteUserService.registerUserInfo(cUser, SecurityConstants.INNER);

        if (R.FAIL == registerResult.getCode())
        {
            throw new ServiceException(registerResult.getMsg());
        }
        recordLogService.recordLogininfor(request.getPhoneNumber(), Constants.REGISTER, "注册成功");
        return Collections.emptyMap();
    }

    /**
     * 验证注册验证码
     * 满足以下任一条件即可通过验证：
     * 1. 邮箱验证码正确
     * 2. 手机验证码正确
     * 3. 后门验证码(mockCode)正确
     *
     * @param request 注册请求
     * @return 验证结果
     */
    private boolean validateVerificationCode(RegisterReqDTO request) {
        // 检查邮箱验证码
        String emailCodeKey = Constants.CODE_KEY + RegistrationStep.EMAIL_VERIFICATION_CODE.getCode() + ":" + request.getEmail();
        String emailCode = redisService.getCacheObject(emailCodeKey);
        if (StringUtils.equals(request.getSendCode(), emailCode)) {
            return true;
        }

        // 检查手机验证码
        String phoneCodeKey = Constants.CODE_KEY + RegistrationStep.PHONE_VERIFICATION_CODE.getCode() + ":" + request.getPhoneNumber();
        String phoneCode = redisService.getCacheObject(phoneCodeKey);
        if (StringUtils.equals(request.getSendCode(), phoneCode)) {
            return true;
        }

        // 检查后门验证码
        if (StringUtils.isNotEmpty(authConfig.getMockCode())&&StringUtils.equals(request.getSendCode(), authConfig.getMockCode())) {
            return true;
        }

        return false;
    }

    @Override
    public boolean supports(RegisterType type) {
        return RegisterType.COMMON== type;
    }
}