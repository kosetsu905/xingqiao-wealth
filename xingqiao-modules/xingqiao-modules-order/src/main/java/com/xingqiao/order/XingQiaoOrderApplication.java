package com.xingqiao.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.xingqiao.common.security.annotation.EnableCustomConfig;
import com.xingqiao.common.security.annotation.EnableRyFeignClients;

/**
 * 数字货币模块
 * 
 * @author xingqiao
 */
@EnableCustomConfig
@EnableRyFeignClients
@SpringBootApplication
public class XingQiaoOrderApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(XingQiaoOrderApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  交易模块启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                " .-------.       ____     __        \n" +
                " |  _ _   \\      \\   \\   /  /    \n" +
                " | ( ' )  |       \\  _. /  '       \n" +
                " |(_ o _) /        _( )_ .'         \n" +
                " | (_,_).' __  ___(_ o _)'          \n" +
                " |  |\\ \\  |  ||   |(_,_)'         \n" +
                " |  | \\ `'   /|   `-'  /           \n" +
                " |  |  \\    /  \\      /           \n" +
                " ''-'   `'-'    `-..-'              ");
    }
}
