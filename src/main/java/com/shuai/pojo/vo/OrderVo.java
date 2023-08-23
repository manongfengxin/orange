package com.shuai.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderVo {

    // 主键（用户id + 时间）
    private String orderId;

    // 商品id
    private String goodId;



    // 商品标题
    private String goodName;

    // 商品描述
    private String goodDescription;

    // 商品单价
    private float goodPrice;

    // 商品首图
    private String goodFirstPicture;



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
