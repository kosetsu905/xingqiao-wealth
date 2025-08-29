package com.xingqiao.message.controller;


import com.xingqiao.common.core.domain.R;
import com.xingqiao.common.core.web.controller.BaseController;
import com.xingqiao.message.api.domain.CodeReqDTO;
import com.xingqiao.message.api.domain.InviteReqDTO;
import com.xingqiao.message.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@Slf4j
public class MessageController extends BaseController {

    @Resource
    private MessageService messageService;

    /**
     * 发验证码接口
     */
    @PostMapping("/sendCode")
    public R<?> sendCode(@RequestBody @Valid CodeReqDTO codeReqDTO) {
        return messageService.sendCode(codeReqDTO);
    }


    /**
     * 发验注册邀请接口
     */
    @PostMapping("/sendInviteMessageBatch")
    public R<?> sendInviteMessageBatch(@RequestBody @Valid InviteReqDTO codeReqDTO) {
        return messageService.sendInviteMessageBatch(codeReqDTO);
    }
}
