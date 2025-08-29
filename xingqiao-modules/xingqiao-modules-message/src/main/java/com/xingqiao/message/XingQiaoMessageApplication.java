package com.xingqiao.message;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.xingqiao.common.security.annotation.EnableCustomConfig;
import com.xingqiao.common.security.annotation.EnableRyFeignClients;

/**
 * 系统模块
 *
 * @author xingqiao
 */
@EnableCustomConfig
@EnableRyFeignClients
@SpringBootApplication
public class XingQiaoMessageApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(XingQiaoMessageApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  信息模块启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
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
