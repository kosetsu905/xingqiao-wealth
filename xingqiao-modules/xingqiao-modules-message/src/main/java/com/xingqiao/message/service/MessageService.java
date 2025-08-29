package com.xingqiao.message.service;

import com.xingqiao.common.core.domain.R;
import com.xingqiao.message.api.domain.CodeReqDTO;
import com.xingqiao.message.api.domain.InviteReqDTO;

import javax.validation.Valid;


public interface MessageService {
    R sendCode(CodeReqDTO codeReqDTO);

    R<?> sendInviteMessageBatch(@Valid InviteReqDTO codeReqDTO);
}
