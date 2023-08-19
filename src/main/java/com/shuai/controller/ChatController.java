package com.shuai.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shuai.handler.UserThreadLocal;
import com.shuai.pojo.po.ChatRecord;
import com.shuai.pojo.po.Inform;
import com.shuai.pojo.po.User;
import com.shuai.pojo.vo.ChatRecordVo;
import com.shuai.pojo.vo.InformVo;
import com.shuai.pojo.vo.UserVo;
import com.shuai.service.ChatRecordService;
import com.shuai.service.InformService;
import com.shuai.service.UserService;
import com.shuai.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.lang.model.element.UnknownAnnotationValueException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Author: fengxin
 * @CreateTime: 2023-04-24  20:58
 * @Description: TODO
 */
@Slf4j
@RestController
@Transactional
@RequestMapping("/record")
public class ChatController {

    @Autowired
    private ChatRecordService chatRecordService;

    @Autowired
    private InformService informService;

    @Autowired
    private UserService userService;


//    /*
//     * @description: 获取和当前用户相关的所有聊天记录
//     * @author: fengxin
//     * @date: 2023/4/25 19:05
//     * @param: []
//     * @return: 和当前用户相关的所有聊天记录
//     **/
//    @GetMapping("/getChatRecord")
//    public Result getChatRecord(@RequestParam(defaultValue = "0", name = "friendId")Long friendId){
//        log.info("传过来的参数：friendId = {}",friendId);
//        // 1.拿到当前用户
//        UserVo userVO = UserThreadLocal.get();
//        // 2.通过当前用户id拿到关于这个用户的所有聊天记录
//        HashMap<Long, List<Object>> chatRecord = chatRecordService.getChatRecord(userVO.getId(),friendId);
//        log.info("返回给前端的数据：{}",chatRecord);
//        return Result.success("获取和当前用户相关的所有聊天记录",chatRecord);
//    }

    //    @GetMapping("/getChatList")
//    public Result getChatList(){
//        // 1.拿到当前用户
//        UserVo userVO = UserThreadLocal.get();
//
//        // 2.通过当前用户id拿到关于这个用户的所有聊天记录
//        HashMap<Long, List<Object>> chatRecord = chatRecordService.getChatRecord(userVO.getId(),friendId);
//        log.info("返回给前端的数据：{}",chatRecord);
//        return Result.success("获取和当前用户相关的所有聊天记录",chatRecord);
//    }
//




    /**
     * @description: 获取和商家聊天的消息列表
     * @author: fengxin
     * @date: 2023/8/15 15:24
     * @param: []
     * @return: 和商家聊天的消息列表
     **/
    @GetMapping("/getGoodChatList")
    public Result getGoodChatList() {
        // 1. 获取自己所有的聊天记录
        List<ChatRecord> chatList = chatRecordService.getChatList();
        // 2. 从中分类得到和商家的聊天列表
        List<ChatRecordVo> goodChatList = chatRecordService.getNeedChatList(chatList,"商家");
        return Result.success("和商家聊天的消息列表",goodChatList);
    }

    /**
     * @description: 获取和专家聊天的消息列表
     * @author: fengxin
     * @date: 2023/8/15 16:59
     * @param: []
     * @return: 和专家聊天的消息列表
     **/
    @GetMapping("/getExpertChatList")
    public Result getExpertChatList() {
        // 1. 获取自己所有的聊天记录
        List<ChatRecord> chatList = chatRecordService.getChatList();
        // 2. 从中分类得到和专家的聊天列表
        List<ChatRecordVo> expertChatList = chatRecordService.getNeedChatList(chatList,"专家");
        return Result.success("和专家聊天的消息列表",expertChatList);
    }


    /**
     * @description: 获取系统消息列表
     * @author: fengxin
     * @date: 2023/8/15 17:21
     * @param: []
     * @return: 系统消息列表
     **/
    @GetMapping("/getSystemChatList")
    public Result getSystemChatList() {
        List<InformVo> informVos = informService.getSystemChatList();
        return Result.success("系统消息列表",informVos);
    }


    /**
     * @description: 获取消息页面的消息列表
     * @author: fengxin
     * @date: 2023/8/15 19:47
     * @param: []
     * @return: 消息页面的消息列表
     **/
    @GetMapping("/getMessageList")
    public Result getMessageList() {
        // 1. 获取自己所有的聊天记录
        List<ChatRecord> chatList = chatRecordService.getChatList();
        // 2. 拿到最新的一条和专家的消息、一条和商家的消息，一条系统通知消息，所有的用户消息
        ChatRecordVo expertMessage = chatRecordService.getNeedChatList(chatList, "专家").get(0);
        ChatRecordVo goodMessage = chatRecordService.getNeedChatList(chatList, "商家").get(0);
        InformVo systemMessage = informService.getSystemChatList().get(0);
        List<ChatRecordVo> userChatList = chatRecordService.getNeedChatList(chatList, "用户");
        // 3. 创建一个Map集合作为返回值返回给前端
        HashMap<String, Object> messages = new HashMap<>();
        messages.put("expertMessage",expertMessage);
        messages.put("goodMessage",goodMessage);
        messages.put("systemMessage",systemMessage);
        messages.put("userMessage",userChatList);
        return Result.success("消息页面的消息列表",messages);
    }

    /**
     * @description: 获取和具体某人的所有聊天记录
     * @author: fengxin
     * @date: 2023/8/15 20:59
     * @param: [chatObjectId]
     * @return: 聊天记录
     **/
    @GetMapping("/getChatRecord")
    public Result getChatRecord(@RequestParam("chatObjectId")Long chatObjectId) {
        return chatRecordService.getChatRecord(chatObjectId);
    }





}