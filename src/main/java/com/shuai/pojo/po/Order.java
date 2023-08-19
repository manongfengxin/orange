package com.shuai.pojo.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class Order {

    // 主键（用户id + 时间）
    private Integer id;

    // 商品id
    private String goodId;

    // 商品标题
    private String goodTitle;

    // 商品描述
    private String goodDescription;

    // 商品单价
    private float goodPrice;

    // 用户id
    private Long userId;

    // 订单号
    private String orderNo;

    // 订单创建时间
    private String createTime;

    // 订单付款时间
    private String payTime;

}
