package com.xingqiao.system.api.enums;

public enum QualificationType {
    CFP("CFP", "金融理财师(CFP)"),
    CFA("CFA", "特许金融分析师(CFA)"),
    CPA("CPA", "注册会计师(CPA)"),
    OTHER("other", "其他资质");

    private final String code; // 编码（对应JSON中的值）
    private final String name; // 名称

    QualificationType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    // getter 方法
    public String getCode() { return code; }
    public String getName() { return name; }
}