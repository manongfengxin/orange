package com.shuai.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shuai.pojo.po.Order;
import com.shuai.util.Result;

/**
 * @Author: fengxin
 * @CreateTime: 2023-08-16  10:12
 * @Description: TODO
 */
public interface OrderService extends IService<Order> {

    // 获取我的订单列表
    Result orderList(Page<Order> page);
}
