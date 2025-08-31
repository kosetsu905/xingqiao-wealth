package com.xingqiao.system.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.cloudauth20190307.Client;
import com.aliyun.cloudauth20190307.models.InitFaceVerifyRequest;
import com.aliyun.cloudauth20190307.models.InitFaceVerifyResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.xingqiao.system.service.ClientCustomerService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;


@Service
public class ClientCustomerServiceImpl implements ClientCustomerService {

    private static com.aliyun.credentials.Client credentialClient = new com.aliyun.credentials.Client();

    @Override
    public String getEkycReturnUrl(JSONObject metaInfo) {


//        InitFaceVerifyRequest request = new InitFaceVerifyRequest();
//        // 场景ID+L。
//        request.setSceneId(1000014670L);
//        // 设置商户请求的唯一标识。
//        request.setOuterOrderNo("2025083017240001");
//        // 认证方案。
//        request.setProductCode("ID_PRO");
//        // 模式。
//        request.setModel("LIVENESS");
//        request.setCertType("IDENTITY_CARD");
//        request.setCertName("覃冠木");
//        request.setCertNo("450802198906072016");
//        // MetaInfo环境参数，此参数应由前端js获取并传入。
//        request.setMetaInfo(metaInfo.toJSONString());
//        //业务页面回跳的目标地址。
//        request.setReturnUrl("https://www.aliyundoc.com");
//
//        InitFaceVerifyResponse response = initFaceVerifyAutoRoute(request);
//
//        response.getBody().getRequestId();
//        response.getBody().getResultObject().getCertifyId();
//        System.out.println(response.getBody().getRequestId());
//        System.out.println(response.getBody().getCode());
//        System.out.println(response.getBody().getMessage());
//        System.out.println(response.getBody().getResultObject() == null ? null
//                : response.getBody().getResultObject().getCertifyId());
//        return response.getBody().getResultObject().getCertifyUrl();

        return "";
    }

    /**
     * <b>description</b> :
     * <p>使用凭据初始化账号Client</p>
     * @return Client
     *
     * @throws Exception
     */
    public static com.aliyun.cloudauth20190307.Client createClient() throws Exception {
        // 工程代码建议使用更安全的无AK方式，凭据配置方式请参见：https://help.aliyun.com/document_detail/378657.html。
        com.aliyun.credentials.Client credential = new com.aliyun.credentials.Client();
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                .setCredential(credential);
        // Endpoint 请参考 https://api.aliyun.com/product/Cloudauth
        config.endpoint = "cloudauth.aliyuncs.com";
        return new com.aliyun.cloudauth20190307.Client(config);
    }
}
