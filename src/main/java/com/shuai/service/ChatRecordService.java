package com.shuai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shuai.pojo.po.ChatRecord;
import com.shuai.pojo.vo.ChatRecordVo;
import com.shuai.util.Result;

import java.util.HashMap;
import java.util.List;

public interface ChatRecordService extends IService<ChatRecord> {

    /*
     * @description: 获取和当前用户相关的所有聊天记录
     * @author: fengxin
     * @date: 2023/4/25 16:15
     * @param: [id]
     * @return: 聊天记录
     **/
//    HashMap<Long, List<Object>> getChatRecord(Long id, Long friendId);

    // 获取自己所有的聊天记录
    List<ChatRecord> getChatList();

    // 获取和 所需要角色 聊天的消息列表（只有和每个 消息最新的一条）
    List<ChatRecordVo> getNeedChatList(List<ChatRecord> chatList,String needRole);

    // 获取和具体某人的所有聊天记录
    Result getChatRecord(Long chatObjectId);
}
