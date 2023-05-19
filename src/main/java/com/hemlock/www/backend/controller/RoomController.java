package com.hemlock.www.backend.controller;

import com.alibaba.fastjson2.JSON;
import com.hemlock.www.backend.BackendApplication;
import com.hemlock.www.backend.request.CreateRoomArgs;
import com.hemlock.www.backend.request.EnterRoomArgs;
import com.hemlock.www.backend.user.UserValue;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hemlock.www.backend.room.*;

import java.text.DecimalFormat;

@CrossOrigin(methods = {RequestMethod.POST})

@RestController
@RequestMapping("/room")
public class RoomController {

    private static final String STR_FORMAT = "00000000";


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


        String storedRoomJson = JSON.toJSONString(room);
        if (BackendApplication.ColdData.Set(roomNum, storedRoomJson) && BackendApplication.HotData.Set(roomNum, "0"))
            return ResponseEntity.status(HttpStatus.OK).body(roomNum);
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("DB error!");
//        return ResponseEntity.status(HttpStatus.OK).body("success");

    }

    @RequestMapping(value = "/enter-room", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> EnterRoom(HttpServletRequest request, @RequestBody EnterRoomArgs args) {
        String user = (String) request.getAttribute("email");
        String storedUserJson = BackendApplication.ColdData.Get(user);
        UserValue storedUserValue = JSON.parseObject(storedUserJson, UserValue.class);
        Member member = new Member(user, storedUserValue.getNickname());

        String storedRoomJson = BackendApplication.ColdData.Get(args.getRoomID());
        RoomValueCold storedRoomValue = JSON.parseObject(storedRoomJson, RoomValueCold.class);
        if (args.isEnter()) {
            String res = JSON.toJSONString(storedRoomValue);
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }

        if (storedRoomValue.getPassword().length() > 0) {
            if (!args.getPassword().equals(storedRoomValue.getPassword())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password error!");
            }
        }

        int roomSize = storedRoomValue.getRoomSize();
        if (storedRoomValue.getMaxUsers() > 0 && roomSize++ > storedRoomValue.getMaxUsers()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This room has been full!");
        }

        storedRoomValue.addMember(member);
        storedRoomJson = JSON.toJSONString(storedRoomValue);

        String res = JSON.toJSONString(storedRoomValue);
        if (BackendApplication.ColdData.Set(args.getRoomID(), storedRoomJson))
            return ResponseEntity.status(HttpStatus.OK).body(res);
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("DB error!");
    }
}

