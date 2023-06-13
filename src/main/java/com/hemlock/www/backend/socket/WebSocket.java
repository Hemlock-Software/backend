package com.hemlock.www.backend.socket;

import com.alibaba.fastjson.JSON;
import com.hemlock.www.backend.BackendApplication;
import com.hemlock.www.backend.ChatBot.ChatGLM;
import com.hemlock.www.backend.MessageQueue.Observer;
import com.hemlock.www.backend.MessageQueue.Subscriber;
import com.hemlock.www.backend.Redis.ClusterRedisIO;
import com.hemlock.www.backend.room.*;
import com.hemlock.www.backend.user.UserValue;
import com.sun.mail.imap.MessageVanishedEvent;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: backend
 * @description:
 * @create: 2023-05-20 14:51
 **/

@Component
@ServerEndpoint("/websocket/{roomid}/{username}") //暴露的ws应用的路径
public class WebSocket extends Observer {
    private static AtomicInteger onlineClientNumber = new AtomicInteger(0);
    //在线客户端的集合
    private static Map<String , Session> onlineClientMap = new ConcurrentHashMap<>();
    private static Map<String,Map<String , Session>> socketMap = new HashMap<>();
    private static boolean isObserverAdded = false; //添加静态变量
    private static WebSocket observerInstance = new WebSocket(); // 创建静态观察者实例

    // 在静态代码块中添加观察者
    static {
        if (!isObserverAdded) {
            Subscriber.getInstance().attach(observerInstance);
            isObserverAdded = true;
        }
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("roomid") String roomid, @PathParam("username") String username) throws IOException {
        //连接成功
        onlineClientNumber.incrementAndGet();//在线数+1
        if (!socketMap.containsKey(roomid)){
            socketMap.put(roomid,new ConcurrentHashMap<>());
            BackendApplication.HotData.Subscribe(roomid);
        }
        socketMap.get(roomid).put(session.getId(),session);
//        onlineClientMap.put(session.getId(),session);//添加当前连接的session


        String content = "enter";
        String storedUserJson = BackendApplication.ColdData.Get(username);
        UserValue storedUserValue = JSON.parseObject(storedUserJson, UserValue.class);

        Member owner = new Member("$notice$", storedUserValue.getNickname());


        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd :HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        MessageValue newMsg = new MessageValue();
        newMsg.setContent(content);
        newMsg.setTime(dateFormat.format(date));
        newMsg.setSender(owner);


        //转发给其他用户
//        Set<String> sessionIdSet = onlineClientMap.keySet(); //获得Map的Key的集合
        Set<String> sessionIdSet = socketMap.get(roomid).keySet(); //获得Map的Key的集合
        for (String sessionId : sessionIdSet) { //迭代Key集合
            Session session1 = socketMap.get(roomid).get(sessionId); //根据Key得到value
            session1.getAsyncRemote().sendText(JSON.toJSONString(newMsg)); //发送消息给客户端

        }
        System.out.println("connect: "+onlineClientNumber);
    }

    @OnClose
    public void onClose(Session session, @PathParam("roomid") String roomid, @PathParam("username") String username){
        //连接关闭
        onlineClientNumber.decrementAndGet();//在线数-1
        socketMap.get(roomid).remove(session.getId());
//        onlineClientMap.remove(session.getId());//移除当前连接的session

        String content = "quit";
        String storedUserJson = BackendApplication.ColdData.Get(username);
        UserValue storedUserValue = JSON.parseObject(storedUserJson, UserValue.class);

        Member owner = new Member("$notice$", storedUserValue.getNickname());

//        MessageKey key = new MessageKey();
//        key.setMessageID(Integer.parseInt(RoomHot.incrementLastMessageID(roomid)));
//        key.setRoomID(roomid);

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd :HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        MessageValue newMsg = new MessageValue();
        newMsg.setContent(content);
        newMsg.setTime(dateFormat.format(date));
        newMsg.setSender(owner);

//        BackendApplication.HotData.Set(JSON.toJSONString(key), JSON.toJSONString(newMsg));


        //转发给其他用户
//        Set<String> sessionIdSet = onlineClientMap.keySet(); //获得Map的Key的集合
        Set<String> sessionIdSet = socketMap.get(roomid).keySet(); //获得Map的Key的集合
        for (String sessionId : sessionIdSet) { //迭代Key集合
            Session session1 = socketMap.get(roomid).get(sessionId); //根据Key得到value
            session1.getAsyncRemote().sendText(JSON.toJSONString(newMsg)); //发送消息给客户端

        }

        System.out.println("close: "+onlineClientNumber);
    }

    @OnError
    public void onError(Throwable error, Session session, @PathParam("roomid") String roomid, @PathParam("username") String username){
        //连接错误
        error.printStackTrace();
    }


    private void messageHandler(String content, String roomid, Member owner){
        MessageKey key = new MessageKey();
        key.setMessageID(Integer.parseInt(RoomHot.incrementLastMessageID(roomid)));
        key.setRoomID(roomid);

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd :HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        MessageValue newMsg = new MessageValue();
        newMsg.setContent(content);
        newMsg.setTime(dateFormat.format(date));
        newMsg.setSender(owner);


        BackendApplication.HotData.Set(JSON.toJSONString(key), JSON.toJSONString(newMsg));

        //放入消息队列
        BackendApplication.HotData.SendMSG(roomid,JSON.toJSONString(newMsg));
    }
    @OnMessage
    public void onMessage(Session session, String content, @PathParam("roomid") String roomid, @PathParam("username") String username){
        System.out.println("roomid:"+roomid+" username:"+username);
        System.out.println("message1: "+onlineClientNumber);

        String storedUserJson = BackendApplication.ColdData.Get(username);
        UserValue storedUserValue = JSON.parseObject(storedUserJson, UserValue.class);

        Member owner = new Member(username, storedUserValue.getNickname());

        messageHandler(content,roomid,owner);

        // 聊天机器人部分
        if(content.length() >= ChatGLM.CHAT_GLM_PREFIX.length() && content.substring(0,ChatGLM.CHAT_GLM_PREFIX.length()).equals(ChatGLM.CHAT_GLM_PREFIX)){
            CompletableFuture<Void> response = CompletableFuture.runAsync(() -> {
                String chatBotResponse = ChatGLM.getMessage( content.substring(ChatGLM.CHAT_GLM_PREFIX.length() ) );
                Member chatBot = new Member("nomail@localhost", "ChatBot-ChatGLM6B");

                messageHandler(chatBotResponse,roomid,chatBot);
            });
        }

        //转发给其他用户
        // update by hrj:这里注释掉，不然消息队列会重复推送
//        Set<String> sessionIdSet = onlineClientMap.keySet(); //获得Map的Key的集合
//        Set<String> sessionIdSet = socketMap.get(roomid).keySet(); //获得Map的Key的集合
//        for (String sessionId : sessionIdSet) { //迭代Key集合
//            Session session1 = socketMap.get(roomid).get(sessionId); //根据Key得到value
//            session1.getAsyncRemote().sendText(JSON.toJSONString(newMsg)); //发送消息给客户端
//
//        }
        System.out.println("message2: "+onlineClientNumber);
    }

    @Override
    public void update(String roomId,String message) {
        if (!socketMap.containsKey(roomId)){
            return;
        }
        System.out.println("message queue valid");
        Set<String> sessionIdSet = socketMap.get(roomId).keySet(); //获得Map的Key的集合
        for (String sessionId : sessionIdSet) { //迭代Key集合
            Session session1 = socketMap.get(roomId).get(sessionId); //根据Key得到value
            session1.getAsyncRemote().sendText(message); //发送消息给客户端

        }
    }

}
