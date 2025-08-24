package com.xingqiao.system.api.domain;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Qualifications{
    private String qualificationType; // 资质类型（枚举，如 CFP）


    private String certificateNumber; // 资质证书编号


    private String issuingAuthority; // 颁发机构


    private Date certificateIssueDate; // 证书颁发日期


    private Date certificateExpiryDate; // 证书有效期截止日期


    private String yearsOfPractice; // 执业年限（字符串，兼容非数字输入）


    private List<String> certificateFileUrls; // 资质证书照片URL列表


    private List<String> practiceCertificateFileUrls; // 执业证明文件URL列表


    private Integer professionalConfirmed ; // 资质是否确认（默认未确认）


}
