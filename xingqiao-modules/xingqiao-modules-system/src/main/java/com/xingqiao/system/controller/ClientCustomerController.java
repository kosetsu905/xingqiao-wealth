
package com.xingqiao.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.xingqiao.common.core.domain.R;
import com.xingqiao.common.core.web.controller.BaseController;

import com.xingqiao.system.service.ClientCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/client/customer")
public class ClientCustomerController extends BaseController {

    @Autowired
    private ClientCustomerService clientCustomerService;

    @PostMapping("/getEkycReturnUrl")
    public R getEkycReturnUrl(@RequestBody  JSONObject metaInfo) {
        return R.ok(clientCustomerService.getEkycReturnUrl(metaInfo));
    }


}
