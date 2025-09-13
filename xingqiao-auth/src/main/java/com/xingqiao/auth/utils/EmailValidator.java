package com.xingqiao.auth.utils;

import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * 邮件校验工具类
 * 提供多种邮箱格式验证方法，支持自定义验证规则
 */
public class EmailValidator {

    // 常用邮箱正则表达式
    private static final String COMMON_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    // RFC 5322 标准正则表达式
    private static final String RFC5322_REGEX =
            "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

    // 简单邮箱正则表达式
    private static final String SIMPLE_REGEX =
            "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";

    // 默认使用的正则表达式
    private static Pattern defaultPattern = Pattern.compile(COMMON_REGEX);

    // 自定义验证规则
    private static Predicate<String> customValidator = null;

    // 是否允许IP地址域名
    private static boolean allowIpAddress = false;

    // 是否允许顶级域名
    private static boolean allowTopLevelDomain = false;

    // 是否允许国际化域名
    private static boolean allowInternational = false;

    private EmailValidator() {
        // 私有构造器防止实例化
    }

    /**
     * 使用默认规则验证邮箱格式
     * @param email 待验证的邮箱地址
     * @return 验证结果
     */
    public static boolean isValid(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        // 首先应用自定义验证器
        if (customValidator != null && !customValidator.test(email)) {
            return false;
        }

        // 使用默认正则验证
        boolean matches = defaultPattern.matcher(email).matches();

        // 额外验证规则
        if (matches) {
            if (!allowIpAddress && isIpAddressDomain(email)) {
                return false;
            }
            if (!allowTopLevelDomain && isTopLevelDomain(email)) {
                return false;
            }
            if (!allowInternational && containsNonAscii(email)) {
                return false;
            }
        }

        return matches;
    }

    /**
     * 使用RFC 5322标准验证邮箱格式
     * @param email 待验证的邮箱地址
     * @return 验证结果
     */
    public static boolean isValidByRfc5322(String email) {
        return Pattern.compile(RFC5322_REGEX).matcher(email).matches();
    }

    /**
     * 使用简单规则验证邮箱格式
     * @param email 待验证的邮箱地址
     * @return 验证结果
     */
    public static boolean isValidSimple(String email) {
        return Pattern.compile(SIMPLE_REGEX).matcher(email).matches();
    }

    /**
     * 设置自定义验证规则
     * @param validator 自定义验证函数
     */
    public static void setCustomValidator(Predicate<String> validator) {
        customValidator = validator;
    }

    /**
     * 设置默认验证模式
     * @param mode 验证模式 (COMMON, RFC5322, SIMPLE)
     */
    public static void setDefaultMode(ValidationMode mode) {
        switch (mode) {
            case COMMON:
                defaultPattern = Pattern.compile(COMMON_REGEX);
                break;
            case RFC5322:
                defaultPattern = Pattern.compile(RFC5322_REGEX);
                break;
            case SIMPLE:
                defaultPattern = Pattern.compile(SIMPLE_REGEX);
                break;
        }
    }

    /**
     * 设置是否允许IP地址域名
     * @param allow 是否允许
     */
    public static void setAllowIpAddress(boolean allow) {
        allowIpAddress = allow;
    }

    /**
     * 设置是否允许顶级域名
     * @param allow 是否允许
     */
    public static void setAllowTopLevelDomain(boolean allow) {
        allowTopLevelDomain = allow;
    }

    /**
     * 设置是否允许国际化域名
     * @param allow 是否允许
     */
    public static void setAllowInternational(boolean allow) {
        allowInternational = allow;
    }

    // 检查域名是否为IP地址
    private static boolean isIpAddressDomain(String email) {
        String domain = email.substring(email.indexOf('@') + 1);
        return domain.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
    }

    // 检查是否为顶级域名
    private static boolean isTopLevelDomain(String email) {
        String domain = email.substring(email.indexOf('@') + 1);
        return !domain.contains(".");
    }

    // 检查是否包含非ASCII字符
    private static boolean containsNonAscii(String email) {
        return !email.matches("\\A\\p{ASCII}*\\z");
    }

    /**
     * 验证模式枚举
     */
    public enum ValidationMode {
        COMMON, RFC5322, SIMPLE
    }
}
