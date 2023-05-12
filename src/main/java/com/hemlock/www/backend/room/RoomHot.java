package com.hemlock.www.backend.room;

import com.hemlock.www.backend.BackendApplication;
import lombok.Data;

@Data
public class RoomHot {
    public String getLastMessageID(String ID){
        return BackendApplication.HotData.Get(ID);
    }

    public String incrementLastMessageID(String ID){
        return BackendApplication.HotData.Increment(ID).toString();
    }
}
