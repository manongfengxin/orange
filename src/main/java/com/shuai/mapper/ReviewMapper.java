package com.shuai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shuai.pojo.po.Review;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: fengxin
 * @CreateTime: 2023-08-11  15:48
 * @Description: 访问 review 表
 */
@Mapper
public interface ReviewMapper extends BaseMapper<Review> {
}
