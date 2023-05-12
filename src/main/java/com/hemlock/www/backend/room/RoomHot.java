package com.hemlock.www.backend.room;

import com.hemlock.www.backend.BackendApplication;
import lombok.Data;

@Data
public class RoomHot {
    private String ID;                   //Room序号

    public String getLastMessageID(){
        return BackendApplication.HotData.Get(ID);
    }

    public String incrementLastMessageID(){
        return BackendApplication.HotData.Increment(ID).toString();
    }
}
