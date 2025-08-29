package com.xingqiao.message.api.domain;

import com.xingqiao.message.api.annotation.PhoneOrEmailRequired;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;


@Data
public class InviteReqDTO {

    /**
     * id
     */
    private List<Long> idList;


    /**
     * 类型，1：发送手机短信邀请，2：发送邮箱邀请
     */
    private String type;


}
