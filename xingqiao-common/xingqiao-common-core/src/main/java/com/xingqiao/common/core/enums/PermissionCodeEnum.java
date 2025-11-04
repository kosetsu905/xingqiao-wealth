package com.xingqiao.common.core.enums;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限代码枚举
 */
public enum PermissionCodeEnum {

    // 市场权限
    A_SHARES("A_SHARES", "A股市场", PermissionTypeEnum.MARKET),
    B_SHARES("B_SHARES", "B股市场", PermissionTypeEnum.MARKET),
    HK_SHARES("HK_SHARES", "港股市场", PermissionTypeEnum.MARKET),
    US_SHARES("US_SHARES", "美股市场", PermissionTypeEnum.MARKET),
    CN_SHARES("CN_SHARES", "内地股市", PermissionTypeEnum.MARKET),
    TECH_BOARD("TECH_BOARD", "科技板", PermissionTypeEnum.MARKET),

    // 板块权限
    MAIN_BOARD("MAIN_BOARD", "主板市场", PermissionTypeEnum.BOARD),
    GEM_BOARD("GEM_BOARD", "创业板", PermissionTypeEnum.BOARD),
    STAR_MARKET("STAR_MARKET", "科创板", PermissionTypeEnum.BOARD),
    BEIJING_BOARD("BEIJING_BOARD", "北交所", PermissionTypeEnum.BOARD),
    SECOND_BOARD("SECOND_BOARD", "中小板", PermissionTypeEnum.BOARD),

    // 产品权限
    ETF("ETF", "交易型开放式指数基金", PermissionTypeEnum.PRODUCT),
    LOF("LOF", "上市型开放式基金", PermissionTypeEnum.PRODUCT),
    REITS("REITS", "房地产投资信托基金", PermissionTypeEnum.PRODUCT),
    WARRANT("WARRANT", "权证", PermissionTypeEnum.PRODUCT),
    BOND("BOND", "债券", PermissionTypeEnum.PRODUCT),
    CONVERTIBLE_BOND("CONVERTIBLE_BOND", "可转换债券", PermissionTypeEnum.PRODUCT),
    FUND("FUND", "基金", PermissionTypeEnum.PRODUCT),
    FUTURES("FUTURES", "期货", PermissionTypeEnum.PRODUCT),
    OPTION("OPTION", "期权", PermissionTypeEnum.PRODUCT),

    // 交易权限
    MARGIN_TRADE("MARGIN_TRADE", "融资融券", PermissionTypeEnum.TRADE),
    SHORT_SELL("SHORT_SELL", "卖空交易", PermissionTypeEnum.TRADE),
    IPO("IPO", "新股申购", PermissionTypeEnum.TRADE),
    PLACEMENT("PLACEMENT", "定向增发", PermissionTypeEnum.TRADE),
    BLOCK_TRADE("BLOCK_TRADE", "大宗交易", PermissionTypeEnum.TRADE),
    AFTER_HOURS("AFTER_HOURS", "盘后交易", PermissionTypeEnum.TRADE),

    // 证券类型权限
    PREFERRED_SHARES("PREFERRED_SHARES", "优先股", PermissionTypeEnum.SECURITY),
    RESTRICTED_SHARES("RESTRICTED_SHARES", "限售股", PermissionTypeEnum.SECURITY),
    GROWTH_SHARES("GROWTH_SHARES", "成长股", PermissionTypeEnum.SECURITY),
    BLUE_CHIP("BLUE_CHIP", "蓝筹股", PermissionTypeEnum.SECURITY),
    SMALL_CAP("SMALL_CAP", "小盘股", PermissionTypeEnum.SECURITY);

    private final String code;
    private final String description;
    private final PermissionTypeEnum permissionType;

    PermissionCodeEnum(String code, String description, PermissionTypeEnum permissionType) {
        this.code = code;
        this.description = description;
        this.permissionType = permissionType;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public PermissionTypeEnum getPermissionType() {
        return permissionType;
    }

    public static PermissionCodeEnum getByCode(String code) {
        for (PermissionCodeEnum permission : values()) {
            if (permission.getCode().equals(code)) {
                return permission;
            }
        }
        return null;
    }

    /**
     * 根据权限类型获取对应的权限代码枚举
     */
    public static List<PermissionCodeEnum> getByPermissionType(PermissionTypeEnum permissionType) {
        return Arrays.stream(values())
                .filter(permission -> permission.getPermissionType() == permissionType)
                .collect(Collectors.toList());
    }
}
