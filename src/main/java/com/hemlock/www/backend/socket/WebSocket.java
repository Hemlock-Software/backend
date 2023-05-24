package com.hemlock.www.backend.socket;

import com.hemlock.www.backend.room.Member;
import com.hemlock.www.backend.room.Message;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: backend
 * @description:
 * @create: 2023-05-20 14:51
 **/

@Component
@ServerEndpoint("/websocket/{roomid}/{username}") //暴露的ws应用的路径
public class WebSocket {
    private static AtomicInteger onlineClientNumber = new AtomicInteger(0);
    //在线客户端的集合
    private static Map<String , Session> onlineClientMap = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("roomid") String roomid, @PathParam("username") String username){
        //连接成功
        onlineClientNumber.incrementAndGet();//在线数+1
        onlineClientMap.put(session.getId(),session);//添加当前连接的session

    }

    @OnClose
    public void onClose(Session session, @PathParam("roomid") String roomid, @PathParam("username") String username){
        //连接关闭
        onlineClientNumber.decrementAndGet();//在线数-1
        onlineClientMap.remove(session.getId());//移除当前连接的session
    }

    @OnError
    public void onError(Throwable error, Session session, @PathParam("roomid") String roomid, @PathParam("username") String username){
        //连接错误
        error.printStackTrace();
    }

    @OnMessage
    public void onMessage(Session session, String content, @PathParam("roomid") String roomid, @PathParam("username") String username){
        System.out.println("roomid:"+roomid+" username:"+username);

        //收到消息
        Message message = new Message();

        message.setRoomID(roomid);
        //messageID怎么获得
        message.setMessageID("");
        message.setContent(content);
        message.setTime(new Time(new Date().getTime()));
        Member member = new Member();
        //member的email nickname怎么获得
        message.setSender(member);

        //message插入数据库

        //转发给其他用户
        Set<String> sessionIdSet = onlineClientMap.keySet(); //获得Map的Key的集合
        for (String sessionId : sessionIdSet) { //迭代Key集合
            Session session1 = onlineClientMap.get(sessionId); //根据Key得到value
            session1.getAsyncRemote().sendText(content); //发送消息给客户端1
        }

    }



}
