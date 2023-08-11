package com.shuai.controller;


import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import com.shuai.pojo.vo.AliPay;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: fengxin
 * @CreateTime: 2023-07-14  20:39
 * @Description: 支付宝接口对接类
 */
//@RestController
//@RequestMapping("/alipay")
//public class AliPayController {
//
//
//
//
//
//
//
//    @GetMapping("/pay")
//    public String pay(AliPay aliPay) {
//        AlipayTradePagePayResponse response;
//        try {
//            //  发起API调用（以创建当面付收款二维码为例）
//            response = Factory.Payment.Page().pay(aliPay.getSubject(), aliPay.getTraceNo(), aliPay.getTotalAmount(), "");
//        } catch (Exception e) {
//            System.err.println("调用遭遇异常，原因：" + e.getMessage());
//            throw new RuntimeException(e.getMessage(), e);
//        }
//        return response.getBody();
//    }
//
//
//    @PostMapping("/notify")  // 注意这里必须是POST接口
//    public String payNotify(HttpServletRequest request, @RequestHeader String token) throws Exception {
//        if (request.getParameter("trade_status").equals("TRADE_SUCCESS")) {
//            System.out.println("=========支付宝异步回调========");
//            Map<String, String> params = new HashMap<>();
//            Map<String, String[]> requestParams = request.getParameterMap();
//            for (String name : requestParams.keySet()) {
//                params.put(name, request.getParameter(name));
//            }
//            String tradeNo = params.get("out_trade_no");
//            String gmtPayment = params.get("gmt_payment");
//            // 支付宝验签
//            if (Factory.Payment.Common().verifyNotify(params)) {
//                // 验签通过
//                System.out.println("交易名称: " + params.get("subject"));
//                System.out.println("交易状态: " + params.get("trade_status"));
//                System.out.println("支付宝交易凭证号: " + params.get("trade_no"));
//                System.out.println("商户订单号: " + params.get("out_trade_no"));
//                System.out.println("交易金额: " + params.get("total_amount"));
//                System.out.println("买家在支付宝唯一id: " + params.get("buyer_id"));
//                System.out.println("买家付款时间: " + params.get("gmt_payment"));
//                System.out.println("买家付款金额: " + params.get("buyer_pay_amount"));
//                // 更新订单未支付 -> 已支付
//                Order order = orderService.selectByOrder(tradeNo);
//                if (order != null) {
//                    Integer goodsId = order.getGoodsId();
//                    Goods goods = goodsService.getById(goodsId);
//                    goods.setSum(goods.getSum() - 1);
//                    goodsService.updateById(goods);
//                    orderMapper.updateState(tradeNo, 1, gmtPayment);
//                } else {
//                    Integer userId = JwtHelper.getUserId(token);
////                    Integer userId = 1;
//                    User user = userService.getById(userId);
//                    String totalAmount = params.get("total_amount");
//                    Integer coins = (int) Double.parseDouble(totalAmount) * 100;
//                    System.out.println(coins);
//                    user.setCoins(user.getCoins() + coins);
//                    userService.updateById(user);
//                }
//            }
//        }
//        return "success";
//    }
//
//}
