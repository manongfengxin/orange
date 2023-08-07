package com.shuai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shuai.mapper.ChatRecordMapper;
import com.shuai.mapper.UserMapper;
import com.shuai.pojo.po.ChatRecord;
import com.shuai.pojo.po.User;
import com.shuai.service.ChatRecordService;
import com.shuai.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * @Author: fengxin
 * @CreateTime: 2023-04-24  21:14
 * @Description: TODO
 */
@Service
public class ChatRecordServiceImpl extends ServiceImpl<ChatRecordMapper, ChatRecord> implements ChatRecordService {

    @Autowired
    private ChatRecordMapper chatRecordMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public HashMap<Long, List<Object>> getChatRecord(Long id, Long friendId) {
        // 最终返回给前端的数据（进行打包）
        HashMap<Long, List<Object>> result = new HashMap<>();
        // 1.通过用户id获取该用户所有的聊天记录
        List<ChatRecord> recordDb = chatRecordMapper.getChatRecordById(id);
        // 2.循环遍历每一条聊天记录
        for (ChatRecord chatRecord : recordDb) {
            // 2.1 得到和当前用户聊天用户的id，记录为toId（toId和id必然不相等）
            Long senderId = chatRecord.getSenderId();
            Long receiverId = chatRecord.getReceiverId();
            Long toId = Objects.equals(senderId, id) ? receiverId : senderId;
            // 2.2 以toId作为key，用户id和用户toId的聊天记录为value
            // 2.2.1 如果该key
            if (!result.containsKey(toId)) {
                // 不存在：
                // 创建一个list集合存储用户id和用户toId的聊天记录或用户信息
                List<Object> storeChatRecord = new ArrayList<>();

                // 获取 和当前用户聊天用户 的昵称、头像
                User user = userMapper.selectById(toId);
                HashMap<String, String> storeInfo = new HashMap<>();
                storeInfo.put("friendId",user.getId().toString());
                storeInfo.put("nickName",user.getNickname());
                storeInfo.put("avatar",user.getAvatar());
                // 再存一条当前这条聊天记录的发送时间
                storeInfo.put("deadline",chatRecord.getSendTime());
                storeChatRecord.add(storeInfo);
                // 将当前这条记录存入
                storeChatRecord.add(chatRecord);

                result.put(toId,storeChatRecord);
            }else {
                // 存在：
                // 将当前这条记录存入
                result.get(toId).add(chatRecord);

                // 随着循环不断修改聊天记录最后一条的时间，返回给前端
                HashMap storeInfo= (HashMap)result.get(toId).get(0);
                storeInfo.put("deadline",chatRecord.getSendTime());
            }

            /* 如果是第一次建立连接：前端将传 friendId，要保证没有和 friendId 有过聊天记录*/
            if (friendId != 0 && !result.containsKey(friendId)) {
                List<Object> storeChatRecord = new ArrayList<>();
                HashMap<String, String> storeInfo = new HashMap<>();
                User friendUser = userMapper.selectById(friendId);
                storeInfo.put("friendId",friendUser.getId().toString());
                storeInfo.put("nickName",friendUser.getNickname());
                storeInfo.put("avatar",friendUser.getAvatar());
                storeInfo.put("deadline", TimeUtil.getNowTime());
                storeChatRecord.add(storeInfo);
                result.put(friendId, storeChatRecord);
            }

//            /* 给 result 以 deadtime 排序：时间月新越在上面*/
//            Collections.sort(result);
        }
        return result;
    }
}
