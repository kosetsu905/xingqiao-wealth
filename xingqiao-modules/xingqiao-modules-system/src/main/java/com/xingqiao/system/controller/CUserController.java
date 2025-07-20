package com.xingqiao.system.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import com.xingqiao.common.core.domain.R;
import com.xingqiao.common.core.utils.StringUtils;
import com.xingqiao.common.security.annotation.InnerAuth;
import com.xingqiao.system.api.domain.CUser;
import com.xingqiao.system.api.domain.CommonUser;
import com.xingqiao.system.api.model.LoginUser;
import com.xingqiao.system.service.ISysConfigService;
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
import com.xingqiao.system.service.ICUserService;
import com.xingqiao.common.core.web.controller.BaseController;
import com.xingqiao.common.core.web.domain.AjaxResult;
import com.xingqiao.common.core.utils.poi.ExcelUtil;
import com.xingqiao.common.core.web.page.TableDataInfo;

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
    private ICUserService cUserService;

    @Autowired
    private ISysConfigService configService;
    /**
     * 查询客户信息列表
     */
    @RequiresPermissions("system:user:list")
    @GetMapping("/list")
    public TableDataInfo list(CUser cUser)
    {
        startPage();
        List<CUser> list = cUserService.selectCUserList(cUser);
        return getDataTable(list);
    }

    /**
     * 导出客户信息列表
     */
    @RequiresPermissions("system:user:export")
    @Log(title = "客户信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CUser cUser)
    {
        List<CUser> list = cUserService.selectCUserList(cUser);
        ExcelUtil<CUser> util = new ExcelUtil<CUser>(CUser.class);
        util.exportExcel(response, list, "客户信息数据");
    }

    /**
     * 获取客户信息详细信息
     */
    @RequiresPermissions("system:user:query")
    @GetMapping(value = "/{userId}")
    public AjaxResult getInfo(@PathVariable("userId") Long userId)
    {
        return success(cUserService.selectCUserByUserId(userId));
    }

    /**
     * 新增客户信息
     */
    @RequiresPermissions("system:user:add")
    @Log(title = "客户信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CUser cUser)
    {
        return toAjax(cUserService.insertCUser(cUser));
    }

    /**
     * 修改客户信息
     */
    @RequiresPermissions("system:user:edit")
    @Log(title = "客户信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CUser cUser)
    {
        return toAjax(cUserService.updateCUser(cUser));
    }

    /**
     * 删除客户信息
     */
    @RequiresPermissions("system:user:remove")
    @Log(title = "客户信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds)
    {
        return toAjax(cUserService.deleteCUserByUserIds(userIds));
    }


    /**
     * 注册用户信息
     */
    @InnerAuth
    @PostMapping("/register")
    public R<Boolean> register(@RequestBody CUser user)
    {
        String account = user.getAccount();
        if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser"))))
        {
            return R.fail("当前系统没有开启注册功能！");
        }
        if (!cUserService.checkAccountUnique(user))
        {
            return R.fail("保存用户'" + account + "'失败，注册账号已存在");
        }
        if (StringUtils.isNotEmpty(user.getPhoneNumber()) && !cUserService.checkPhoneUnique(user))
        {
            return R.fail("新增用户'" + user.getPhoneNumber() + "'失败，手机号码已存在");
        }
        else if (StringUtils.isNotEmpty(user.getEmail()) && !cUserService.checkEmailUnique(user))
        {
            return R.fail("新增用户'" + user.getEmail() + "'失败，邮箱账号已存在");
        }
        return R.ok(cUserService.insertCUser(user)>0);

    }
    /**
     * 获取当前用户信息
     */
    @InnerAuth
    @GetMapping("/info/{account}/{userType}")
    public R<LoginUser> info(@PathVariable("account") String account,@PathVariable("userType") String userType)
    {
        CommonUser commonUser = cUserService.selectCUserByAccount(account,userType);
        if (StringUtils.isNull(commonUser))
        {
            return R.fail("用户不存在！");
        }
        LoginUser sysUserVo = new LoginUser();
        sysUserVo.setUser(commonUser);
        return R.ok(sysUserVo);
    }



}
