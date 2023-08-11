package com.shuai.pojo.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: fengxin
 * @CreateTime: 2023-04-19  18:56
 * @Description: 用于用户登录、身份验证
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVo implements Serializable {

    // 主键
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    // 用户名
    private String username;

    // 密码
    private String password;

    // 昵称
    private String nickname;

    // 性别
    private String gender;

    // 头像
    private String avatar;

    // 电话号码
    private String phone;

    //微信唯一标识
    private String openid;

    // 联合id
    private String wxUnionId;

    // token令牌
    private String token;

    // 验证码
    private String code;

    public void from(WxUserInfo wxUserInfo){
        this.nickname = wxUserInfo.getNickName();
        this.avatar = wxUserInfo.getAvatarUrl();
        this.username = "";
        this.password = "";
        this.gender = wxUserInfo.getGender();
        this.openid = wxUserInfo.getOpenid();
        this.wxUnionId = wxUserInfo.getUnionId();
    }

}
