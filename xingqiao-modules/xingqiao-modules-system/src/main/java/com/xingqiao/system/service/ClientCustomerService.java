package com.xingqiao.system.service;

import com.alibaba.fastjson.JSONObject;

public interface ClientCustomerService {
    String getEkycReturnUrl(JSONObject metaInfo) throws Exception;
}
