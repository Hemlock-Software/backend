package com.hemlock.www.backend.ChatBot;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.Data;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

@Data
class ChatGLMResponse{
    private String response;
    private String history;
    private int status;
    private String time;
};

public class ChatGLM {
    static String Url = "http://10.112.11.58:8000/short";

    public static String getMessage(String input){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());

        JSONObject param = new JSONObject();
        param.put("prompt", input);

        HttpEntity<String> formEntity = new HttpEntity<String>(param.toJSONString(), headers);

        try {
            String result = restTemplate.postForObject(Url, formEntity, String.class);
            ChatGLMResponse response = JSON.parseObject(result,ChatGLMResponse.class);

            return response.getResponse();
        }catch (Exception e){
            return "ChatGLM暂时掉线";
        }
    }
}
