package com.shuai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shuai.mapper.OrderMapper;
import com.shuai.mapper.PostMapper;
import com.shuai.pojo.po.Order;
import com.shuai.pojo.po.Post;
import com.shuai.service.OrderService;
import com.shuai.service.PostService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: fengxin
 * @CreateTime: 2023-08-16  10:12
 * @Description: TODO
 */
@Service
@Transactional
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {







}
