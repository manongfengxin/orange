package com.shuai.controller;


import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shuai.handler.NoAuth;
import com.shuai.pojo.po.Good;
import com.shuai.pojo.po.Order;
import com.shuai.pojo.vo.AliPay;
import com.shuai.service.GoodService;
import com.shuai.service.OrderService;
import com.shuai.util.Result;
import com.shuai.util.TimeUtil;
import org.apache.ibatis.ognl.Ognl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: fengxin
 * @CreateTime: 2023-07-14  20:39
 * @Description: 支付宝接口对接类
 */
@RestController
@RequestMapping("/alipay")
public class AliPayController {


    @Autowired
    private OrderService orderService;

    @Autowired
    private GoodService goodService;


    @NoAuth
    @GetMapping("/pay")
    public String pay(AliPay aliPay) {
        AlipayTradePagePayResponse response;
        try {
            //  发起API调用（以创建当面付收款二维码为例）
            response = Factory.Payment.Page().pay(aliPay.getSubject(), aliPay.getTraceNo(), aliPay.getTotalAmount(), "");
        } catch (Exception e) {
            System.err.println("调用遭遇异常，原因：" + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
        return response.getBody();
    }


    @NoAuth
    @PostMapping("/notify")  // 注意这里必须是POST接口
    public Result payNotify(HttpServletRequest request) throws Exception {
        if (request.getParameter("trade_status").equals("TRADE_SUCCESS")) {
            System.out.println("=========支付宝异步回调========");
            Map<String, String> params = new HashMap<>();
            Map<String, String[]> requestParams = request.getParameterMap();
            for (String name : requestParams.keySet()) {
                params.put(name, request.getParameter(name));
            }
            String tradeNo = params.get("out_trade_no");
            String gmtPayment = params.get("gmt_payment");
            // 支付宝验签
            if (Factory.Payment.Common().verifyNotify(params)) {
                // 验签通过
                System.out.println("交易名称: " + params.get("subject"));
                System.out.println("交易状态: " + params.get("trade_status"));
                System.out.println("支付宝交易凭证号: " + params.get("trade_no"));
                System.out.println("商户订单号: " + params.get("out_trade_no"));
                System.out.println("交易金额: " + params.get("total_amount"));
                System.out.println("买家在支付宝唯一id: " + params.get("buyer_id"));
                System.out.println("买家付款时间: " + params.get("gmt_payment"));
                System.out.println("买家付款金额: " + params.get("buyer_pay_amount"));

                // 更新订单未支付 -> 已支付
                Order order = orderService.getOne(
                        new LambdaQueryWrapper<Order>().eq(Order::getOrderNo, tradeNo));
                // 判断订单是否存在
                if (Objects.equals(order, null)) {
                    return Result.fail("支付错误！该订单不存在");
                }
                // 对应商品的销量 +1
                String goodId = order.getGoodId();
                Good good = goodService.getById(goodId);
                good.setGoodSales(good.getGoodSales() + 1);
                goodService.updateById(good);
                // 支付成功修改订单的状态（修改支付时间不为空）
                if (Objects.equals(order.getPayTime(), null)) {
                    order.setPayTime(gmtPayment);
                    orderService.updateById(order);
                } else {
                    return Result.fail("支付错误！该订单重复支付");
                }

            }else {
                return Result.fail("支付失败！支付宝验签不通过");
            }
        }
        return Result.success("支付成功");
    }

}
