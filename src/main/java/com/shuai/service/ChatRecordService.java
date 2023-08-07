package com.shuai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shuai.pojo.po.ChatRecord;
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
    HashMap<Long, List<Object>> getChatRecord(Long id, Long friendId);
}
