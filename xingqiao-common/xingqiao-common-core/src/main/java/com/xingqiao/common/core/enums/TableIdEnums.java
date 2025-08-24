package com.xingqiao.common.core.enums;

//表id 雪花算法配置枚举
public enum TableIdEnums {
    SYS_CUSTOMER_INFO("system", "sys_customer_info", 1, 1),
    SALES_OPPORTUNITY("system", "sales_opportunity", 2, 1),
    ;
    private TableIdEnums(String module, String tableName, long workerId, long datacenterId) {
        this.module = module;
        this.tableName = tableName;
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    //模块
    private String module;
    //表名
    private String tableName;
    //workerId
    private long workerId ;
    //datacenterId
    private long datacenterId;

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(long workerId) {
        this.workerId = workerId;
    }

    public long getDatacenterId() {
        return datacenterId;
    }

    public void setDatacenterId(long datacenterId) {
        this.datacenterId = datacenterId;
    }
}
