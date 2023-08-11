package com.shuai.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shuai.handler.UserThreadLocal;
import com.shuai.pojo.po.Footprint;
import com.shuai.pojo.po.Post;
import com.shuai.pojo.vo.PostVo;
import com.shuai.pojo.vo.UserVo;
import com.shuai.service.FootprintService;
import com.shuai.service.PostService;
import com.shuai.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: fengxin
 * @CreateTime: 2023-08-07  14:53
 * @Description: 帖子（论坛）管理
 */
@Slf4j
@RestController
@RequestMapping("/post")
public class PostController {

    @Value("${project.pageSize}")
    private String pageSize;

    @Autowired
    private PostService postService;

    @Autowired
    private FootprintService footprintService;


    /**
     * @description: 发布帖子
     * @author: fengxin
     * @date: 2023/8/8 9:14
     * @param: [postVo] = {postTitle, postContent, postType, images }
     * @return: 是否成功
     **/
    @PostMapping("/add")
    public Result add(@RequestBody PostVo postVo) {
        log.info("调用/post/add接口，传入：{}", postVo);
        return postService.add(postVo);
    }

    /**
     * @description: 修改帖子
     * @author: fengxin
     * @date: 2023/8/8 10:44
     * @param: [postVo] = {postId, postTitle, postContent, postType, images }
     * @return: 是否成功
     **/
    @PutMapping("/update")
    public Result put(@RequestBody PostVo postVo) {
        log.info("调用/post/update接口，传入：{}", postVo);
        return postService.put(postVo);
    }


    /**
     * @description: 最热帖子列表
     * @author: fengxin
     * @date: 2023/8/8 9:06
     * @param: [current]
     * @return: 帖子列表
     **/
    @GetMapping("/hottestList")
    public Result hottestList(@RequestParam("title")String title, @RequestParam(defaultValue = "1", name = "current") Integer current) {
        log.info("current==>{}",current);
        IPage<PostVo> page = postService.hottestList(title, new Page<Post>(current, Long.parseLong(pageSize)));
        return Result.success("最热帖子列表",page);
    }


    /**
     * @description: 最新帖子列表
     * @author: fengxin
     * @date: 2023/8/8 9:06
     * @param: [current]
     * @return: 帖子列表（包括列表长度）
     **/
    @GetMapping("/latestList")
    public Result latestList(@RequestParam("title")String title, @RequestParam(defaultValue = "1", name = "current") Integer current) {
        log.info("current==>{}",current);
        IPage<PostVo> page = postService.latestList(title, new Page<>(current, Long.parseLong(pageSize)));
        return Result.success("最新帖子列表",page);
    }

    /**
     * @description: 获取指定帖子详情信息
     * @author: fengxin
     * @date: 2023/8/8 19:43
     * @param: [postId]
     * @return: 帖子的详情信息，和作者的关注情况，对该篇帖子的点赞、收藏情况
     **/
    @GetMapping("/details")
    public Result details(@RequestParam("postId") String postId) {
        log.info("postId==>{}",postId);
        return postService.details(postId);
    }

    /**
     * @description: 获取我的足迹（帖子篇）
     * @author: fengxin
     * @date: 2023/8/9 15:32
     * @param: []
     * @return: 一个帖子信息列表
     **/
    @GetMapping("/getPostFootprint")
    public Result getPostFootprint() {
        ArrayList<PostVo> postVoList = new ArrayList<>();
        // 0. 拿到当前用户id
        Long userId = UserThreadLocal.get().getId();
        // 1. 通过用户id 查询帖子浏览记录
        List<Footprint> postFootprint = footprintService.list(
                new LambdaQueryWrapper<Footprint>()
                        .eq(Footprint::getUserId, userId)
                        .eq(Footprint::getPost, 1));
        // 2. 通过postId查询帖子的简要信息
        for (Footprint footprint :postFootprint) {
            PostVo postVo = postService.brief(footprint.getPostId());
            postVoList.add(postVo);
        }
        return Result.success("帖子浏览记录",postVoList);
    }



}
