package com.xingqiao.system.controller;

import com.xingqiao.common.core.domain.R;
import com.xingqiao.common.core.utils.StringUtils;
import com.xingqiao.common.security.annotation.InnerAuth;
import com.xingqiao.system.api.domain.SysUser;
import com.xingqiao.system.api.model.LoginUser;
import com.xingqiao.system.service.ISysConfigService;
import com.xingqiao.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.xingqiao.common.log.annotation.Log;
import com.xingqiao.common.log.enums.BusinessType;
import com.xingqiao.common.security.annotation.RequiresPermissions;
import com.xingqiao.common.core.web.controller.BaseController;
import com.xingqiao.common.core.web.domain.AjaxResult;

/**
 * 客户信息Controller
 *
 * @author xingqiao
 * @date 2025-07-19
 */
@RestController
@RequestMapping("/client/user")
public class CUserController extends BaseController
{
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysConfigService configService;


    /**
     * 新增客户信息
     */
    @RequiresPermissions("system:user:add")
    @Log(title = "客户信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysUser cUser)
    {
        return toAjax(userService.insertUser(cUser));
    }

    /**
     * 修改客户信息
     */
    @RequiresPermissions("system:user:edit")
    @Log(title = "客户信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysUser cUser)
    {
        return toAjax(userService.updateUser(cUser));
    }

    /**
     * 删除客户信息
     */
    @RequiresPermissions("system:user:remove")
    @Log(title = "客户信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds)
    {
        return toAjax(userService.deleteCUserByUserIds(userIds));
    }


    /**
     * 注册用户信息
     */
    @InnerAuth
    @PostMapping("/register")
    public R<Boolean> register(@RequestBody SysUser user)
    {
        String account = user.getUserName();
        if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser"))))
        {
            return R.fail("当前系统没有开启注册功能！");
        }
        if (!userService.checkUserNameUnique(user))
        {
            return R.fail("保存用户'" + account + "'失败，注册账号已存在");
        }
        if (StringUtils.isNotEmpty(user.getPhoneNumber()) && !userService.checkPhoneUnique(user))
        {
            return R.fail("新增用户'" + user.getPhoneNumber() + "'失败，手机号码已存在");
        }
        else if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(user))
        {
            return R.fail("新增用户'" + user.getEmail() + "'失败，邮箱账号已存在");
        }
        return R.ok(userService.insertUser(user)>0);

    }
    /**
     * 获取当前用户信息
     */
    @InnerAuth
    @GetMapping("/info/{userName}/{userType}")
    public R<LoginUser> info(@PathVariable("userName") String userName,@PathVariable("userType") String userType)
    {
        SysUser commonUser = userService.selectUserByUserName(userName,userType);
        if (StringUtils.isNull(commonUser))
        {
            return R.fail("用户不存在！");
        }
        LoginUser sysUserVo = new LoginUser();
        sysUserVo.setUser(commonUser);
        return R.ok(sysUserVo);
    }



    /**
     * 获取当前用户信息
     */
    @InnerAuth
    @GetMapping("/infoByEmail/{email}/{userType}")
    public R<LoginUser> infoByEmail(@PathVariable("email") String email
            ,@PathVariable("userType") String userType)
    {
        SysUser commonUser = userService.selectUserByEmail(email,userType);
        if (StringUtils.isNull(commonUser))
        {
            return R.fail("用户不存在！");
        }
        LoginUser sysUserVo = new LoginUser();
        sysUserVo.setUser(commonUser);
        return R.ok(sysUserVo);
    }

    /**
     * 获取当前用户信息
     */
    @InnerAuth
    @GetMapping("/infoByPhone/{phone}/{userType}")
    public R<LoginUser> infoByPhone(@PathVariable("phone") String phone
            ,@PathVariable("userType") String userType)
    {
        SysUser commonUser = userService.selectUserByPhone(phone,userType);
        if (StringUtils.isNull(commonUser))
        {
            return R.fail("用户不存在！");
        }
        LoginUser sysUserVo = new LoginUser();
        sysUserVo.setUser(commonUser);
        return R.ok(sysUserVo);
    }



}
