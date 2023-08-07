package com.shuai.controller;

import com.shuai.handler.NoAuth;
import com.shuai.handler.UserThreadLocal;
import com.shuai.pojo.bo.WxAuth;
import com.shuai.pojo.vo.UserVO;
import com.shuai.service.UserService;
import com.shuai.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: fengxin
 * @CreateTime: 2023-04-19  18:51
 * @Description: 处理登录注册
 */
@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserService userService;

    /*
     * @description: 通过用户名+密码登录
     * @author: fengxin
     * @date: 2023/4/20 10:39
     * @param: [uservo]：{username,password}
     * @return: com.improve.shell.util.Result
     **/
    @NoAuth
    @PostMapping("/loginByUser")
    public Result login(@RequestBody UserVO uservo){
        log.info("传过来的uservo==>{}",uservo);
        return userService.loginByUser(uservo);
    }


    /* 注册用户名+并设置密码
     * @description:
     * @author: fengxin
     * @date: 2023/4/20 10:38
     * @param: [uservo]：{username,password}
     * @return: com.improve.shell.util.Result
     **/
    @NoAuth
    @PostMapping("/registerUser")
    public Result registerUser(@RequestBody UserVO uservo){
        log.info("传过来的uservo==>{}",uservo);
        return userService.register(uservo);
    }

    // --------------------下面进行微信登录接口----------------

    /**
     * 拿到 code生成一个 sessionId返回
     * @param: [code]
     * @return: sessionId
     **/
    @NoAuth
    @GetMapping("/getSessionId")
    public Result getSessionId(@RequestParam String code){
        log.info("接收到code==>{}",code);
        return userService.getSessionId(code);
    }

    /**
     * 通过微信解密参数 对用户信息进行解密  生成 token
     * @param: [wxAuth]
     * @return: 用户微信基本信息 + token
     **/
    @NoAuth
    @PostMapping("/authLogin")
    public Result authLogin(@RequestBody WxAuth wxAuth){
        log.info("获得三个参数WxAuth==>{}",wxAuth);
        Result result = userService.authLogin(wxAuth);
        log.info("{}",result);
        return result;
    }

    /**
     * 刷新 token，获取用户基本信息
     * @param refresh
     * @return 用户微信基本信息 + token
     */
    @NoAuth
    @GetMapping("/userinfo")
    public Result userinfo(boolean refresh){
        return userService.userinfo(refresh);
    }

    /**
     * @description:
     * @author: fengxin
     * @date: 2023/8/6 19:04
     * @param:
     * @return:
     **/
    @DeleteMapping("/logout")
    public Result logout() {
        return userService.logout();
    }


}
