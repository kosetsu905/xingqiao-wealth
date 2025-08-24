package com.xingqiao.system.api.enums;

import lombok.Getter;

@Getter
public enum CertificateFileEnums {

    //资质证书照片
    CERTIFICATE_FILE("01", "资质证书照片"),
    //执业证明文件
    PRACTICE_CERTIFICATE_FILE("02", "执业证明文件");

    private String code;
    private String message;
    CertificateFileEnums(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
