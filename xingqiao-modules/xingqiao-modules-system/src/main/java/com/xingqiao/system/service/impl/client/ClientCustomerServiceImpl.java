package com.xingqiao.system.service.impl.client;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.teaconsole.Client;
import com.aliyun.teaopenapi.models.OpenApiRequest;
import com.aliyun.teaopenapi.models.Params;
import com.aliyun.teautil.Common;
import com.aliyun.teautil.models.RuntimeOptions;
import com.xingqiao.common.core.domain.R;
import com.xingqiao.common.core.utils.IdCardAgeCalculator;
import com.xingqiao.common.core.utils.StringUtils;
import com.xingqiao.common.core.utils.bean.BeanUtils;
import com.xingqiao.system.api.domain.client.ClientCustomerReq;
import com.xingqiao.system.api.domain.client.ClientCustomerResp;
import com.xingqiao.system.api.domain.client.CustomerKycRecordsReq;
import com.xingqiao.system.config.ClientKycConfig;
import com.xingqiao.system.domain.client.CustomerInfo;
import com.xingqiao.system.service.client.ClientCustomerService;
import com.xingqiao.system.service.client.ICustomerInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ClientCustomerServiceImpl implements ClientCustomerService {

    @Autowired
    private ICustomerInfoService iCustomerInfoService;
    @Autowired
    private  ClientKycConfig clientKycConfig;

    // 使用单例模式优化性能
    private static final com.aliyun.credentials.Client credentialClient = new com.aliyun.credentials.Client();

    /**
     * <b>description</b> :
     * <p>API 相关</p>
     *
     * @return OpenApi.Params
     */
    public static com.aliyun.teaopenapi.models.Params createApiInfo(String actionName, String reqBodyType) throws Exception {
        // 接口名称
        // 接口版本
        // 接口协议
        // 接口 HTTP 方法
        // 接口 PATH
        // 接口请求体内容格式
        // 接口响应体内容格式
        return new Params()
                // 接口名称
                .setAction(actionName)
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
                .setReqBodyType(reqBodyType)
                // 接口响应体内容格式
                .setBodyType("json");
    }


    /**
     * <b>description</b> :
     * <p>使用凭据初始化账号Client</p>
     * @return Client
     *
     * @throws Exception
     */
    public  com.aliyun.cloudauth20190307.Client createClient() throws Exception {
        // 工程代码建议使用更安全的无AK方式，凭据配置方式请参见：https://help.aliyun.com/document_detail/378657.html。
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                .setCredential(credentialClient);
        // Endpoint 请参考 https://api.aliyun.com/product/Cloudauth
                // 您的AccessKey ID
        config.setAccessKeyId(clientKycConfig.getAccessKeyId())
                // 您的AccessKey Secret
                .setAccessKeySecret(clientKycConfig.getAccessKeySecret());
        // 访问的域名
        config.endpoint = clientKycConfig.getEndpoint();

        return new com.aliyun.cloudauth20190307.Client(config);
    }


    @Override
    public R saveKycInfo(CustomerKycRecordsReq req) {
        log.info("保存客户KYC信息开始");
        CustomerInfo recordsReq=new CustomerInfo();
        recordsReq.setUserId(req.getUserId());
        List<CustomerInfo> list= iCustomerInfoService.selectCustomerInfoList(recordsReq);

        CustomerInfo records=new CustomerInfo();
        BeanUtils.copyProperties(req,records);
        //根据身份证号码计算年龄
        int age=IdCardAgeCalculator.calculateAge(records.getIdNumber());
        records.setAge(String.valueOf(age));
        if(CollectionUtils.isNotEmpty(list)){
            records.setId(list.get(0).getId());
            if(StringUtils.isNotEmpty(list.get(0).getCertifyId())){
                R ret=getEkycResult(list.get(0).getUserId());
                if (ret.getCode() == R.SUCCESS&& "T".equals(ret.getData())){
                    return  R.fail("认证成功不能编辑。");
                }
            }
            //更新
            iCustomerInfoService.updateCustomerInfo(records);
        }else{
            iCustomerInfoService.insertCustomerInfo(records);
        }
        return R.ok();
    }

    @Override
    public CustomerKycRecordsReq getKycInfo(Long userId) {
        CustomerInfo recordsReq=new CustomerInfo();
        recordsReq.setUserId(userId);
        List<CustomerInfo> list= iCustomerInfoService.selectCustomerInfoList(recordsReq);
        if (CollectionUtils.isNotEmpty(list)){
            CustomerKycRecordsReq req=new CustomerKycRecordsReq();
            BeanUtils.copyProperties(list.get(0),req);
            return req;
        }
        return null;
    }

    @Override
    public R getEkycReturnUrl(Long userId, JSONObject metaInfo) {
        try {

            CustomerInfo recordsReq=new CustomerInfo();
            recordsReq.setUserId(userId);
            List<CustomerInfo> list= iCustomerInfoService.selectCustomerInfoList(recordsReq);
            if (CollectionUtils.isEmpty(list)){
               return  R.fail("用户身份信息为空。");
            }

            CustomerInfo records=list.get(0);
            if (2L==records.getFaceVerifyStatus()){
                return  R.fail("已经认证成功。");
            }
            if(StringUtils.isNotEmpty(records.getCertifyId())){
                R ret=getEkycResult(userId);
                if (ret.getCode() == R.SUCCESS&& "T".equals(ret.getData())){
                    return  R.fail("已经认证成功。");
                }
            }

            com.aliyun.cloudauth20190307.Client client = createClient();
            Params params = createApiInfo("InitFaceVerify","formData");
            // query params
            Map<String, Object> queries = new HashMap<>();
            queries.put("SceneId", clientKycConfig.getSceneId());
            Snowflake snowflake = IdUtil.getSnowflake(); // 默认 workerId=1, datacenterId=1
            long outerOrderNo = snowflake.nextId(); // 生成一个 ID
            queries.put("OuterOrderNo", String.valueOf(outerOrderNo));
            queries.put("ProductCode", clientKycConfig.getProductCode());
            queries.put("Model", clientKycConfig.getModel());
            queries.put("CertType", clientKycConfig.getCertType());
            queries.put("CertName", records.getFullName());
            queries.put("CertNo", records.getIdNumber());
            queries.put("ReturnUrl", clientKycConfig.getReturnUrl());
            queries.put("Mobile", records.getPhoneNumber());
            queries.put("MetaInfo", metaInfo.toJSONString());
            queries.put("Ip", null);
            queries.put("UserId", records.getUserId());
            queries.put("OssBucketName", clientKycConfig.getOssBucketName());
            String path=getFrontIdFilrUrl(records);
            queries.put("OssObjectName", path);
            // body params
            Map<String, Object> body = new HashMap<>();
            // runtime options
            RuntimeOptions runtime = new RuntimeOptions();
            OpenApiRequest request = new OpenApiRequest()
                    .setQuery(com.aliyun.openapiutil.Client.query(queries))
                    .setBody(body);
            // 复制代码运行请自行打印 API 的返回值
            // 返回值实际为 Map 类型，可从 Map 中获得三类数据：响应体 body、响应头 headers、HTTP 返回的状态码 statusCode。
            Object resp = client.callApi(params, request, runtime);
            com.aliyun.teaconsole.Client.log(com.aliyun.teautil.Common.toJSONString(resp));
            // 解析返回结果
            String certifyUrl="";
            String certifyId="";
            String msg="";
            if (resp != null) {
                Map<String, Object> responseMap = (Map<String, Object>) resp;
                if (responseMap.containsKey("body")) {
                    Map<String, Object> bodyMap = (Map<String, Object>) responseMap.get("body");
                    if (bodyMap.containsKey("ResultObject")) {
                        Map<String, Object> resultObject = (Map<String, Object>) bodyMap.get("ResultObject");
                        if (resultObject.containsKey("CertifyUrl")) {
                            certifyId= (String) resultObject.get("CertifyId");
                            certifyUrl= (String) resultObject.get("CertifyUrl");
                        }
                        if (bodyMap.containsKey("Message")) {
                            msg = (String) bodyMap.get("Message");
                        }
                    }
                }
            }

            //更新
            CustomerInfo updateRecords=new CustomerInfo();
            updateRecords.setCertifyId(certifyId);
            updateRecords.setId(records.getId());
            updateRecords.setFaceImageUrl(certifyUrl);
            updateRecords.setUpdateBy("system");
            updateRecords.setRemark(msg);
            //认证中
            updateRecords.setFaceVerifyStatus(1L);

            if(StringUtils.isEmpty(certifyUrl)||StringUtils.isEmpty(certifyId)){
                //认证失败
                updateRecords.setFaceVerifyStatus(3L);
                iCustomerInfoService.updateCustomerInfo(updateRecords);
                return R.fail(msg);
            }else{
                iCustomerInfoService.updateCustomerInfo(updateRecords);
            }
           return  R.ok(certifyUrl);
        }catch (Exception e){
            log.error("获取客户信息失败：",e);
            return R.fail("获取客户信息失败");
        }
    }

    @Override
    public R getEkycResult(Long userId) {
        try {

            CustomerInfo recordsReq=new CustomerInfo();
            recordsReq.setUserId(userId);
            List<CustomerInfo> list= iCustomerInfoService.selectCustomerInfoList(recordsReq);
            if (CollectionUtils.isEmpty(list)){
               return R.fail("用户身份信息为空。");
            }

            CustomerInfo records=list.get(0);
            if (StringUtils.isEmpty(records.getCertifyId())){
                return R.fail("请先获取认证信息");
            }
            if (2L==records.getFaceVerifyStatus()){
                return R.ok("T");
            }

            com.aliyun.teaopenapi.Client client = createClient();
            Params params = createApiInfo("DescribeFaceVerify","json");
            // query params
            java.util.Map<String, Object> queries = new java.util.HashMap<>();
            queries.put("SceneId", 1000014670);
            queries.put("CertifyId", records.getCertifyId());
            // runtime options
            com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
            com.aliyun.teaopenapi.models.OpenApiRequest request = new com.aliyun.teaopenapi.models.OpenApiRequest()
                    .setQuery(com.aliyun.openapiutil.Client.query(queries));
            // 复制代码运行请自行打印 API 的返回值
            // 返回值实际为 Map 类型，可从 Map 中获得三类数据：响应体 body、响应头 headers、HTTP 返回的状态码 statusCode。
            Map resp =client.callApi(params, request, runtime);
            com.aliyun.teaconsole.Client.log(com.aliyun.teautil.Common.toJSONString(resp));
            String msg = "";
            String passed = "F";
            BigDecimal verifyScore = BigDecimal.ZERO;
            if (resp != null) {
                try {
                    Map<String, Object> responseMap = (Map<String, Object>) resp;
                    Map<String, Object> bodyMap = (Map<String, Object>) responseMap.get("body");
                    if (bodyMap != null) {
                        // 获取Message字段
                        msg = bodyMap.getOrDefault("Message", "").toString();
                        // 处理ResultObject
                        if (bodyMap.containsKey("ResultObject") && bodyMap.get("ResultObject") instanceof Map) {
                            Map<String, Object> resultObject = (Map<String, Object>) bodyMap.get("ResultObject");
                            // 获取Passed字段
                            passed = resultObject.getOrDefault("Passed", "F").toString();
                            // 处理MaterialInfo（需要单独解析JSON字符串）
                            if (resultObject.containsKey("MaterialInfo")) {
                                Object materialInfoObj = resultObject.get("MaterialInfo");
                                JSONObject materialInfo = (JSONObject)JSONObject.parse(materialInfoObj+"");
                                String facialPictureFront =materialInfo.getString("facialPictureFront");
                                if(StringUtils.isNotEmpty(facialPictureFront)){
                                    JSONObject facialPictureFrontJSON = (JSONObject)JSONObject.parse(facialPictureFront);
                                    verifyScore = facialPictureFrontJSON.getBigDecimal("verifyScore");
                                }
                            }
                        }
                    }
                } catch (ClassCastException e) {
                    log.error("响应结构类型异常", e);
                }
            }

            CustomerInfo updateRecords=new CustomerInfo();
            updateRecords.setId(records.getId());
            updateRecords.setFaceVerifyScore(verifyScore);
            updateRecords.setFaceVerifyTime(new Date());
            updateRecords.setFaceVerifyStatus("T".equals(passed)?2L:3L);
            updateRecords.setUpdateBy("system");
            updateRecords.setUpdateTime(new Date());
            updateRecords.setRemark(msg);
            int updateCount=iCustomerInfoService.updateCustomerInfo(updateRecords);
            log.info("更新客户信息成功："+updateCount);
            return R.ok(passed);
        }catch (Exception e){
            log.error("获取客户信息失败：",e);
            return R.fail("获取客户信息失败");
        }

    }


    private static String getFrontIdFilrUrl(CustomerInfo records) {
        String frontIdFileUrl = records.getFrontIdFileUrl();
        String path = "";
        if (frontIdFileUrl != null && !frontIdFileUrl.isEmpty()) {
            // 查找"com"后面的第一个斜杠位置
            int comIndex = frontIdFileUrl.indexOf("com");
            if (comIndex != -1) {
                // 从"com"后面开始查找第一个斜杠
                path = frontIdFileUrl.substring(comIndex+4);
            }
            int queryIndex = path.indexOf("?");
            if (queryIndex != -1) {
                // 截取"?"之前的部分
                path = path.substring(0, queryIndex);
            }
        }
        return path;
    }


    @Override
    public R getClientCustomerInfo(Long userId) {
        CustomerInfo recordsReq=new CustomerInfo();
        recordsReq.setUserId(userId);
        List<CustomerInfo> list= iCustomerInfoService.selectCustomerInfoList(recordsReq);
        if (list.isEmpty()){
            return R.fail("尚未KYC认证！");
        }
        CustomerInfo records=list.get(0);
        ClientCustomerResp resp=new ClientCustomerResp();
        BeanUtils.copyBeanProp(resp,records);
        return R.ok(resp);
    }

    @Override
    public R saveClientCustomerInfo(ClientCustomerReq req) {
        log.info("保存客户信息开始");
        CustomerInfo recordsReq=new CustomerInfo();
        recordsReq.setUserId(req.getUserId());
        List<CustomerInfo> list= iCustomerInfoService.selectCustomerInfoList(recordsReq);
        if(CollectionUtils.isEmpty(list)){
            return R.fail("请先进行KYC认证");
        }
        CustomerInfo exitCustomerInfo=list.get(0);
        CustomerInfo records=new CustomerInfo();
        BeanUtils.copyProperties(req,records);
        //根据身份证号码计算年龄
        int age=IdCardAgeCalculator.calculateAge(records.getIdNumber());
        records.setAge(String.valueOf(age));
        records.setId(exitCustomerInfo.getId());
        //更新
        iCustomerInfoService.updateCustomerInfo(records);
        return R.ok();
    }


}
