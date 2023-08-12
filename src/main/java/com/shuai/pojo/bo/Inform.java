package com.shuai.pojo.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: fengxin
 * @CreateTime: 2023-08-11  18:54
 * @Description: 系统通知消息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inform {

    // 发送者 id
    private Long fromId;

    // 接收者 id
    private Long toId;

    // 对象 id
    private String objectId;

    // 子对象 id
    private String subObjectId;

    // 首图
    private String firstImage;

    // 消息内容
    private String content;

}
