package com.xingqiao.system.mapper;

import com.xingqiao.system.api.domain.CommonUser;
import io.lettuce.core.dynamic.annotation.Param;

public interface CUserExtMapper {
    CommonUser checkPhoneUnique(@Param("phoneNumber")  String phoneNumber,@Param("userType")  String userType);

    CommonUser checkEmailUnique(@Param("email")  String email,@Param("userType")  String userType);

    CommonUser checkAccountUnique(@Param("account") String account,@Param("userType")  String userType);

    CommonUser selectCUserByAccount(@Param("account") String account,@Param("userType") String userType);
}
