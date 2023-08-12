package com.shuai.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shuai.handler.UserThreadLocal;
import com.shuai.pojo.po.Footprint;
import com.shuai.pojo.po.Post;
import com.shuai.pojo.vo.PostVo;
import com.shuai.service.ConcernService;
import com.shuai.service.FootprintService;
import com.shuai.service.PostService;
import com.shuai.service.impl.PostServiceImpl;
import com.shuai.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: fengxin
 * @CreateTime: 2023-08-08  19:31
 * @Description: ”我的“面板 接口
 */
@Slf4j
@Transactional
@RestController
@RequestMapping("/my")
public class MyController {

    @Autowired
    private PostService postService;

    @Autowired
    private ConcernService concernService;


    /**
     * @description: 获取我的帖子列表
     * @author: fengxin
     * @date: 2023/8/9 8:38
     * @param: []
     * @return: 我的帖子列表信息
     **/
    @GetMapping("/getPostList")
    public Result getMyPostList() {
        return postService.getMyPostList();
    }

    /**
     * @description: 获取我的关注列表
     * @author: fengxin
     * @date: 2023/8/9 11:47
     * @param: []
     * @return: 一个集合{userId,nickname,avatar,description,concerned}
     * 注：concerned是对方是否关注自己
     **/
    @GetMapping("/getConcernList")
    public Result getConcernList() {
        return concernService.getConcernList();
    }

    /**
     * @description: 获取我的粉丝列表
     * @author: fengxin
     * @date: 2023/8/9 12:25
     * @param: []
     * @return: 一个集合{userId,nickname,avatar,description,concerned}
     * 注：concerned是自己是否关注对方
     **/
    @GetMapping("/getFansList")
    public Result getFansList() {
        return concernService.getFansList();
    }




}
