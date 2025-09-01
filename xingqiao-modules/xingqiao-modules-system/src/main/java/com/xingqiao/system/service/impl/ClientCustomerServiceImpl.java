package com.xingqiao.system.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xingqiao.system.service.ClientCustomerService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ClientCustomerServiceImpl implements ClientCustomerService {

    // 使用单例模式优化性能
    private static com.aliyun.credentials.Client credentialClient = new com.aliyun.credentials.Client();

    @Override
    public String getEkycReturnUrlDemo(JSONObject metaInfo) throws Exception {

        com.aliyun.teaopenapi.Client client = createClient();
        com.aliyun.teaopenapi.models.Params params = createApiInfo();
        // query params
        java.util.Map<String, Object> queries = new java.util.HashMap<>();
        queries.put("SceneId", 1000014670);
        queries.put("OuterOrderNo", "2025083117240001");
        queries.put("ProductCode", "PV_FV");
        queries.put("Model", "MOVE_ACTION");
        queries.put("CertType", "IDENTITY_CARD");
        queries.put("CertName", "覃冠木");
        queries.put("CertNo", "450802198906072016");
        queries.put("ReturnUrl", "http://www.baidu.com");
        queries.put("Mobile", "17665319189");
        queries.put("MetaInfo", metaInfo.toJSONString());
        queries.put("Ip", null);
        queries.put("UserId", "1");
        queries.put("OssBucketName", "cn-shenzhen-aliyun-cloudauth-2025081614336153");
        queries.put("OssObjectName", "ekyc/微信图片_2025-08-31_202051_864.jpg");
        // body params
        java.util.Map<String, Object> body = new java.util.HashMap<>();
        // runtime options
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        com.aliyun.teaopenapi.models.OpenApiRequest request = new com.aliyun.teaopenapi.models.OpenApiRequest()
                .setQuery(com.aliyun.openapiutil.Client.query(queries))
                .setBody(body);
        // 复制代码运行请自行打印 API 的返回值
        // 返回值实际为 Map 类型，可从 Map 中获得三类数据：响应体 body、响应头 headers、HTTP 返回的状态码 statusCode。
        Object resp = client.callApi(params, request, runtime);
        com.aliyun.teaconsole.Client.log(com.aliyun.teautil.Common.toJSONString(resp));

        // 解析返回结果，提取CertifyUrl
        if (resp != null) {
            Map<String, Object> responseMap = (Map<String, Object>) resp;
            if (responseMap.containsKey("body")) {
                Map<String, Object> bodyMap = (Map<String, Object>) responseMap.get("body");
                if (bodyMap.containsKey("ResultObject")) {
                    Map<String, Object> resultObject = (Map<String, Object>) bodyMap.get("ResultObject");
                    if (resultObject.containsKey("CertifyUrl")) {
                        return (String) resultObject.get("CertifyUrl");
                    }
                }
            }
        }
        
        return "";
    }


    /**
     * <b>description</b> :
     * <p>API 相关</p>
     *
     * @return OpenApi.Params
     */
    public static com.aliyun.teaopenapi.models.Params createApiInfo() throws Exception {
        com.aliyun.teaopenapi.models.Params params = new com.aliyun.teaopenapi.models.Params()
                // 接口名称
                .setAction("InitFaceVerify")
                // 接口版本
                .setVersion("2019-03-07")
                // 接口协议
                .setProtocol("HTTPS")
                // 接口 HTTP 方法
                .setMethod("POST")
                .setAuthType("AK")
                .setStyle("RPC")
                // 接口 PATH
                .setPathname("/")
                // 接口请求体内容格式
                .setReqBodyType("formData")
                // 接口响应体内容格式
                .setBodyType("json");
        return params;
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
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                .setCredential(credentialClient);
        // Endpoint 请参考 https://api.aliyun.com/product/Cloudauth
                // 您的AccessKey ID
        config.setAccessKeyId("LTAI5tJZnKc3VN96FeVvDUbw")
                // 您的AccessKey Secret
                .setAccessKeySecret("gu4tSpLB9utFEZouYoKRwX4OfqKwQT");
        // 访问的域名
        config.endpoint = "cloudauth.aliyuncs.com";

        return new com.aliyun.cloudauth20190307.Client(config);
    }
}
