package com.shuai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shuai.pojo.po.ChatRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ChatRecordMapper extends BaseMapper<ChatRecord> {

    // 通过当前用户id搜索获取该用户的所有聊天记录
    @Select("SELECT * FROM chat_record WHERE sender_id = 1 OR receiver_id = 1 ORDER BY send_time ASC")
    List<ChatRecord> getChatRecordById(Long id);
}
