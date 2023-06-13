package com.hemlock.www.backend.ChatBot;

import lombok.Data;

import java.util.ArrayList;

@Data
public class ChatGLMHistory {
    public ArrayList<ArrayList<String>> history = new ArrayList<>();
}
