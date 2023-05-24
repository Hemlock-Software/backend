package com.hemlock.www.backend.controller;

import com.alibaba.fastjson2.JSON;
import com.hemlock.www.backend.BackendApplication;
import com.hemlock.www.backend.request.CreateRoomArgs;
import com.hemlock.www.backend.request.EnterRoomArgs;
import com.hemlock.www.backend.request.GetRoomInfoArgs;
import com.hemlock.www.backend.request.TestSendMsgArgs;
import com.hemlock.www.backend.user.UserStoredRoomValue;
import com.hemlock.www.backend.user.UserValue;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hemlock.www.backend.room.*;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Objects;

@CrossOrigin(methods = {RequestMethod.POST})

@RestController
@RequestMapping("/room")
public class RoomController {

    private static final String STR_FORMAT = "00000000";

    @RequestMapping(value = "/enter-room", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> EnterRoom(HttpServletRequest request,@RequestBody EnterRoomArgs args) {
        // 验证
        // 1. get user data
        String user = (String) request.getAttribute("email");
        String storedUserJson = BackendApplication.ColdData.Get(user);
        UserValue storedUserValue = JSON.parseObject(storedUserJson, UserValue.class);

        Member caller = new Member(user, storedUserValue.getNickname());
        // 2. check roomID is valid or not
        if(BackendApplication.ColdData.Exists(args.getRoomID()) > 0){
            RoomValueCold roomData = JSON.parseObject(BackendApplication.ColdData.Get(args.getRoomID()),RoomValueCold.class);
            if(roomData.getPassword().equals(args.getPassword()) ){
                // add to room member list
                roomData.addMember(caller);
                // add to user room list
                storedUserValue.addRoom(args.getRoomID(),roomData.getName());

                BackendApplication.ColdData.Set(user,JSON.toJSONString(storedUserValue));
                BackendApplication.ColdData.Set(args.getRoomID(), JSON.toJSONString(roomData));
                return ResponseEntity.status(HttpStatus.OK).body(JSON.toJSONString(roomData));
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("wrong password!");
            }
        }else{
            // roomID not valid
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("roomID is not valid!");
        }
    }


    @RequestMapping(value = "/create-room", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> CreateRoom(HttpServletRequest request, @RequestBody CreateRoomArgs args) {
        String user = (String) request.getAttribute("email");
        String storedUserJson = BackendApplication.ColdData.Get(user);
        UserValue storedUserValue = JSON.parseObject(storedUserJson, UserValue.class);

        Member owner = new Member(user, storedUserValue.getNickname());

        RoomValueCold room = new RoomValueCold();
        room.setName(args.getName());
        room.setOwner(owner);
        room.addMember(owner);
        room.setPassword(args.getPassword());
        room.setMaxUsers(args.getMaxUsers());
        Long roomSeq = BackendApplication.ColdData.Increment("roomSeq");
        DecimalFormat df = new DecimalFormat(STR_FORMAT);
        String roomNum = df.format(roomSeq);

        storedUserValue.addRoom(roomNum, args.getName());
        BackendApplication.ColdData.Set(user,JSON.toJSONString(storedUserValue));

        String storedRoomJson = JSON.toJSONString(room);

        if (BackendApplication.ColdData.Set(roomNum, storedRoomJson) && BackendApplication.HotData.Set(roomNum, "0"))
            return ResponseEntity.status(HttpStatus.OK).body(roomNum);
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("DB error!");
//        return ResponseEntity.status(HttpStatus.OK).body("success");
    }

    // 获得用户加入的所有聊天室
    @RequestMapping(value = "/getList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> GetList(HttpServletRequest request) {
        String user = (String) request.getAttribute("email");
        String storedUserJson = BackendApplication.ColdData.Get(user);
        UserValue storedUserValue = JSON.parseObject(storedUserJson, UserValue.class);

        return ResponseEntity.status(HttpStatus.OK).body(JSON.toJSONString(storedUserValue.getRoomList()));
    }

    @RequestMapping(value = "/getRoomInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> GetRoomInfo(HttpServletRequest request, @RequestBody GetRoomInfoArgs args) {
        String user = (String) request.getAttribute("email");
        String storedUserJson = BackendApplication.ColdData.Get(user);
        UserValue storedUserValue = JSON.parseObject(storedUserJson, UserValue.class);

        //判断下一个元素之后有值
        for (UserStoredRoomValue userStoredRoomValue : storedUserValue.getRoomList()) {
            if (userStoredRoomValue.getID().equals(args.getRoomID())) {
                String jsonData = BackendApplication.ColdData.Get(userStoredRoomValue.getID());
                return ResponseEntity.status(HttpStatus.OK).body(jsonData);
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The Room is not in your RoomList");
    }

    @RequestMapping(value = "/sendMessageTest", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> SendMessageTest(HttpServletRequest request, @RequestBody TestSendMsgArgs args) {
        String user = (String) request.getAttribute("email");
        System.out.println(user);
        String storedUserJson = BackendApplication.ColdData.Get(user);
        UserValue storedUserValue = JSON.parseObject(storedUserJson, UserValue.class);

        Member owner = new Member(user, storedUserValue.getNickname());

        StringBuilder retVal = new StringBuilder();

        System.out.println(args);

        MessageKey key = new MessageKey();
        key.setMessageID(Integer.parseInt(RoomHot.incrementLastMessageID(args.getId())));
        key.setRoomID(args.getId());

        System.out.println(key);

        //prepare new message
        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");

        MessageValue newMsg = new MessageValue();
        newMsg.setContent(args.getContent());
        newMsg.setTime(dateFormat.format(date));
        newMsg.setSender(owner);

        BackendApplication.HotData.Set(JSON.toJSONString(key),JSON.toJSONString(newMsg));

        return ResponseEntity.status(HttpStatus.OK).body(retVal.toString());
    }

    @RequestMapping(value = "/getMessageTest", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> GetMessageTest(HttpServletRequest request, @RequestBody TestSendMsgArgs args) {
        String user = (String) request.getAttribute("email");
        String storedUserJson = BackendApplication.ColdData.Get(user);
        UserValue storedUserValue = JSON.parseObject(storedUserJson, UserValue.class);

        Member owner = new Member(user, storedUserValue.getNickname());

        // check meta data
        if(!RoomHot.checkExistMessage(args.getId())){
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }

        ArrayList<MessageValue> retVal = new ArrayList<>();

        MessageKey key = new MessageKey();
        key.setMessageID( Integer.parseInt( RoomHot.getLastMessageID(args.getId()) ) );
        key.setRoomID(args.getId());

        for(int messageIndex = key.getMessageID();messageIndex>0;messageIndex--){
            MessageValue message = JSON.parseObject(BackendApplication.HotData.Get(JSON.toJSONString(key)),MessageValue.class);
            retVal.add(message);
            key.setMessageID(key.getMessageID() - 1);
        }


        return ResponseEntity.status(HttpStatus.OK).body(JSON.toJSONString(retVal));
    }

//    @RequestMapping(value = "/enter-room", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
//    public ResponseEntity<String> EnterRoomForFirstTime(HttpServletRequest request, @RequestBody EnterRoomArgs args) {
//        String user = (String) request.getAttribute("email");
//        String storedUserJson = BackendApplication.ColdData.Get(user);
//        UserValue storedUserValue = JSON.parseObject(storedUserJson, UserValue.class);
//        Member member = new Member(user, storedUserValue.getNickname());
//
//        String storedRoomJson = BackendApplication.ColdData.Get(args.getRoomID());
//        RoomValueCold storedRoomValue = JSON.parseObject(storedRoomJson, RoomValueCold.class);
//        if (args.isEnter()) {
//            String res = JSON.toJSONString(storedRoomValue);
//            return ResponseEntity.status(HttpStatus.OK).body(res);
//        }
//
//        if (storedRoomValue.getPassword().length() > 0) {
//            if (!args.getPassword().equals(storedRoomValue.getPassword())) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password error!");
//            }
//        }
//
//        int roomSize = storedRoomValue.getRoomSize();
//        if (storedRoomValue.getMaxUsers() > 0 && roomSize++ > storedRoomValue.getMaxUsers()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This room has been full!");
//        }
//
//        storedRoomValue.addMember(member);
//        storedRoomJson = JSON.toJSONString(storedRoomValue);
//
//        String res = JSON.toJSONString(storedRoomValue);
//        if (BackendApplication.ColdData.Set(args.getRoomID(), storedRoomJson))
//            return ResponseEntity.status(HttpStatus.OK).body(res);
//        else
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("DB error!");
//    }
}

