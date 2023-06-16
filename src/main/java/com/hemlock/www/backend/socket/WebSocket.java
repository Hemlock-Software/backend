package com.hemlock.www.backend.socket;

import com.alibaba.fastjson.JSON;
import com.hemlock.www.backend.BackendApplication;
import com.hemlock.www.backend.ChatBot.ChatGLM;
import com.hemlock.www.backend.ChatBot.ChatGLMHistory;
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
import org.springframework.web.server.WebSession;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;

import java.io.IOException;
import java.net.URI;
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

//@Component

public class WebSocket extends Observer implements WebSocketHandler {
    private static AtomicInteger onlineClientNumber = new AtomicInteger(0);
    //在线客户端的集合
    private static Map<String , Session> onlineClientMap = new ConcurrentHashMap<>();
    private static Map<String,Map<String , WebSocketSession>> socketMap = new HashMap<>();
    private static boolean isObserverAdded = false; //添加静态变量
    private static WebSocket observerInstance = new WebSocket(); // 创建静态观察者实例

    // 记录每个房间的ChatGLM记录
    // 放redis里面，以后负载均衡也能用
    public static String ChatGLMHistoryPrifix = "ChatGLMHistory";

    // 在静态代码块中添加观察者
    static {
        if (!isObserverAdded) {
            Subscriber.getInstance().attach(observerInstance);
            isObserverAdded = true;
        }
    }

    private Map<String, String> parseParameters(URI uri) {
        // 解析 URI 中的参数并返回一个 Map
        // 这里仅作示例，实际解析方法根据你的具体需求进行实现
        // 你可以使用 Java 提供的 URI 解析工具类或自定义解析方法
        // 返回的 Map 包含解析得到的参数键值对
        // 如果 URI 不包含参数，可以返回空的 Map 或者根据需求进行相应处理
        // 下面是示例代码，仅作为参考，请根据实际情况修改
        Map<String, String> parameters = new HashMap<>();
        String query = uri.getQuery();
        System.out.println("query"+query);
        if (query != null && !query.isEmpty()) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    String key = keyValue[0];
                    String value = keyValue[1];
                    System.out.println(key+" "+value);
                    parameters.put(key, value);
                }
            }
        }
        return parameters;
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        //连接成功
        URI uri =session.getUri();

        Map<String, String> parameters = parseParameters(uri);
        String roomid = parameters.get("roomid");
        String username = parameters.get("username");
        System.out.println(roomid+" "+username);

        onlineClientNumber.incrementAndGet();//在线数+1
        if (!socketMap.containsKey(roomid)){
            socketMap.put(roomid,new ConcurrentHashMap<>());
            BackendApplication.HotData.Subscribe(roomid);
        }

        socketMap.get(roomid).put(session.getId(),session);
        //socketMap.get(roomid).put(session.getId(), new ConcurrentWebSocketSessionDecorator(session,10000,1024*128, ConcurrentWebSocketSessionDecorator.OverflowStrategy.DROP));
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
        BackendApplication.HotData.SendMSG(roomid,JSON.toJSONString(newMsg));
        

        System.out.println("connect: "+onlineClientNumber);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus){
        //连接关闭
        URI uri =session.getUri();
        Map<String, String> parameters = parseParameters(uri);
        String roomid = parameters.get("roomid");
        String username = parameters.get("username");

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

        //转发给其他用户
        BackendApplication.HotData.SendMSG(roomid,JSON.toJSONString(newMsg));

        System.out.println("close: "+onlineClientNumber);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception){
        //连接错误
        System.out.println("连接出错");
    }


    private void messageHandler(String content, String roomid, Member owner){

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
        String jsonNewMsg = JSON.toJSONString(newMsg);
        RoomHot.addMessage(roomid,jsonNewMsg);

        //放入消息队列
        BackendApplication.HotData.SendMSG(roomid,jsonNewMsg);
    }
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message){
        URI uri =session.getUri();
        Map<String, String> parameters = parseParameters(uri);
        String roomid = parameters.get("roomid");
        String username = parameters.get("username");
        Object payload = message.getPayload();
        String content = (String) payload;

        System.out.println("roomid:"+roomid+" username:"+username);
        System.out.println("message1: "+onlineClientNumber);

        String storedUserJson = BackendApplication.ColdData.Get(username);
        UserValue storedUserValue = JSON.parseObject(storedUserJson, UserValue.class);

        Member owner = new Member(username, storedUserValue.getNickname());

        messageHandler(content,roomid,owner);

        // 聊天机器人部分
        // 没有上下文版
//        if(content.length() >= ChatGLM.CHAT_GLM_PREFIX.length() && content.substring(0,ChatGLM.CHAT_GLM_PREFIX.length()).equals(ChatGLM.CHAT_GLM_PREFIX)){
//            CompletableFuture<Void> response = CompletableFuture.runAsync(() -> {
//                String chatBotResponse = ChatGLM.getMessage( content.substring(ChatGLM.CHAT_GLM_PREFIX.length() ) );
//                Member chatBot = new Member("nomail@localhost", "ChatBot-ChatGLM6B");
//
//                messageHandler(chatBotResponse,roomid,chatBot);
//            });
//        }
        if(content.length() >= ChatGLM.CHAT_GLM_PREFIX.length() && content.substring(0,ChatGLM.CHAT_GLM_PREFIX.length()).equals(ChatGLM.CHAT_GLM_PREFIX)){
            CompletableFuture<Void> response = CompletableFuture.runAsync(() -> {
                ChatGLMHistory history = new ChatGLMHistory();

                if(BackendApplication.HotData.Exists(ChatGLMHistoryPrifix+roomid) > 0){
                    history = JSON.parseObject(BackendApplication.HotData.Get(ChatGLMHistoryPrifix+roomid),ChatGLMHistory.class);
                }else{
                    ArrayList<String> a = new ArrayList<>();
                    a.add("接下来的所有输出只含一句话，不要超过30字。");
                    a.add("好的。");
                    history.history.add(a);
                }

                // get response
                String chatBotResponse = ChatGLM.getMessageWithContext( content.substring(ChatGLM.CHAT_GLM_PREFIX.length()),history.history );
                // edit room history
                ArrayList<String> b = new ArrayList<>();
                b.add(content.substring(ChatGLM.CHAT_GLM_PREFIX.length()));
                b.add(chatBotResponse);
                if(history.history.size()<=4){
                    history.history.add(b);
                }else{
                    history.history.remove(1);
                    history.history.add(b);
                }
                BackendApplication.HotData.Set(ChatGLMHistoryPrifix+roomid,JSON.toJSONString(history));
                // send message
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
    public void update(String roomId,String message) throws IOException {
        if (!socketMap.containsKey(roomId)){
            return;
        }
        System.out.println("message queue valid");
        Set<String> sessionIdSet = socketMap.get(roomId).keySet(); //获得Map的Key的集合
        for (String sessionId : sessionIdSet) { //迭代Key集合

            WebSocketSession session1 = socketMap.get(roomId).get(sessionId); //根据Key得到value
            session1.sendMessage(new TextMessage(message)); //发送消息给客户端


        }
    }

}
