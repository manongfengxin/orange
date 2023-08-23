package com.shuai.controller;

import com.shuai.service.LikeService;
import com.shuai.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: fengxin
 * @CreateTime: 2023-08-11  12:08
 * @Description: 点赞管理器
 */
@Slf4j
@RestController
@Transactional
@RequestMapping("/like")
public class LikeController {

    @Autowired
    private LikeService likeService;


    /**
     * @description: 点赞指定帖子（二次点击取消点赞）
     * @author: fengxin
     * @date: 2023/8/11 13:04
     * @param: [postId]
     * @return: 是否成功
     **/
    @PutMapping("/post")
    public Result likePost(@RequestParam("postId")String postId) {
        return likeService.likePost(postId);
    }

    /**
     * @description: 点赞指定评论（二次点击取消点赞）
     * @author: fengxin
     * @date: 2023/8/11 14:41
     * @param: [commentId]
     * @return: 是否成功
     **/
    @PutMapping("/comment")
    public Result likeComment(@RequestParam("commentId")String commentId) {
        return likeService.likeComment(commentId);
    }


    /**
     * @description: 点赞指定商品评价（二次点击取消点赞）
     * @author: fengxin
     * @date: 2023/8/11 15:45
     * @param: [reviewId]
     * @return: 是否成功
     **/
    @PutMapping("/review")
    public Result likeReview(@RequestParam("reviewId")String reviewId) {
        return likeService.likeReview(reviewId);
    }
}
