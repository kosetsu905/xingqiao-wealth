package com.xingqiao.message.util.sms;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendBatchSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendBatchSmsResponse;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 阿里云短信服务工具类
 */
@Component
public class AliyunSmsUtil {
    
    private static final Logger log = LoggerFactory.getLogger(AliyunSmsUtil.class);
    
    @Resource
    private AliyunSmsConfig aliyunSmsConfig;

    private static AliyunSmsConfig staticAliyunSmsConfig;
    
    @PostConstruct
    public void init() {
        staticAliyunSmsConfig = aliyunSmsConfig;
    }
    
    /**
     * 创建客户端
     *
     * @return Client
     * @throws Exception 异常
     */
    public  Client createClient() throws Exception {
        // 工程代码建议使用更安全的无AK方式，凭据配置方式请参见：https://help.aliyun.com/document_detail/378657.html。
        Config config = new Config()
                .setAccessKeyId(staticAliyunSmsConfig.getAccessKeyId())
                .setAccessKeySecret(staticAliyunSmsConfig.getAccessKeySecret())
                .setEndpoint(staticAliyunSmsConfig.getEndpoint());
        return new Client(config);
    }
    
    /**
     * 发送短信验证码
     *
     * @param phoneNumbers 手机号
     * @param code         验证码
     * @return 发送结果
     */
    public  boolean sendSmsCode(String phoneNumbers, String code) {
        try {
            Client client = createClient();
            SendSmsRequest sendSmsRequest = new SendSmsRequest()
                    .setSignName(staticAliyunSmsConfig.getSignName())
                    .setTemplateCode(TemplateCodeConstant.TEMPLATE_CODE_REGISTER)
                    .setPhoneNumbers(phoneNumbers)
                    .setTemplateParam("{\"code\":\"" + code + "\"}");
            RuntimeOptions runtime = new RuntimeOptions();
            SendSmsResponse resp = client.sendSmsWithOptions(sendSmsRequest, runtime);
            
            log.info("发送短信验证码，手机号：{}，验证码：{}，返回结果：{}", phoneNumbers, code, resp.getBody());
            
            // 判断发送是否成功
            return "OK".equals(resp.getBody().getCode());
        } catch (TeaException error) {
            log.error("发送短信验证码出现 TeaException，错误信息：{}", error.getMessage(), error);
            return false;
        } catch (Exception _error) {
            TeaException error = new TeaException(_error.getMessage(), _error);
            log.error("发送短信验证码出现 Exception，错误信息：{}", error.getMessage(), error);
            return false;
        }
    }
    
    /**
     * 发送短信
     *
     * @param phoneNumbers 手机号
     * @param signName     短信签名
     * @param templateCode 短信模板ID
     * @param templateParam 短信模板变量对应的实际值
     * @return 发送结果
     */
    public  boolean sendSms(String phoneNumbers, String signName, String templateCode, Map<String, String> templateParam) {
        try {
            Client client = createClient();
            
            // 构造模板参数
            StringBuilder templateParamStr = new StringBuilder();
            templateParamStr.append("{");
            int i = 0;
            for (Map.Entry<String, String> entry : templateParam.entrySet()) {
                if (i > 0) {
                    templateParamStr.append(",");
                }
                templateParamStr.append("\"").append(entry.getKey()).append("\":\"").append(entry.getValue()).append("\"");
                i++;
            }
            templateParamStr.append("}");
            
            SendSmsRequest sendSmsRequest = new SendSmsRequest()
                    .setSignName(signName)
                    .setTemplateCode(templateCode)
                    .setPhoneNumbers(phoneNumbers)
                    .setTemplateParam(templateParamStr.toString());
            RuntimeOptions runtime = new RuntimeOptions();
            SendSmsResponse resp = client.sendSmsWithOptions(sendSmsRequest, runtime);
            
            log.info("发送短信，手机号：{}，签名：{}，模板：{}，参数：{}，返回结果：{}", 
                    phoneNumbers, signName, templateCode, templateParamStr, resp.getBody());
            
            // 判断发送是否成功
            return "OK".equals(resp.getBody().getCode());
        } catch (TeaException error) {
            log.error("发送短信出现 TeaException，错误信息：{}", error.getMessage(), error);
            return false;
        } catch (Exception _error) {
            TeaException error = new TeaException(_error.getMessage(), _error);
            log.error("发送短信出现 Exception，错误信息：{}", error.getMessage(), error);
            return false;
        }
    }
    
    /**
     * 批量发送短信
     *
     * @param phoneNumbers 手机号列表，多个手机号用逗号分隔，最多支持1000个
     * @param signName     短信签名
     * @param templateCode 短信模板ID
     * @param templateParam 短信模板变量对应的实际值
     * @return 发送结果
     */
    public  boolean sendBatchSms(List<String> phoneNumbers, String signName, String templateCode, Map<String, String> templateParam) {
        try {
            Client client = createClient();
            
            // 构造手机号参数
            StringBuilder phoneNumbersStr = new StringBuilder();
            phoneNumbersStr.append("{");
            for (int i = 0; i < phoneNumbers.size(); i++) {
                if (i > 0) {
                    phoneNumbersStr.append(",");
                }
                phoneNumbersStr.append("\"").append(i).append("\":\"").append(phoneNumbers.get(i)).append("\"");
            }
            phoneNumbersStr.append("}");
            
            // 构造签名参数
            StringBuilder signNameStr = new StringBuilder();
            signNameStr.append("{");
            for (int i = 0; i < phoneNumbers.size(); i++) {
                if (i > 0) {
                    signNameStr.append(",");
                }
                signNameStr.append("\"").append(i).append("\":\"").append(signName).append("\"");
            }
            signNameStr.append("}");
            
            // 构造模板参数
            StringBuilder templateParamStr = new StringBuilder();
            templateParamStr.append("{");
            int i = 0;
            for (Map.Entry<String, String> entry : templateParam.entrySet()) {
                if (i > 0) {
                    templateParamStr.append(",");
                }
                templateParamStr.append("\"").append(entry.getKey()).append("\":\"").append(entry.getValue()).append("\"");
                i++;
            }
            templateParamStr.append("}");
            
            // 构造模板参数列表
            StringBuilder templateParamListStr = new StringBuilder();
            templateParamListStr.append("[");
            for (int j = 0; j < phoneNumbers.size(); j++) {
                if (j > 0) {
                    templateParamListStr.append(",");
                }
                templateParamListStr.append(templateParamStr.toString());
            }
            templateParamListStr.append("]");
            
            SendBatchSmsRequest sendBatchSmsRequest = new SendBatchSmsRequest()
                    .setPhoneNumberJson(phoneNumbersStr.toString())
                    .setSignNameJson(signNameStr.toString())
                    .setTemplateCode(templateCode)
                    .setTemplateParamJson(templateParamListStr.toString());
            
            RuntimeOptions runtime = new RuntimeOptions();
            SendBatchSmsResponse resp = client.sendBatchSmsWithOptions(sendBatchSmsRequest, runtime);
            
            log.info("批量发送短信，手机号：{}，签名：{}，模板：{}，参数：{}，返回结果：{}", 
                    phoneNumbers, signName, templateCode, templateParamListStr, resp.getBody());
            
            // 判断发送是否成功
            return "OK".equals(resp.getBody().getCode());
        } catch (TeaException error) {
            log.error("批量发送短信出现 TeaException，错误信息：{}", error.getMessage(), error);
            return false;
        } catch (Exception _error) {
            TeaException error = new TeaException(_error.getMessage(), _error);
            log.error("批量发送短信出现 Exception，错误信息：{}", error.getMessage(), error);
            return false;
        }
    }
    
    /**
     * 批量发送不同内容短信
     *
     * @param phoneNumbers 手机号列表
     * @param signNames    短信签名列表，与手机号列表一一对应
     * @param templateCode 短信模板ID
     * @param templateParams 短信模板变量列表，与手机号列表一一对应
     * @return 发送结果
     */
    public  boolean sendBatchSmsWithDifferentContent(List<String> phoneNumbers, List<String> signNames,
                                                           String templateCode, List<Map<String, String>> templateParams) {
        try {
            Client client = createClient();
            
            // 参数校验
            if (phoneNumbers.size() != signNames.size() || phoneNumbers.size() != templateParams.size()) {
                log.error("批量发送不同内容短信参数数量不一致，手机号：{}，签名：{}，模板参数：{}", 
                        phoneNumbers.size(), signNames.size(), templateParams.size());
                return false;
            }
            
            // 构造手机号参数
            StringBuilder phoneNumbersStr = new StringBuilder();
            phoneNumbersStr.append("{");
            for (int i = 0; i < phoneNumbers.size(); i++) {
                if (i > 0) {
                    phoneNumbersStr.append(",");
                }
                phoneNumbersStr.append("\"").append(i).append("\":\"").append(phoneNumbers.get(i)).append("\"");
            }
            phoneNumbersStr.append("}");
            
            // 构造签名参数
            StringBuilder signNameStr = new StringBuilder();
            signNameStr.append("{");
            for (int i = 0; i < signNames.size(); i++) {
                if (i > 0) {
                    signNameStr.append(",");
                }
                signNameStr.append("\"").append(i).append("\":\"").append(signNames.get(i)).append("\"");
            }
            signNameStr.append("}");
            
            // 构造模板参数列表
            StringBuilder templateParamListStr = new StringBuilder();
            templateParamListStr.append("[");
            for (int j = 0; j < templateParams.size(); j++) {
                if (j > 0) {
                    templateParamListStr.append(",");
                }
                
                // 构造单个模板参数
                StringBuilder singleTemplateParamStr = new StringBuilder();
                singleTemplateParamStr.append("{");
                Map<String, String> paramMap = templateParams.get(j);
                int k = 0;
                for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                    if (k > 0) {
                        singleTemplateParamStr.append(",");
                    }
                    singleTemplateParamStr.append("\"").append(entry.getKey()).append("\":\"").append(entry.getValue()).append("\"");
                    k++;
                }
                singleTemplateParamStr.append("}");
                templateParamListStr.append(singleTemplateParamStr.toString());
            }
            templateParamListStr.append("]");
            
            SendBatchSmsRequest sendBatchSmsRequest = new SendBatchSmsRequest()
                    .setPhoneNumberJson(phoneNumbersStr.toString())
                    .setSignNameJson(signNameStr.toString())
                    .setTemplateCode(templateCode)
                    .setTemplateParamJson(templateParamListStr.toString());
            
            RuntimeOptions runtime = new RuntimeOptions();
            SendBatchSmsResponse resp = client.sendBatchSmsWithOptions(sendBatchSmsRequest, runtime);
            
            log.info("批量发送不同内容短信，手机号：{}，签名：{}，模板：{}，参数：{}，返回结果：{}", 
                    phoneNumbers, signNames, templateCode, templateParamListStr, resp.getBody());
            
            // 判断发送是否成功
            return "OK".equals(resp.getBody().getCode());
        } catch (TeaException error) {
            log.error("批量发送不同内容短信出现 TeaException，错误信息：{}", error.getMessage(), error);
            return false;
        } catch (Exception _error) {
            TeaException error = new TeaException(_error.getMessage(), _error);
            log.error("批量发送不同内容短信出现 Exception，错误信息：{}", error.getMessage(), error);
            return false;
        }
    }
}