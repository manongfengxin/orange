package com.shuai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shuai.handler.UserThreadLocal;
import com.shuai.mapper.GoodMapper;
import com.shuai.mapper.OrderMapper;
import com.shuai.mapper.PostMapper;
import com.shuai.pojo.po.Good;
import com.shuai.pojo.po.Order;
import com.shuai.pojo.po.Post;
import com.shuai.pojo.vo.OrderVo;
import com.shuai.pojo.vo.PostVo;
import com.shuai.service.OrderService;
import com.shuai.service.PostService;
import com.shuai.util.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Author: fengxin
 * @CreateTime: 2023-08-16  10:12
 * @Description: TODO
 */
@Service
@Transactional
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private GoodMapper goodMapper;

    @Override
    public Result orderList(Page<Order> page) {
        List<OrderVo> records = new ArrayList<>();
        // 0. 拿到当前用户id
        Long userId = UserThreadLocal.get().getId();
        // 1. 通过用户id查询 order 表
        Page<Order> orderPage = orderMapper.selectPage(page,
                new LambdaQueryWrapper<Order>()
                        .eq(Order::getUserId,userId));
        // 2. 通过订单对应的商品id，查询订单列表每一条订单的商品标题、描述、单价、首图
        for (Order order :orderPage.getRecords()) {
            // 2.1 查询数据库
            Good good = goodMapper.selectById(order.getGoodId());
            // 2.2 order 赋值给 orderVo
            OrderVo orderVo = new OrderVo();
            BeanUtils.copyProperties(order,orderVo);
            orderVo.setOrderId(order.getId());
            // 2.3 good 赋值给 orderVo
            orderVo.setGoodName(good.getGoodName());
            orderVo.setGoodDescription(good.getGoodDescription());
            orderVo.setGoodPrice(good.getGoodPrice());
            orderVo.setGoodFirstPicture(good.getGoodFirstPicture());
            // 3. 加入 records 中，返回前端
            records.add(orderVo);
        }
        Page<OrderVo> orderVoPage = new Page<>();
        // 4. 给返回值 orderVoPage 赋值
        orderVoPage.setRecords(records);
        BeanUtils.copyProperties(orderPage,orderVoPage,"records");
        return Result.success("我的订单列表（分页）",orderVoPage);
    }








}
