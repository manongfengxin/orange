package com.shuai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shuai.handler.UserThreadLocal;
import com.shuai.mapper.ChatRecordMapper;
import com.shuai.mapper.UserMapper;
import com.shuai.pojo.po.ChatRecord;
import com.shuai.pojo.po.User;
import com.shuai.pojo.vo.ChatRecordVo;
import com.shuai.service.ChatRecordService;
import com.shuai.util.Result;
import com.shuai.util.TimeUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@Transactional
public class ChatRecordServiceImpl extends ServiceImpl<ChatRecordMapper, ChatRecord> implements ChatRecordService {

    @Autowired
    private ChatRecordMapper chatRecordMapper;

    @Autowired
    private UserMapper userMapper;

//    @Override
//    public HashMap<Long, List<Object>> getChatRecord(Long id, Long friendId) {
//        // 最终返回给前端的数据（进行打包）
//        HashMap<Long, List<Object>> result = new HashMap<>();
//        // 1.通过用户id获取该用户所有的聊天记录
//        List<ChatRecord> recordDb = chatRecordMapper.getChatRecordById(id);
//        // 2.循环遍历每一条聊天记录
//        for (ChatRecord chatRecord : recordDb) {
//            // 2.1 得到和当前用户聊天用户的id，记录为toId（toId和id必然不相等）
//            Long senderId = chatRecord.getSenderId();
//            Long receiverId = chatRecord.getReceiverId();
//            Long toId = Objects.equals(senderId, id) ? receiverId : senderId;
//            // 2.2 以toId作为key，用户id和用户toId的聊天记录为value
//            // 2.2.1 如果该key
//            if (!result.containsKey(toId)) {
//                // 不存在：
//                // 创建一个list集合存储用户id和用户toId的聊天记录或用户信息
//                List<Object> storeChatRecord = new ArrayList<>();
//
//                // 获取 和当前用户聊天用户 的昵称、头像
//                User user = userMapper.selectById(toId);
//                HashMap<String, String> storeInfo = new HashMap<>();
//                storeInfo.put("friendId",user.getId().toString());
//                storeInfo.put("nickName",user.getNickname());
//                storeInfo.put("avatar",user.getAvatar());
//                // 再存一条当前这条聊天记录的发送时间
//                storeInfo.put("deadline",chatRecord.getSendTime());
//                storeChatRecord.add(storeInfo);
//                // 将当前这条记录存入
//                storeChatRecord.add(chatRecord);
//
//                result.put(toId,storeChatRecord);
//            }else {
//                // 存在：
//                // 将当前这条记录存入
//                result.get(toId).add(chatRecord);
//
//                // 随着循环不断修改聊天记录最后一条的时间，返回给前端
//                HashMap storeInfo= (HashMap)result.get(toId).get(0);
//                storeInfo.put("deadline",chatRecord.getSendTime());
//            }
//
//            /* 如果是第一次建立连接：前端将传 friendId，要保证没有和 friendId 有过聊天记录*/
//            if (friendId != 0 && !result.containsKey(friendId)) {
//                List<Object> storeChatRecord = new ArrayList<>();
//                HashMap<String, String> storeInfo = new HashMap<>();
//                User friendUser = userMapper.selectById(friendId);
//                storeInfo.put("friendId",friendUser.getId().toString());
//                storeInfo.put("nickName",friendUser.getNickname());
//                storeInfo.put("avatar",friendUser.getAvatar());
//                storeInfo.put("deadline", TimeUtil.getNowTime());
//                storeChatRecord.add(storeInfo);
//                result.put(friendId, storeChatRecord);
//            }
//
////            /* 给 result 以 deadtime 排序：时间月新越在上面*/
////            Collections.sort(result);
//        }
//        return result;
//    }

    @Override
    public List<ChatRecord> getChatList() {
        // 0. 拿到当前用户id
        Long userId = UserThreadLocal.get().getId();
        // 2. 查询数据库和所有人的聊天记录
        return chatRecordMapper.selectList(
                new LambdaQueryWrapper<ChatRecord>()
                .orderByDesc(ChatRecord::getSendTime)
                .eq(ChatRecord::getSenderId,userId)
                .or().eq(ChatRecord::getReceiverId,userId));
    }

    @Override
    public List<ChatRecordVo> getNeedChatList(List<ChatRecord> chatList, String needRole) {
        // 0. 拿到当前用户id
        Long userId = UserThreadLocal.get().getId();
        // 1. 创建一个map集合：key放 所需要角色 的id，value放最新一条聊天记录
        HashMap<Long, ChatRecordVo> result = new HashMap<>();
        // 2. 遍历聊天记录
        for (ChatRecord chatRecord :chatList) {
            // 3. 得到和当前用户聊天用户的id，记录为toId（toId和userId必然不相等）
            Long senderId = chatRecord.getSenderId();
            Long receiverId = chatRecord.getReceiverId();
            Long toId = Objects.equals(senderId, userId) ? receiverId : senderId;
            // 4. 通过查询 toId 的身份
            User user = userMapper.selectById(toId);
            String role = user.getRole();
            // 5. 筛选出和 所需要角色 的聊天记录，然后判断这个key是否存在 result map集合中
            if (Objects.equals(role,needRole) && !result.containsKey(toId)) {
                // 6. 如果该 key 在 result 集合中不存在：则加入 result 集合
                // 6.1 构建返回前端的聊天记录 chatRecordVo
                ChatRecordVo chatRecordVo = new ChatRecordVo();
                BeanUtils.copyProperties(chatRecord,chatRecordVo);
                chatRecordVo.setToId(toId);
                chatRecordVo.setToNickname(user.getNickname());
                chatRecordVo.setToAvatar(user.getAvatar());
                // 6.2 加入 result 集合
                result.put(toId,chatRecordVo);
            }
        }
        // 7. 为了便于前端处理渲染数据：把Map集合转换为List集合
        return new ArrayList<>(result.values());
    }

    @Override
    public Result getChatRecord(Long chatObjectId) {
        HashMap<String, Object> chatRecord = new HashMap<>();
        // 0. 拿到当前用户id
        Long userId = UserThreadLocal.get().getId();
        // 1. 查询数据库：userId 和 chatObjectId 的聊天记录
        List<ChatRecord> chatRecordList = chatRecordMapper.selectList(
                new LambdaQueryWrapper<ChatRecord>()
                        .eq(ChatRecord::getSenderId, userId)
                        .eq(ChatRecord::getReceiverId, chatObjectId)
                        .or()
                        .eq(ChatRecord::getReceiverId, userId)
                        .eq(ChatRecord::getSenderId, chatObjectId));
        // 2. 通过 chatObjectId 查询该用户的昵称、头像
        User chatObject = userMapper.getBriefInfo(chatObjectId);
        chatRecord.put("chatRecord",chatRecordList);
        chatRecord.put("nickname",chatObject.getNickname());
        chatRecord.put("avatar",chatObject.getAvatar());
        return Result.success("获取和某人的聊天记录",chatRecord);
    }
}
