package com.xingqiao.system.api.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CustomerFinancial {
    private BigDecimal familyTotalAsset;
    private BigDecimal familyDebt;
    private BigDecimal familyAnnualIncome;
    private BigDecimal newInvestmentAmount;

}
