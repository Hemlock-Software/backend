package com.hemlock.www.backend.request;

import lombok.Data;

@Data
public class RemoveMemberArgs {
    private String roomID;    //聊天室的ID{
    private String mail;    //踢出的用户的邮箱
}
