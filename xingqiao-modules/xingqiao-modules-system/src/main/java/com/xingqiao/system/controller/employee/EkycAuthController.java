package com.xingqiao.system.controller.employee;

import com.xingqiao.common.core.domain.R;
import com.xingqiao.common.core.web.controller.BaseController;
import com.xingqiao.common.security.utils.SecurityUtils;
import com.xingqiao.system.api.domain.employee.AgencyEkyc;
import com.xingqiao.system.service.employee.ISysEmployeeInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/agency/ekyc")
public class EkycAuthController extends BaseController {
    @Autowired
    private ISysEmployeeInfoService sysEmployeeInfoService;

    /**
     * 提交ekyc数据
     *
     * @param ekycData ekyc数据
     * @return 提交结果
     */
    @PostMapping("/submit")
    public R<?> submitEkycData(@RequestBody AgencyEkyc ekycData) {
        Long userId = SecurityUtils.getUserId();
        ekycData.setUserId(userId);
        ekycData.setUpdateBy(SecurityUtils.getLoginUser().getUsername());
        return R.ok(sysEmployeeInfoService.submitEkycData(ekycData));
    }

    /**
     * 获取表单info数据
     *
     * @return 表单info数据
     */
    @GetMapping("/info")
    public R<?> getEkycInfo() {
        Long userId = SecurityUtils.getUserId();
        return R.ok(sysEmployeeInfoService.getEkycInfo(userId));
    }


}