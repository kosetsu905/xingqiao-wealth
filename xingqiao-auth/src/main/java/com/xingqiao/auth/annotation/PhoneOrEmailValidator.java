package com.xingqiao.auth.annotation;

import com.xingqiao.auth.form.CodeReqDTO;
import com.xingqiao.common.core.utils.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneOrEmailValidator implements ConstraintValidator<PhoneOrEmailRequired, CodeReqDTO> {

    @Override
    public void initialize(PhoneOrEmailRequired constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(CodeReqDTO dto, ConstraintValidatorContext context) {
        String phone = dto.getPhoneNumber();
        String email = dto.getEmail();

        // 检查是否至少有一个非空且非全空白
        boolean phoneValid = StringUtils.hasText(phone);
        boolean emailValid = StringUtils.hasText(email);

        if (!phoneValid && !emailValid) {
            // 如果都不满足，设置错误信息
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode(null)
                    .addConstraintViolation();
            return false;
        }

        // 可选：如果其中一个存在，校验其格式
        if (phoneValid) {
            // 校验手机号格式，使用之前的正则
            if (!isValidPhone(phone)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("手机号格式不正确")
                        .addPropertyNode("phoneNumber")
                        .addConstraintViolation();
                return false;
            }
        }

        if (emailValid) {
            // 校验邮箱格式
            if (!isValidEmail(email)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("邮箱格式不正确")
                        .addPropertyNode("email")
                        .addConstraintViolation();
                return false;
            }
        }

        return true;
    }

    private boolean isValidPhone(String phone) {
        // 使用之前的手机号正则，比如^1[3456789]\\d{8}$
        return phone.matches("^1(3[0-9]|4[5789]|5[0-35-9]|6[257]|7[0-25-8]|8[0-9]|9[189])\\d{8}$");
    }

    private boolean isValidEmail(String email) {
        // 简单的邮箱正则，或者使用Java自带的验证
        return email.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
    }

}
