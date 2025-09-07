package com.xingqiao.common.core.utils;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class IdCardAgeCalculator {

    public static int calculateAge(String idNumber) {
        // 验证身份证长度
        if (idNumber.length() != 15 && idNumber.length() != 18) {
            throw new IllegalArgumentException("无效的身份证号码长度");
        }

        String birthDateStr;
        // 处理18位身份证
        if (idNumber.length() == 18) {
            birthDateStr = idNumber.substring(6, 14); // 提取YYYYMMDD格式的出生日期
        }
        // 处理15位身份证
        else {
            // 15位身份证年份只有2位，默认补19（因为2000年后基本使用18位身份证）
            birthDateStr = "19" + idNumber.substring(6, 12); // 格式变为YYYYMMDD
        }

        // 解析出生日期
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate birthDate = LocalDate.parse(birthDateStr, formatter);

        // 获取当前日期
        LocalDate currentDate = LocalDate.now();

        // 计算周岁年龄
        Period period = Period.between(birthDate, currentDate);
        int age = period.getYears();

        // 如果今年生日还没过，年龄减1
        if (currentDate.getMonthValue() < birthDate.getMonthValue() ||
                (currentDate.getMonthValue() == birthDate.getMonthValue() &&
                        currentDate.getDayOfMonth() < birthDate.getDayOfMonth())) {
            age--;
        }

        return age;
    }

    public static void main(String[] args) {
        // 测试示例
        String id18 = "110105199001011234"; // 18位身份证
        String id15 = "110105900101123";    // 15位身份证

        System.out.println("18位身份证年龄: " + calculateAge(id18));
        System.out.println("15位身份证年龄: " + calculateAge(id15));
    }
}