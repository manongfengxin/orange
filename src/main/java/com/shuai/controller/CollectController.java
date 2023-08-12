package com.shuai.controller;

import com.shuai.service.CollectService;
import com.shuai.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: fengxin
 * @CreateTime: 2023-08-11  12:09
 * @Description: 收藏管理器
 */
@Slf4j
@RestController
@RequestMapping("/collect")
public class CollectController {

    @Autowired
    private CollectService collectService;


    /**
     * @description: 收藏指定帖子（二次点击取消收藏）
     * @author: fengxin
     * @date: 2023/8/11 16:20
     * @param: [postId]
     * @return: 是否成功
     **/
    @PostMapping("/post")
    public Result collectPost(@RequestParam("postId")String postId) {
        return collectService.collectPost(postId);
    }

}
