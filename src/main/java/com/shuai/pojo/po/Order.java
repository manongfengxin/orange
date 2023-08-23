package com.shuai.pojo.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author: fengxin
 * @CreateTime: 2023-08-04  12:14
 * @Description: 订单表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("`order`")
public class Order {

    // 主键（用户id + 时间）
    private String id;

    // 商品id
    private String goodId;

    // 商品数量
    private Integer goodNumber;

    // 用户id
    private Long userId;

    // 订单号
    private String orderNo;

    // 订单创建时间
    private String createTime;

    // 订单付款时间
    private String payTime;


}
