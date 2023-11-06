package com.shuai.controller.admin;

import com.shuai.handler.UserThreadLocal;
import com.shuai.pojo.po.Post;
import com.shuai.pojo.vo.GoodVo;
import com.shuai.pojo.vo.PostVo;
import com.shuai.service.GoodService;
import com.shuai.service.PostService;
import com.shuai.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * @Author: fengxin
 * @CreateTime: 2023-08-13  07:51
 * @Description: 后台管理器
 */
@Slf4j
@RestController
@Transactional
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private PostService postService;

    /**
     * @description: 删除指定帖子，及其相关数据
     * @author: fengxin
     * @date: 2023/9/26 19:47
     * @param: [postId]
     * @return: 是否成功
     **/
    @PreAuthorize("hasAuthority('/admin/post/delete')")
    @DeleteMapping("/post/delete")
    public Result delete(@RequestBody PostVo postVo) {
        log.info("传入：{}",postVo.getPostId());
        // 0. 拿到当前用户id
        Long userId = UserThreadLocal.get().getId();
        // 1. 通过 postId 查询数据库,拿到指定帖子信息
        Post post = postService.getById(postVo.getPostId());
        // 2. 判断帖子是否存在
        if (Objects.equals(post,null)) {
            return Result.fail("删除失败，指定帖子不存在！");
        }
        // 3. 删除指定帖子
        return postService.delete(postVo.getPostId());
    }


}
