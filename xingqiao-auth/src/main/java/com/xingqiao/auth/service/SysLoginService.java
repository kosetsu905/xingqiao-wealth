package com.xingqiao.auth.service;

import com.xingqiao.auth.form.CodeReqDTO;
import com.xingqiao.auth.service.login.PasswordLoginStrategy;
import com.xingqiao.common.core.enums.RegistrationStep;
import com.xingqiao.common.core.utils.RandomUtil;
import com.xingqiao.system.api.RemoteEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.xingqiao.common.core.constant.CacheConstants;
import com.xingqiao.common.core.constant.Constants;
import com.xingqiao.common.core.constant.SecurityConstants;
import com.xingqiao.common.core.constant.UserConstants;
import com.xingqiao.common.core.domain.R;
import com.xingqiao.common.core.enums.UserStatus;
import com.xingqiao.common.core.exception.ServiceException;
import com.xingqiao.common.core.text.Convert;
import com.xingqiao.common.core.utils.DateUtils;
import com.xingqiao.common.core.utils.StringUtils;
import com.xingqiao.common.core.utils.ip.IpUtils;
import com.xingqiao.common.redis.service.RedisService;
import com.xingqiao.common.security.utils.SecurityUtils;
import com.xingqiao.system.api.RemoteUserService;
import com.xingqiao.system.api.domain.SysUser;
import com.xingqiao.system.api.model.LoginUser;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 登录校验方法
 * 
 * @author xingqiao
 */
@Component
public class SysLoginService
{
    private static final Logger log = LoggerFactory.getLogger(SysLoginService.class);

    @Resource
    private RemoteUserService remoteUserService;

    @Autowired
    private SysPasswordService passwordService;

    @Autowired
    private SysRecordLogService recordLogService;

    @Autowired
    private RedisService redisService;

    @Resource
    private RemoteEmailService remoteEmailService;

    /**
     * 登录
     */
    public LoginUser login(String username, String password)
    {
        // 用户名或密码为空 错误
        if (StringUtils.isAnyBlank(username, password))
        {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, "用户/密码必须填写");
            throw new ServiceException("用户/密码必须填写");
        }
        // 密码如果不在指定范围内 错误
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, "用户密码不在指定范围");
            throw new ServiceException("用户密码不在指定范围");
        }
        // 用户名不在指定范围内 错误
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH)
        {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, "用户名不在指定范围");
            throw new ServiceException("用户名不在指定范围");
        }
        // IP黑名单校验
        String blackStr = Convert.toStr(redisService.getCacheObject(CacheConstants.SYS_LOGIN_BLACKIPLIST));
        if (IpUtils.isMatchedIp(blackStr, IpUtils.getIpAddr()))
        {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, "很遗憾，访问IP已被列入系统黑名单");
            throw new ServiceException("很遗憾，访问IP已被列入系统黑名单");
        }
        // 查询用户信息
        R<LoginUser> userResult = remoteUserService.getUserInfo(username, SecurityConstants.INNER);

        if (R.FAIL == userResult.getCode())
        {
            throw new ServiceException(userResult.getMsg());
        }

        LoginUser userInfo = userResult.getData();

        SysUser commonUser = userResult.getData().getUser();
        SysUser user = new SysUser();
        BeanUtils.copyProperties(commonUser, user);

        if (UserStatus.DELETED.getCode().equals(user.getDelFlag()))
        {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, "对不起，您的账号已被删除");
            throw new ServiceException("对不起，您的账号：" + username + " 已被删除");
        }
        if (UserStatus.DISABLE.getCode().equals(user.getStatus()))
        {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, "用户已停用，请联系管理员");
            throw new ServiceException("对不起，您的账号：" + username + " 已停用");
        }
        passwordService.validate(user.getPassword(),user.getUserName(), password);
        recordLogService.recordLogininfor(username, Constants.LOGIN_SUCCESS, "登录成功");
        recordLoginInfo(user.getUserId());
        return userInfo;
    }

    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    public void recordLoginInfo(Long userId)
    {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        // 更新用户登录IP
        sysUser.setLoginIp(IpUtils.getIpAddr());
        // 更新用户登录时间
        sysUser.setLoginDate(DateUtils.getNowDate());
        remoteUserService.recordUserLogin(sysUser, SecurityConstants.INNER);
    }

    public void logout(String loginName)
    {
        recordLogService.recordLogininfor(loginName, Constants.LOGOUT, "退出成功");
    }

    /**
     * 注册
     */
    public void register(String username, String password)
    {
        // 用户名或密码为空 错误
        if (StringUtils.isAnyBlank(username, password))
        {
            throw new ServiceException("用户/密码必须填写");
        }
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH)
        {
            throw new ServiceException("账户长度必须在2到20个字符之间");
        }
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            throw new ServiceException("密码长度必须在5到20个字符之间");
        }

        // 注册用户信息
        SysUser sysUser = new SysUser();
        sysUser.setUserName(username);
        sysUser.setNickName(username);
        sysUser.setPwdUpdateDate(DateUtils.getNowDate());
        sysUser.setPassword(SecurityUtils.encryptPassword(password));
        R<?> registerResult = remoteUserService.registerUserInfo(sysUser, SecurityConstants.INNER);

        if (R.FAIL == registerResult.getCode())
        {
            throw new ServiceException(registerResult.getMsg());
        }
        recordLogService.recordLogininfor(username, Constants.REGISTER, "注册成功");
    }

    public Map<String, Object> sendCode(CodeReqDTO codeReqDTO) {
        //生成随机的6位数验证码
        String code = RandomUtil.randomNumbers(6);
        //判断验证码类型
        if (RegistrationStep.PHONE_VERIFICATION_CODE.getCode().equals(codeReqDTO.getStep())) {
            //发送手机验证码
            //发送成功保存到redis中，设置有效时间
            redisService.setCacheObject(Constants.CODE_KEY +
                            codeReqDTO.getStep()+":"+codeReqDTO.getPhoneNumber(),
                    code, Constants.CODE_TTL, TimeUnit.SECONDS);
        }

        if (RegistrationStep.EMAIL_VERIFICATION_CODE.getCode().equals(codeReqDTO.getStep())) {
            log.info("发送邮箱验证码,code={}",code);
            //发送邮箱验证码
            String text=String.format("【财富管理】您的注册验证码是：%s",code);
            remoteEmailService.sendSimpleEmail(codeReqDTO.getEmail(),"注册验证码！",text, SecurityConstants.INNER);
            //发送成功保存到redis中，设置有效时间
            redisService.setCacheObject(Constants.CODE_KEY +
                            codeReqDTO.getStep()+":"+codeReqDTO.getEmail(),
                    code, Constants.CODE_TTL, TimeUnit.SECONDS);
            log.info("发送邮箱验证码成功");
        }
        return Collections.emptyMap();
    }
}
