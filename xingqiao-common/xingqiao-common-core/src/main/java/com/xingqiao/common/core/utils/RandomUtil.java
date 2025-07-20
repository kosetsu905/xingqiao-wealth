package com.xingqiao.common.core.utils;

public class RandomUtil {
    //6位数验证码生成方法
    public static String randomNumbers(int length){
        StringBuffer code = new StringBuffer();
        for(int i = 0; i < length; i++){
            int number = (int)(Math.random() * 10);
            code.append(number);
        }
        return code.toString();
    }
}
