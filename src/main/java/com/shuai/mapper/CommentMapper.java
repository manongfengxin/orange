package com.shuai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shuai.pojo.po.Comment;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: fengxin
 * @CreateTime: 2023-08-08  12:08
 * @Description: 对应数据库 comment 表
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

}
