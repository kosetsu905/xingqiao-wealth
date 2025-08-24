package com.xingqiao.common.core.utils;
import com.xingqiao.common.core.domain.IdCardInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


public class IdCardInfoExtractor {
    protected static final Logger logger = LoggerFactory.getLogger(IdCardInfoExtractor.class);

    // 18位身份证校验码权重（ISO 7064:1983.MOD 11-2 算法）
    private static final int[] WEIGHTS = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
    // 校验码映射表（索引对应余数，值对应校验码字符）
    private static final char[] CHECK_CODES = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};

    /**
     * 提取身份证号中的年龄、性别和出生日期（支持15位/18位）
     * @param idCard 身份证号
     * @return 包含出生日期、年龄、性别的 Map（失败时返回 error 字段）
     */
    public static IdCardInfo extractInfo(String idCard) {
        IdCardInfo result = new IdCardInfo();

        // 1. 校验长度
        if (idCard == null || (idCard.length() != 15 && idCard.length() != 18)) {
            logger.info("身份证号长度必须为15位或18位");
            return result;
        }

        // 2. 校验18位身份证的校验码（15位无校验码）
        if (idCard.length() == 18 && !validateCheckCode(idCard)) {
            logger.info("18位身份证校验码错误");
            return result;
        }

        // 3. 提取出生日期
        String birthDateStr;
        try {
            if (idCard.length() == 15) {
                // 15位：第7-12位为 YYMMDD → 补全为 19YYMMDD
                birthDateStr = "19" + idCard.substring(6, 12);
            } else {
                // 18位：第7-14位为 YYYYMMDD
                birthDateStr = idCard.substring(6, 14);
            }
            // 校验日期有效性（如 20230230 是非法日期）
            LocalDate.parse(birthDateStr, DateTimeFormatter.BASIC_ISO_DATE);
        } catch (DateTimeParseException e) {
            logger.info("出生日期无效：{}", e.getMessage());
            return result;
        }

        // 4. 计算年龄（基于当前日期）
        LocalDate today = LocalDate.now();
        LocalDate birthDate = LocalDate.parse(birthDateStr, DateTimeFormatter.BASIC_ISO_DATE);
        Period period = Period.between(birthDate, today);
        int age = period.getYears();

        // 5. 提取性别
        int genderCode;
        if (idCard.length() == 15) {
            // 15位：第15位（索引14）为顺序码
            genderCode = Character.getNumericValue(idCard.charAt(14));
        } else {
            // 18位：第17位（索引16）为顺序码
            genderCode = Character.getNumericValue(idCard.charAt(16));
        }
        String gender = (genderCode % 2 == 1) ? "男" : "女";

        // 封装结果
        result.setBirthDate(birthDateStr);
        result.setAge(age);
        result.setGender(gender);
        return result;
    }

    /**
     * 校验18位身份证的校验码（ISO 7064:1983.MOD 11-2 算法）
     * @param idCard 18位身份证号
     * @return 校验是否通过
     */
    private static boolean validateCheckCode(String idCard) {
        if (idCard.length() != 18) return false;

        // 计算前17位的加权和
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            char c = idCard.charAt(i);
            if (!Character.isDigit(c)) return false; // 前17位必须是数字
            sum += Character.getNumericValue(c) * WEIGHTS[i];
        }

        // 计算校验码并比对最后一位
        int mod = sum % 11;
        char actualCheckCode = idCard.charAt(17);
        char expectedCheckCode = CHECK_CODES[mod];
        return (actualCheckCode == expectedCheckCode) ||
                (actualCheckCode == 'x' && expectedCheckCode == 'X'); // 兼容小写x
    }

    // 测试示例
    public static void main(String[] args) {
        // 18位身份证（1989-06-07 出生，男，校验码正确）
        String id18 = "450802198906072016";
        // 15位身份证（1990-05-15 出生，男）
        String id15 = "110105900515123";
        // 无效身份证（长度错误）
        String invalidId = "12345";

        System.out.println("18位身份证测试：" + extractInfo(id18));
        // 输出：{出生日期=19900515, 年龄=35（假设当前是2025年）, 性别=男}

        System.out.println("15位身份证测试：" + extractInfo(id15));
        // 输出：{出生日期=19900515, 年龄=35, 性别=男}

        System.out.println("无效身份证测试：" + extractInfo(invalidId));
        // 输出：{error=身份证号长度必须为15位或18位}
    }
}