package com.xingqiao.coin.controller;

import com.xingqiao.common.core.web.domain.AjaxResult;
import com.xingqiao.coin.manager.WebsocketManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * WebSocket测试控制器
 * 
 * @author xingqiao
 */
@RestController
@RequestMapping("/digital/websocket")
public class WebsocketController {

    @Autowired
    private WebsocketManager websocketManager;

    /**
     * 发送消息给所有连接的客户端
     * 
     * @param message 消息内容
     * @return 结果
     */
    @PostMapping("/sendToAll")
    public AjaxResult sendToAll(@RequestParam String message) {
        websocketManager.sendToAll(message);
        return AjaxResult.success("发送成功");
    }

    /**
     * 获取当前在线人数
     * 
     * @return 在线人数
     */
    @GetMapping("/online")
    public AjaxResult online() {
        int count = websocketManager.getOnlineCount();
        return AjaxResult.success(count);
    }
}