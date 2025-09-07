package com.xingqiao.system.service.client;

import com.alibaba.fastjson.JSONObject;
import com.xingqiao.common.core.domain.R;
import com.xingqiao.system.api.domain.client.ClientCustomerReq;
import com.xingqiao.system.api.domain.client.ClientCustomerResp;
import com.xingqiao.system.api.domain.client.CustomerKycRecordsReq;

public interface ClientCustomerService {

    R saveKycInfo(CustomerKycRecordsReq req);

    CustomerKycRecordsReq getKycInfo(Long userId);

    R getEkycReturnUrl(Long userId, JSONObject metaInfo);

    R getEkycResult(Long userId);

    R getClientCustomerInfo(Long userId);

    R saveClientCustomerInfo(ClientCustomerReq req);
}
