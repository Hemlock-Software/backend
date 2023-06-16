package com.hemlock.www.backend.room;

import com.alibaba.fastjson2.JSON;
import com.hemlock.www.backend.BackendApplication;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class RoomHot {
//    public static String getLastMessageID(String ID){
//        return BackendApplication.HotData.Get(ID);
//    }
//
//    public static String incrementLastMessageID(String ID){
//        return BackendApplication.HotData.Increment(ID).toString();
//    }
//
//    public static void SetLastMessageID(String ID){
//        BackendApplication.HotData.Set(ID,"0");
//    }
//
//    public static Boolean checkExistMessage(String ID){
//        if(BackendApplication.HotData.Exists(ID) > 0){
//            // exist meta data
//            return true;
//        }else{
//            // no meta data
//            return false;
//        }
//    }

    public static List<String> getMessageRange(String ID, int start, int end){
        return BackendApplication.HotData.GetListRange(ID,start,end);
    }

    public static void addMessage(String ID,String val){
        BackendApplication.HotData.ListRPush(ID,val);
    }

    public static void delAllMessage(String ID){
        BackendApplication.HotData.Del(ID);
    }

    public static void sendTempMessage(String ID,MessageValue msg){
        BackendApplication.HotData.SendMSG(ID,JSON.toJSONString(msg));
    }

//    public static void createNewList(String ID,Member creator){
//        Date date = new Date();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd :HH:mm:ss");
//        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
//        MessageValue newMsg = new MessageValue();
//        newMsg.setContent("Welcome to chat!");
//        newMsg.setTime(dateFormat.format(date));
//        newMsg.setSender(creator);
//
//        BackendApplication.HotData.ListRPush(ID, JSON.toJSONString(newMsg));
//    }
}
