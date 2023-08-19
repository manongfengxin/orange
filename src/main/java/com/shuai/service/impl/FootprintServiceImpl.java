package com.shuai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shuai.handler.UserThreadLocal;
import com.shuai.mapper.FootprintMapper;
import com.shuai.pojo.po.Footprint;
import com.shuai.service.FootprintService;
import com.shuai.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: fengxin
 * @CreateTime: 2023-08-09  14:14
 * @Description: TODO
 */
@Service
@Transactional
public class FootprintServiceImpl extends ServiceImpl<FootprintMapper,Footprint> implements FootprintService {

    @Autowired
    private FootprintMapper footprintMapper;

//
//    @Override
//    public Result getPostFootprint() {
//
//        return null;
//    }
//
//    @Override
//    public Result getGoodFootprint() {
//        // 0. 拿到当前用户id
//        Long userId = UserThreadLocal.get().getId();
//        // 1. 通过用户id 查询商品浏览记录
//
//
//
//        return null;
//    }
}
