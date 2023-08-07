package com.shuai.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.shuai.pojo.po.ChatRecord;
import com.shuai.pojo.vo.ChatRecordVO;
import com.shuai.service.ChatRecordService;
import com.shuai.util.MessageUtils;
import com.shuai.util.MyBeanUtil;
import com.shuai.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: fengxin
 * @CreateTime: 2023-04-20  19:28
 * @Description: websocket实现聊天功能
 */
@Slf4j
@Controller
@ServerEndpoint("/chat/{id}")
public class ChatEndpoint {

    // 用来存储每一个客户端对象对应的 ChatEndpoint 对象
    private static Map<Long, Session> onlineUsers = new ConcurrentHashMap<>();

    // 声明Session对象，通过该对象可以发送消息给指定的用户
    private Session session;

    // 存储当前用户的登录信息
//    private UserVO uservo;
    private Long id;

    /*
     * @description: 抽取方法：将当前在线用户的id推送给所有的在线客户端
     * @author: fengxin
     * @date: 2023/4/20 22:43
     * @param: []
     * @return: void
     **/
    private void sendUsers() {
        log.info("进行广播，通知所有用户，目前在线用户情况");

        // 1. 获取所有在线用户的id
        Set<Long> ids = onlineUsers.keySet();

        log.info("当前在线用户：{}",ids);

        // 2.封装成系统发送给用户的消息格式
        String resultMessage = MessageUtils.getMessage(true, null, ids);
        // 3.通过遍历所有的在线用户id
        for (Long id : ids) {
            /**/// 获取每个在线用户的id，通过id拿到对应的ChatEndpoint对象
            Session session = onlineUsers.get(id);
            try {
                // 通过ChatEndpoint对象给对应在线用户发送系统消息
                session.getBasicRemote().sendText(resultMessage);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * @description: 聊天连接建立时被调用：将当前在线用户的id推送给所有的在线客户端
     * @author: fengxin
     * @date: 2023/4/20 19:33
     * @param: [session, config]
     * @return: void
     **/
    @OnOpen
    public void onOpen(Session session, @PathParam("id") Long id) {

        log.info("进入onOpen方法！");
        // 1.将局部session对象赋值给成员session
        this.session = session;
        // 2.将当前用户登录信息赋值给成员uservo
//        this.uservo = UserThreadLocal.get();
        this.id = id;
        // 3.将当前用户对象存储到容器中（通过id标识在线用户）
        onlineUsers.put(id/*uservo.getId()*/,session);
        // 4.将当前在线用户的id推送给所有的在线客户端
        sendUsers();
        log.info("新用户成功建立连接，用户id：{}，当前在线人数：{}",id/*uservo.getUsername()*/,onlineUsers.size());
    }

    /*
     * @description: 接收到客户端发送的数据被调用：实现用户与用户的消息传递
     * @author: fengxin
     * @date: 2023/4/20 19:33
     * @param: [message, session]
     * @return: void
     **/
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("进入onMessage方法，接收到用户id：{}发来信息：{}",id/*uservo.getUsername()*/,message);

        try {
            // 1.将局部变量message转换成JSONObject对象
            JSONObject obj = JSONUtil.parseObj(message);
            // 2.获取接收者用户的id ,获取发送者用户的id ,消息类型 messageType
            Long receiverId = obj.getLong("toId");
            Long senderId = obj.getLong("fromId");
            String messageType = obj.getStr("messageType");
            // 3.获取消息数据
            String content = obj.getStr("content");

            ChatRecordVO chatRecordVO = new ChatRecordVO(senderId,receiverId,content,messageType);
            // 判断用户toId是否在线
            if (onlineUsers.containsKey(1L)) {
                // 用户toId在线：
                // 4.封装成系统发送给用户的消息格式
                String resultMessage = MessageUtils.getMessage(false, senderId, content);
                // 5.服务器向指定（id）客户端发送数据
                /**/// 5.1 获取接收消息用户id对应的ChatEndpoint对象
                Session receiverSession = onlineUsers.get(1L);
                // 5.2 通过ChatEndpoint对象给对应在线用户发送系统消息
                receiverSession.getBasicRemote().sendText(resultMessage);
            }
            // 6.向发送者反馈发送成功
            String returnMessage = MessageUtils.getMessage(true, null,"发送成功！");
            Session senderSession = onlineUsers.get(senderId);
            senderSession.getBasicRemote().sendText(returnMessage);

            // 将当前用户发送的消息记录到数据库
            ChatRecord chatRecord = new ChatRecord();
            // 把chatRecordvo里的值都赋值给chatRecord
            BeanUtils.copyProperties(chatRecordVO,chatRecord);
            // 获取当前时间
            String nowTime = TimeUtil.getNowTime();
            String cid = "" + id/*uservo.getId()*/ + nowTime + receiverId;
            cid = cid.replace(" ","").replace("-","").replace(":","");
            chatRecord.setId(cid);
            chatRecord.setSenderId(senderId);
            chatRecord.setReceiverId(receiverId);
            chatRecord.setSendTime(nowTime);

            // 将该条聊天记录新增到数据库
            // 1.通过工具类手动获取 chatRecordService 对象
            ChatRecordService chatRecordService = MyBeanUtil.getBean(ChatRecordService.class);
            // 2.调用 chatRecordService 的sava方法，将该条数据保存到数据库
            chatRecordService.save(chatRecord);
            log.info("将这条聊天记录：{}保存到了数据库", chatRecord);
        }catch (Exception e) {
            log.info("onMessage方法出现错误了，快去调试~");
            e.printStackTrace();
        }
    }

    /*
     * @description: 连接关闭时被调用：将新的所有在线用户通知给所有在线用户
     * @author: fengxin
     * @date: 2023/4/20 19:34
     * @param: [session]
     * @return: void
     **/
    @OnClose
    public void onClose(Session session) {
        log.info("进入onClose方法，在线用户 -1");

        // 1.当前用户下线，则从在线用户中删除该用户
        onlineUsers.remove(id/*uservo.getId()*/);
        // 2.将新的所有在线用户通知给所有在线用户
        sendUsers();
    }



    /*
     * @description: 当 websocket 发生错误自动调用此方法
     * @author: fengxin
     * @date: 2023/5/12 18:30
     * @param: [session, error]
     * @return: void
     **/
    @OnError
    public void onError(Session session, Throwable error) {
        //什么都不想打印都去掉就好了
        log.info(" websocket 出错啦 -_-!");
        //打印错误信息，如果你不想打印错误信息，去掉就好了
        //这里打印的也是  java.io.EOFException: null
        error.printStackTrace();
    }

}
