package com.xingqiao.message.api.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {PhoneOrEmailValidator.class})
public @interface PhoneOrEmailRequired {
    String message() default "手机号或邮箱至少填写一个";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}