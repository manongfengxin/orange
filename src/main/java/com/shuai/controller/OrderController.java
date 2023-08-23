package com.shuai.controller;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shuai.handler.UserThreadLocal;
import com.shuai.pojo.po.Good;
import com.shuai.pojo.po.Order;
import com.shuai.pojo.po.User;
import com.shuai.pojo.vo.OrderVo;
import com.shuai.service.GoodService;
import com.shuai.service.OrderService;
import com.shuai.service.UserService;
import com.shuai.util.Result;
import com.shuai.util.TimeUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private GoodService goodService;

    @Value("${project.pageSize}")
    private String pageSize;


    /**
     * @description: 获取我的订单列表（分页）
     * @author: fengxin
     * @date: 2023/8/20 16:14
     * @param: [current]
     * @return: 订单列表（分页）
     **/
    @GetMapping("/list")
    public Result getUserOrders(@RequestParam(defaultValue = "1", name = "current") Integer current) {
        return orderService.orderList(new Page<>(current, Long.parseLong(pageSize)));
    }

    /**
     * @description: 通过订单id查询订单详情
     * @author: fengxin
     * @date: 2023/8/20 16:17
     * @param: [orderId]
     * @return: 订单详情
     **/
    @GetMapping("/details")
    public Result getOrderById(@RequestParam("orderId") String orderId) {
        // 1. 通过订单id查询订单信息，通过商品id查询商品信息
        Order order = orderService.getById(orderId);
        Good good = goodService.getById(order.getGoodId());
        // 2. 构建返回值 orderVo
        OrderVo orderVo = new OrderVo();
        BeanUtils.copyProperties(order, orderVo);
        orderVo.setOrderId(orderId);
        orderVo.setGoodName(good.getGoodName());
        orderVo.setGoodDescription(good.getGoodDescription());
        orderVo.setGoodPrice(good.getGoodPrice());
        orderVo.setGoodFirstPicture(good.getGoodFirstPicture());
        return Result.success("订单的详细信息", orderVo);
    }


    /**
     * @description: 删除指定订单
     * @author: fengxin
     * @date: 2023/8/21 14:18
     * @param: [orderId]
     * @return: 是否成功
     **/
    @DeleteMapping("/delete")
    public Result deleteOrderById(@RequestParam("orderId") String orderId) {
        // 0. 拿到当前用户id
        Long userId = UserThreadLocal.get().getId();
        // 1. 查询改订单是否存在
        Order order = orderService.getById(orderId);
        if (Objects.equals(order, null)) {
            return Result.fail("指定订单不存在！");
        }
        // 2. 查询是否是该订单所有者（检测是否有删除权限）
        if (!Objects.equals(order.getUserId(), userId)) {
            return Result.fail("没有删除该订单的权限");
        }
        // 3. 删除订单
        boolean remove = orderService.removeById(orderId);
        if (remove) {
            return Result.success("删除订单成功!");
        } else {
            return Result.fail("删除订单失败!请联系后台");
        }
    }

    /**
     * @description: 购买指定商品
     * @author: fengxin
     * @date: 2023/8/21 14:35
     * @param: [goodId, goodNumber]
     * @return: 后端支付宝接口的 url（前端直接访问）
     **/
    @GetMapping("/buy")
    public Result buy(@RequestParam("goodId") String goodId, @RequestParam("goodNumber") Integer goodNumber) {
        // 0. 拿到当前用户id
        Long userId = UserThreadLocal.get().getId();
        // 1. 通过商品id获取商品信息
        Good good = goodService.getById(goodId);
        // 2. 随机生成订单号（使用了 Hutool 工具包中的IdUtil类，调用IdUtil.getSnowflake().nextIdStr()可以生成一个Snowflake算法生成的唯一ID）
        String orderNo = IdUtil.getSnowflake().nextIdStr();
        // 3. 构建一个 url：前端可直接访问这个url，调用后端支付宝支付接口
        String payUrl = "http://localhost:5901/alipay/pay?subject=" + good.getGoodName() + good.getGoodDescription() + "&traceNo=" + orderNo + "&totalAmount=" + good.getGoodPrice() * goodNumber;
        // 4. 新增一条订单信息
        Order order = new Order(userId + TimeUtil.getNowTimeString(), goodId, goodNumber, userId, orderNo, TimeUtil.getNowTime(), null);
        orderService.save(order);
        return Result.success("调用后端支付宝接口", payUrl);
    }
}