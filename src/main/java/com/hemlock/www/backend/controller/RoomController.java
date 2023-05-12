package com.hemlock.www.backend.controller;

import com.alibaba.fastjson2.JSON;
import com.hemlock.www.backend.BackendApplication;
import com.hemlock.www.backend.user.UserValue;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hemlock.www.backend.room.*;

import java.text.DecimalFormat;

@CrossOrigin(methods = {RequestMethod.POST})

@RestController
@RequestMapping("/room")
public class RoomController {

    private static int roomSeq = 0;
    private static final String STR_FORMAT = "00000000";

    @RequestMapping(value = "/enter-room", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> EnterRoom(@RequestBody EnterRoomArgs args) {
        return ResponseEntity.status(HttpStatus.OK).body(args.getName());
    }

    @RequestMapping(value = "/create-room", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> CreateRoom(@RequestBody CreateRoomArgs args) {
        String storedUserJson = BackendApplication.ColdData.Get(args.getUser());
        UserValue storedUserValue = JSON.parseObject(storedUserJson, UserValue.class);

        Member owner = new Member(args.getUser(),storedUserValue.getNickname());

        RoomValueCold room = new RoomValueCold();
        room.setName(args.getName());
        room.setOwner(owner);
        room.addMember(owner);
        roomSeq++;
        DecimalFormat df = new DecimalFormat(STR_FORMAT);
        String roomNum = df.format(roomSeq);


        String storedRoomJson = JSON.toJSONString(room);
        if (BackendApplication.ColdData.Set(roomNum, storedRoomJson))
            return ResponseEntity.status(HttpStatus.OK).body("success!");
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("DB error!");
//        return ResponseEntity.status(HttpStatus.OK).body("success");

    }
}
