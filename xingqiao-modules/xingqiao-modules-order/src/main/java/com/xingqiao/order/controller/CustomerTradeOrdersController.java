package com.xingqiao.order.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.math.BigDecimal;

import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.xingqiao.common.log.annotation.Log;
import com.xingqiao.common.log.enums.BusinessType;
import com.xingqiao.common.security.annotation.RequiresPermissions;
import com.xingqiao.common.security.utils.SecurityUtils;
import com.xingqiao.order.domain.CustomerTradeOrders;
import com.xingqiao.order.service.trade.CustomerTradeOrdersService;
import com.xingqiao.common.core.web.controller.BaseController;
import com.xingqiao.common.core.web.domain.AjaxResult;
import com.xingqiao.common.core.utils.poi.ExcelUtil;
import com.xingqiao.common.core.web.page.TableDataInfo;
import com.xingqiao.order.service.trade.IOrderMessageService;
import com.xingqiao.common.rocketmq.service.RocketMQMessageService;
import lombok.extern.slf4j.Slf4j;

/**
 * 交易订单Controller
 * 
 * @author xingqiao
 * @date 2025-09-21
 */
@Slf4j
@RestController
@RequestMapping("/trade")
public class CustomerTradeOrdersController extends BaseController
{
    @Autowired
    private CustomerTradeOrdersService customerTradeOrdersService;
    
    @Autowired
    private RocketMQMessageService rocketMQMessageService;
    
    @Autowired
    private IOrderMessageService orderMessageService;

    // /**
    //  * 查询交易订单列表
    //  */
    // //@RequiresPermissions("order:trade:list")
    // @GetMapping("/list")
    // public TableDataInfo list(CustomerTradeOrders customerTradeOrders)
    // {
    //     startPage();
    //     List<CustomerTradeOrders> list = customerTradeOrdersService.selectCustomerTradeOrdersList(customerTradeOrders);
    //     return getDataTable(list);
    // }

    // /**
    //  * 导出交易订单列表
    //  */
    // //@RequiresPermissions("order:trade:export")
    // @Log(title = "交易订单", businessType = BusinessType.EXPORT)
    // @PostMapping("/export")
    // public void export(HttpServletResponse response, CustomerTradeOrders customerTradeOrders) throws IOException
    // {
    //     List<CustomerTradeOrders> list = customerTradeOrdersService.selectCustomerTradeOrdersList(customerTradeOrders);
    //     ExcelUtil<CustomerTradeOrders> util = new ExcelUtil<CustomerTradeOrders>(CustomerTradeOrders.class);
    //     util.exportExcel(response, list, "交易订单数据");
    // }

    // /**
    //  * 获取交易订单详细信息
    //  */
    // //@RequiresPermissions("order:trade:query")
    // @GetMapping(value = "{id}")
    // public AjaxResult getInfo(@PathVariable("id") String id)
    // {
    //     return AjaxResult.success(customerTradeOrdersService.selectCustomerTradeOrdersById(id));
    // }

    // /**
    //  * 新增交易订单
    //  */
    // //@RequiresPermissions("order:trade:add")
    // @Log(title = "交易订单", businessType = BusinessType.INSERT)
    // @PostMapping
    // public AjaxResult add(@RequestBody CustomerTradeOrders customerTradeOrders)
    // {
    //     return toAjax(customerTradeOrdersService.insertCustomerTradeOrders(customerTradeOrders));
    // }

    // /**
    //  * 修改交易订单
    //  */
    // //@RequiresPermissions("order:trade:edit")
    // @Log(title = "交易订单", businessType = BusinessType.UPDATE)
    // @PutMapping
    // public AjaxResult edit(@RequestBody CustomerTradeOrders customerTradeOrders)
    // {
    //     return toAjax(customerTradeOrdersService.updateCustomerTradeOrders(customerTradeOrders));
    // }

    // /**
    //  * 删除交易订单
    //  */
    // //@RequiresPermissions("order:trade:remove")
    // @Log(title = "交易订单", businessType = BusinessType.DELETE)
    // @DeleteMapping("{ids}")
    // public AjaxResult remove(@PathVariable String[] ids)
    // {
    //     return toAjax(customerTradeOrdersService.deleteCustomerTradeOrdersByIds(ids));
    // }

    // /**
    //  * 根据用户ID查询交易订单
    //  */
    // @GetMapping("/user/{userId}")
    // public TableDataInfo getByUserId(@PathVariable("userId") String userId)
    // {
    //     List<CustomerTradeOrders> list = customerTradeOrdersService.selectCustomerTradeOrdersByUserId(userId);
    //     return getDataTable(list);
    // }

    // /**
    //  * 根据账户ID查询交易订单
    //  */
    // @GetMapping("/account/{accountId}")
    // public TableDataInfo getByAccountId(@PathVariable("accountId") String accountId)
    // {
    //     List<CustomerTradeOrders> list = customerTradeOrdersService.selectCustomerTradeOrdersByAccountId(accountId);
    //     return getDataTable(list);
    // }

    // /**
    //  * 获取用户今日盈亏
    //  * 
    //  * @return 今日盈亏金额
    //  */
    // @GetMapping("/today-profit-loss")
    // public AjaxResult getTodayProfitLossByToken()
    // {
    //     try {
    //         // 从token中获取当前登录用户ID
    //         Long userId = SecurityUtils.getUserId();
    //         if (userId == null || userId == 0) {
    //             return AjaxResult.error("用户未登录");
    //         }
            
    //         BigDecimal profitLoss = customerTradeOrdersService.getTodayProfitLossByUserId(userId.toString());
    //         return AjaxResult.success(profitLoss);
    //     } catch (Exception e) {
    //         log.error("获取今日盈亏异常", e);
    //         return AjaxResult.error("获取今日盈亏失败：" + e.getMessage());
    //     }
    // }
    
    // /**
    //  * 获取用户今日盈亏（保留原有接口，向后兼容）
    //  * 
    //  * @param userId 用户ID
    //  * @return 今日盈亏金额
    //  */
    // @GetMapping("/today-profit-loss/{userId}")
    // public AjaxResult getTodayProfitLossByUserId(@PathVariable("userId") String userId)
    // {
    //     try {
    //         BigDecimal profitLoss = customerTradeOrdersService.getTodayProfitLossByUserId(userId);
    //         return AjaxResult.success(profitLoss);
    //     } catch (Exception e) {
    //         return AjaxResult.error("获取今日盈亏失败：" + e.getMessage());
    //     }
    // }

    /**
     * 下单交易
     * 
     * @param securityId 证券ID
     * @param orderType 订单类型: 1-限价单, 2-市价单, 3-条件单
     * @param direction 买卖方向: 1-买入, 2-卖出
     * @param price 委托价格(市价单可为0)
     * @param quantity 委托数量
     * @param accountId 资金账户ID
     * @param remark 备注
     * @return 订单ID和FtTrdService下单结果
     */
    @PostMapping("/place-order")
    public AjaxResult placeOrder(
            @RequestParam String securityId,
            @RequestParam Long orderType,
            @RequestParam Long direction,
            @RequestParam BigDecimal price,
            @RequestParam BigDecimal quantity,
            @RequestParam(required = false) String accountId,
            @RequestParam(required = false) String remark) {
        try {
            // 从token中获取当前登录用户ID
            Long userId = SecurityUtils.getUserId();
            if (userId == null || userId == 0) {
                return AjaxResult.error("用户未登录");
            }
            
            // 创建订单对象
            CustomerTradeOrders order = new CustomerTradeOrders();
            order.setUserId(userId.toString());
            order.setSecurityId(securityId);
            order.setOrderType(orderType);
            order.setDirection(direction);
            order.setPrice(price);
            order.setQuantity(quantity);
            order.setAccountId(accountId);
            order.setRemark(remark);
            
            // 设置订单基本信息
            order.setOrderTime(new Date());
            order.setStatus(0L); // 0-待报状态
            order.setRiskChecked(0); // 初始未通过风控检查
            order.setVersion(1L);
            
            // 计算预估金额
            BigDecimal estimatedAmount = price.multiply(quantity);
            order.setAmount(estimatedAmount);
            
            // 调用service层创建订单
            int rows = customerTradeOrdersService.insertCustomerTradeOrders(order);
            if (rows > 0) {
                log.info("用户[{}]下单成功，订单ID: {}", userId, order.getId());
                
                try {
                    // 发送RocketMQ消息，通知订单创建成功
                    // 实际下单操作将由OrderCreateListener监听到消息后异步调用FtTrdService完成
                    Long accountIdLong = accountId != null ? Long.parseLong(accountId) : null;
                    orderMessageService.sendOrderCreateMessage(order, userId, securityId, 
                                                             direction, price, quantity, 
                                                             orderType, accountIdLong);
                    
                    // 返回系统订单ID，富途下单结果将通过异步方式处理
                    return AjaxResult.success("下单请求已受理", order.getId());
                } catch (Exception e) {
                    log.error("发送RocketMQ消息异常", e);
                    // 即使消息发送失败，系统内订单仍然创建成功，这里返回警告信息
                    return AjaxResult.success("系统订单创建成功，但通知消息发送异常", order.getId());
                }
            } else {
                log.error("用户[{}]下单失败", userId);
                return AjaxResult.error("下单失败");
            }
        } catch (Exception e) {
            log.error("下单异常", e);
            return AjaxResult.error("下单失败：" + e.getMessage());
        }
    }

    // /**
    //  * 查询订单是否已完成
    //  * 
    //  * @param orderId 订单ID
    //  * @return 是否已完成交易的信息
    //  */
    // @GetMapping("/is-completed/{orderId}")
    // public AjaxResult isOrderCompleted(@PathVariable String orderId) {
    //     try {
    //         // 从token中获取当前登录用户ID
    //         Long userId = SecurityUtils.getUserId();
    //         if (userId == null || userId == 0) {
    //             return AjaxResult.error("用户未登录");
    //         }
            
    //         // 根据订单ID查询订单详情
    //         CustomerTradeOrders order = customerTradeOrdersService.selectCustomerTradeOrdersById(orderId);
    //         if (order == null) {
    //             return AjaxResult.error("订单不存在");
    //         }
            
    //         // 验证订单所属用户，确保数据安全
    //         if (!order.getUserId().equals(userId.toString())) {
    //             return AjaxResult.error("无权查询该订单");
    //         }
            
    //         // 判断订单状态是否为完全成交(3)
    //         boolean isCompleted = order.getStatus() == 3;
            
    //         // 构建返回结果
    //         Map<String, Object> result = new HashMap<>();
    //         result.put("orderId", orderId);
    //         result.put("isCompleted", isCompleted);
    //         result.put("status", order.getStatus());
    //         result.put("statusText", getStatusText(order.getStatus()));
            
    //         // 如果已完成，返回成交信息
    //         if (isCompleted) {
    //             result.put("filledQuantity", order.getFilledQuantity());
    //             result.put("filledAmount", order.getFilledAmount());
    //             result.put("avgFilledPrice", order.getAvgFilledPrice());
    //             result.put("finishTime", order.getFinishTime());
    //         }
            
    //         return AjaxResult.success(result);
    //     } catch (Exception e) {
    //         log.error("查询订单完成状态异常", e);
    //         return AjaxResult.error("查询失败：" + e.getMessage());
    //     }
    // }

    // /**
    //  * 获取订单状态对应的文本描述
    //  * 
    //  * @param status 订单状态值
    //  * @return 状态文本描述
    //  */
    // private String getStatusText(Long status) {
    //     switch (status.intValue()) {
    //         case 0: return "待报";
    //         case 1: return "已报";
    //         case 2: return "部分成交";
    //         case 3: return "完全成交";
    //         case 4: return "部分撤单";
    //         case 5: return "完全撤单";
    //         case 6: return "废单";
    //         default: return "未知状态";
    //     }
    // }
}