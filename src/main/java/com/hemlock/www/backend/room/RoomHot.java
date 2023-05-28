package com.hemlock.www.backend.room;

import com.hemlock.www.backend.BackendApplication;

public class RoomHot {
    public static String getLastMessageID(String ID){
        return BackendApplication.HotData.Get(ID);
    }

    public static String incrementLastMessageID(String ID){
        return BackendApplication.HotData.Increment(ID).toString();
    }

    public static void SetLastMessageID(String ID){
        BackendApplication.HotData.Set(ID,"0");
    }

    public static Boolean checkExistMessage(String ID){
        if(BackendApplication.HotData.Exists(ID) > 0){
            // exist meta data
            return true;
        }else{
            // no meta data
            return false;
        }
    }
}
