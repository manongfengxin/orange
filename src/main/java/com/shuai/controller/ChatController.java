package com.shuai.controller;

import com.shuai.handler.UserThreadLocal;
import com.shuai.pojo.vo.UserVO;
import com.shuai.service.ChatRecordService;
import com.shuai.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * @Author: fengxin
 * @CreateTime: 2023-04-24  20:58
 * @Description: TODO
 */
@Slf4j
@RestController
@RequestMapping("/record")
public class ChatController {

    @Autowired
    private ChatRecordService chatRecordService;


    /*
     * @description: 获取和当前用户相关的所有聊天记录
     * @author: fengxin
     * @date: 2023/4/25 19:05
     * @param: []
     * @return: 和当前用户相关的所有聊天记录
     **/
    @GetMapping("/getChatRecord")
    public Result getChatRecord(@RequestParam(defaultValue = "0", name = "friendId")Long friendId){
        log.info("传过来的参数：friendId = {}",friendId);
        // 1.拿到当前用户
        UserVO userVO = UserThreadLocal.get();
        // 2.通过当前用户id拿到关于这个用户的所有聊天记录
        HashMap<Long, List<Object>> chatRecord = chatRecordService.getChatRecord(userVO.getId(),friendId);
        log.info("返回给前端的数据：{}",chatRecord);
        return Result.success("获取和当前用户相关的所有聊天记录",chatRecord);
    }

}