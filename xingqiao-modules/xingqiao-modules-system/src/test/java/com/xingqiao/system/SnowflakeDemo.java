package com.xingqiao.system;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.xingqiao.common.core.enums.TableIdEnums;

public class SnowflakeDemo {
    public static void main(String[] args) {
        // 0 表示 workerId（工作机器ID），0 表示 datacenterId（数据中心ID）
        Snowflake snowflake = IdUtil.createSnowflake(TableIdEnums.SYS_CUSTOMER_INFO.getWorkerId(), TableIdEnums.SYS_CUSTOMER_INFO.getDatacenterId());

        // 生成唯一ID
        long id = snowflake.nextId();
        System.out.println("生成的雪花ID: " + id);
    }
}
