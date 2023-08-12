package com.shuai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shuai.common.Constants;
import com.shuai.controller.ChatEndpoint;
import com.shuai.handler.UserThreadLocal;
import com.shuai.mapper.PostCollectMapper;
import com.shuai.mapper.PostMapper;
import com.shuai.pojo.bo.Inform;
import com.shuai.pojo.po.Post;
import com.shuai.pojo.po.PostCollect;
import com.shuai.pojo.po.PostLike;
import com.shuai.service.CollectService;
import com.shuai.util.Result;
import com.shuai.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @Author: fengxin
 * @CreateTime: 2023-08-11  16:19
 * @Description: TODO
 */
@Service
@Transactional
public class CollectServiceImpl implements CollectService {

    @Autowired
    private PostCollectMapper postCollectMapper;

    @Autowired
    private PostMapper postMapper;

    @Override
    public Result collectPost(String postId) {
        // 0. 拿到当前用户id
        Long userId = UserThreadLocal.get().getId();
        // 1. 通过 postId 查询对应帖子 并 确实帖子是否存在
        Post post = postMapper.selectById(postId);
        if (Objects.equals(post,null)) {
            return Result.fail("收藏失败！该帖子不存在");
        }
        // 2. 查询数据库是否已有点赞记录（deleted可能是1 or 0）
        PostCollect postCollectInfo = postCollectMapper.selectOne(
                new LambdaQueryWrapper<PostCollect>()
                        .eq(PostCollect::getUserId, userId)
                        .eq(PostCollect::getPostId, postId));
        // 3. 无记录：添加新记录，有记录：deleted为0则变1、为1则变0
        if (Objects.equals(postCollectInfo,null)) {
            // 3.1 收藏对应帖子（新增插入数据）
            String id = userId + postId;
            PostCollect postCollect =
                    new PostCollect(id,userId,postId,0, TimeUtil.getNowTime());
            int insert = postCollectMapper.insert(postCollect);
            if (insert > 0){
                // 4. 给帖子作者发送某人收藏了他的帖子 的 在线系统通知
                ChatEndpoint.sendInfo(/* fromId, toId, objectId, sonObjectId, firstImage, content*/
                        new Inform(userId,post.getAuthorId(),postId,null,
                                post.getPostFirstPicture(), Constants.COLLECT_POST));
                return Result.success("收藏帖子成功！");
            }else {
                return Result.fail("收藏帖子失败，联系后台");
            }
        }else {
            // 3.2 修改记录，deleted为0则变1、为1则变0
            if(postCollectInfo.getDeleted() > 0) {
                postCollectInfo.setDeleted(0);
                // 注：需填入新的点赞时间
                postCollectInfo.setCollectTime(TimeUtil.getNowTime());
                postCollectMapper.updateById(postCollectInfo);
                return Result.success("收藏帖子成功！");
            }else {
                postCollectInfo.setDeleted(1);
                postCollectMapper.updateById(postCollectInfo);
                // 4. 给帖子作者发送某人收藏了他的帖子 的 在线系统通知
                ChatEndpoint.sendInfo(/* fromId, toId, objectId, sonObjectId, firstImage, content*/
                        new Inform(userId,post.getAuthorId(),postId,null,
                                post.getPostFirstPicture(), Constants.COLLECT_POST));
                return Result.success("取消收藏帖子成功！");
            }
        }
    }
}
