package com.xingqiao.message.service.impl;

import com.xingqiao.common.core.constant.Constants;
import com.xingqiao.common.core.domain.R;
import com.xingqiao.common.core.enums.RegistrationStep;
import com.xingqiao.common.core.utils.RandomUtil;
import com.xingqiao.common.core.utils.StringUtils;
import com.xingqiao.common.redis.service.RedisService;
import com.xingqiao.message.CommonConfig;
import com.xingqiao.message.api.domain.CodeReqDTO;
import com.xingqiao.message.api.domain.InviteReqDTO;
import com.xingqiao.message.service.MessageService;
import com.xingqiao.message.util.email.EmailUtils;
import com.xingqiao.message.util.sms.AliyunSmsUtil;
import com.xingqiao.system.api.RemoteTocUserService;
import com.xingqiao.system.api.domain.CustomerQueryInnerRequest;
import com.xingqiao.system.api.model.CustomerListInnerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import javax.annotation.Resource;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class MessageServiceImpl implements MessageService {

    @Resource
    private RedisService redisService;
    @Resource
    private EmailUtils emailUtils;
    @Resource
    private AliyunSmsUtil aliyunSmsUtil;
    @Resource
    private CommonConfig commonConfig;
    @Resource
    private RemoteTocUserService remoteTocUserService;


    public R sendCode(CodeReqDTO codeReqDTO) {
        //生成随机的6位数验证码
        String code = RandomUtil.randomNumbers(6);
        //判断验证码类型
        if (RegistrationStep.PHONE_VERIFICATION_CODE.getCode().equals(codeReqDTO.getStep())) {
            //发送手机验证码
            aliyunSmsUtil.sendSmsCode(codeReqDTO.getPhoneNumber(),code);
            //发送成功保存到redis中，设置有效时间
            redisService.setCacheObject(Constants.CODE_KEY +
                            codeReqDTO.getStep()+":"+codeReqDTO.getPhoneNumber(),
                    code, Constants.CODE_TTL, TimeUnit.SECONDS);
            return R.ok("发送成功");
        }

        if (RegistrationStep.EMAIL_VERIFICATION_CODE.getCode().equals(codeReqDTO.getStep())) {
            log.info("发送邮箱验证码,code={}",code);
            //发送邮箱验证码
            R sendEmailResult = this.sendRegisterCodeEmail(codeReqDTO.getEmail(),code);
            if (R.SUCCESS == sendEmailResult.getCode()){
                //发送成功保存到redis中，设置有效时间
                redisService.setCacheObject(Constants.CODE_KEY +
                                codeReqDTO.getStep()+":"+codeReqDTO.getEmail(),
                        code, Constants.CODE_TTL, TimeUnit.SECONDS);
                log.info("发送邮箱验证码成功");
                return R.ok("发送成功");
            }else {
                log.error("发送邮箱验证码失败{}", sendEmailResult.getMsg());
                return R.fail("发送邮件失败: " + sendEmailResult.getMsg());
            }
        }
        log.error("找不到发送渠道");
        return R.fail("找不到发送渠道");
    }


    /**
     * 读取HTML模板文件内容
     *
     * @param templatePath 模板路径，相对于resources目录
     * @return 模板文件内容
     */
    public String readTemplateToString(String templatePath) {
        try {
            ClassPathResource resource = new ClassPathResource(templatePath);
            InputStream inputStream = resource.getInputStream();
            return StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("读取模板文件失败: {}", templatePath, e);
            throw new RuntimeException("读取模板文件失败");
        }
    }

    /**
     * 发送注册验证码邮件
     *
     * @param to   收件人邮箱
     * @param code 验证码
     * @return 发送结果
     */
    public R sendRegisterCodeEmail(String to, String code) {
        try {
            // 读取模板文件
            String templateContent = readTemplateToString("template/registerCodeTemplate.html");
            if (templateContent == null) {
                return R.fail("读取邮件模板失败");
            }

            // 替换模板中的验证码
            String emailContent = replaceTemplateVariable(templateContent, code);

            // 发送HTML邮件
            emailUtils.sendHtmlEmail(to, "深圳星桥数据技术", emailContent,false);
            return R.ok("发送成功！");
        } catch (Exception e) {
            log.error("发送注册验证码邮件失败", e);
            return R.fail("发送邮件失败: " + e.getMessage());
        }
    }

    /**
     * 替换模板中的变量
     *
     * @param templateContent 模板内容
     * @param code            验证码
     * @return 替换后的HTML内容
     */
    private String replaceTemplateVariable(String templateContent, String code) {
        // 使用简单的字符串替换
        return templateContent.replace("{code}", code);
    }

    @Override
    public R<?> sendInviteMessageBatch(InviteReqDTO inviteReqDTO) {
        List<Long> idList = inviteReqDTO.getIdList();

        CustomerQueryInnerRequest request=new CustomerQueryInnerRequest();
        request.setIdList(idList);
        R<List<CustomerListInnerResponse>> result = remoteTocUserService.getCustomerInnerList(request);
        if (result.getCode()!=200) {
            log.error("获取客户列表失败: {}", result.getMsg());
            return R.fail("获取客户列表失败: " + result.getMsg());
        }
        List<CustomerListInnerResponse> customerList = result.getData();

        String inviteLink = commonConfig.getRegisterUrl();
        if (StringUtils.isEmpty(inviteLink)){
            return R.fail("注册链接不能为空");
        }

        String type = inviteReqDTO.getType();
        if (idList == null || idList.isEmpty()) {
            return R.fail("客户ID列表不能为空");
        }

        if ("2".equals(type)) {
            // 发送邮箱邀请
            for (CustomerListInnerResponse customer : customerList) {
                try {
                    inviteLink=inviteLink+"?=userTempid="+customer.getUserTempId();
                    // 读取邀请模板文件
                    String templateContent = readTemplateToString("template/inviteCustomerTemplate.html");
                    if (templateContent == null) {
                        log.error("读取邀请邮件模板失败");
                        continue;
                    }
                    // 替换模板中的邀请链接
                    String emailContent = templateContent.replace("{inviteLink}", inviteLink);

                    // 发送HTML邮件
                    emailUtils.sendHtmlEmail(customer.getEmail(), "深圳星桥数据技术", emailContent, false);
                    log.info("发送邀请邮件成功，客户ID: {}, 邮箱: {}", customer.getId(), customer.getEmail());
                } catch (Exception e) {
                    log.error("发送邀请邮件失败，客户ID: {}, 邮箱: {}", customer.getId(),  customer.getEmail(), e);
                }
            }
            return R.ok("批量发送邀请邮件完成");
        } else if ("1".equals(type)) {
            // 发送手机短信邀请
            // 发送邮箱邀请
            for (CustomerListInnerResponse customer : customerList) {
                inviteLink=inviteLink+"?=userTempid="+customer.getUserTempId();
            }
            return R.ok("批量发送短信邀请完成");
        } else {
            return R.fail("不支持的发送类型");
        }
    }
}