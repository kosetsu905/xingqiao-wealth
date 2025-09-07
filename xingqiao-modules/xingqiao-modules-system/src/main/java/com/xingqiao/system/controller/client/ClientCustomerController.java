
package com.xingqiao.system.controller.client;

import com.alibaba.fastjson.JSONObject;
import com.xingqiao.common.core.domain.R;
import com.xingqiao.common.core.web.controller.BaseController;
import com.xingqiao.common.security.utils.SecurityUtils;
import com.xingqiao.system.api.domain.client.CustomerKycRecordsReq;
import com.xingqiao.system.service.client.ClientCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/client/customer")
public class ClientCustomerController extends BaseController {

    @Autowired
    private ClientCustomerService clientCustomerService;

    @PostMapping("/getEkycReturnUrlDemo")
    public R getEkycReturnUrlDemo(@RequestBody  JSONObject metaInfo) throws Exception {
        return R.ok(clientCustomerService.getEkycReturnUrlDemo(metaInfo));
    }


    @PostMapping("/saveKycInfo")
    public R saveKycInfo(@RequestBody CustomerKycRecordsReq req) {
        Long userId = SecurityUtils.getUserId();
        req.setUserId(userId);
        return clientCustomerService.saveKycInfo(req);
    }


    /**
     * 获取Kyc认证信息
     */
    @GetMapping("/getKycInfo")
    public R getKycInfo() {
        try {
            Long userId = SecurityUtils.getUserId();
            CustomerKycRecordsReq response = clientCustomerService.getKycInfo(userId);
            return R.ok(response);
        } catch (Exception e) {
            logger.error("获取客户信息失败：", e);
            return R.fail("失败");
        }
    }


    @PostMapping("/getEkycReturnUrl")
    public R getEkycReturnUrl(@RequestBody  JSONObject metaInfo) throws Exception {
        Long userId = SecurityUtils.getUserId();
        return  clientCustomerService.getEkycReturnUrl(userId,metaInfo);
    }

    @GetMapping("/getEkycResult")
    public R getEkycResult() throws Exception {
        Long userId = SecurityUtils.getUserId();
        return  clientCustomerService.getEkycResult(userId);
    }

}
