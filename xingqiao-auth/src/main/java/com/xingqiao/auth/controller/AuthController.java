package com.xingqiao.auth.controller;


import com.xingqiao.auth.common.LoginContext;
import com.xingqiao.auth.common.RegisterContext;
import com.xingqiao.auth.form.CodeReqDTO;
import com.xingqiao.auth.form.LoginReqDTO;
import com.xingqiao.auth.form.RegisterReqDTO;
import com.xingqiao.auth.service.SysLoginService;
import com.xingqiao.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * token 控制
 * 
 * @author xingqiao
 */
@RestController
@RequestMapping("/client")
public class AuthController
{
    @Resource
    private LoginContext loginContext;
    @Resource
    private RegisterContext registerContext;
    @Autowired
    private SysLoginService sysLoginService;
    @PostMapping("/login")
    public R<?> login(@RequestBody @Valid LoginReqDTO request) {
        return R.ok(loginContext.executeLogin(request));
    }

    @PostMapping("/register")
    public R<?> register(@RequestBody @Valid RegisterReqDTO request) {
        return R.ok(registerContext.executeRegister(request));
    }

    /**
     * 发验证码接口
     */
    @PostMapping("/sendCode")
    public R<?> sendCode(@RequestBody CodeReqDTO codeReqDTO) {
        return R.ok(sysLoginService.sendCode(codeReqDTO));
    }
}
