package com.shuai.service;

import com.shuai.util.Result;

/**
 * @Author: fengxin
 * @CreateTime: 2023-08-11  16:18
 * @Description: 完成所有的收藏相关服务
 */
public interface CollectService {

    // 收藏指定帖子（二次点击取消收藏）
    Result collectPost(String postId);
}
