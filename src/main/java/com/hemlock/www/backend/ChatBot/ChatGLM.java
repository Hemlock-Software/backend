package com.hemlock.www.backend.ChatBot;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.Data;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.ArrayList;

@Data
class ChatGLMResponse{
    private String response;
    private String history;
    private int status;
    private String time;
};

public class ChatGLM {
    static private String Url = "http://10.112.11.58:8000/short";
    static private String Url2 = "http://10.112.11.58:8000/";
    static public String CHAT_GLM_PREFIX = "@ChatGLM";

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

    public static String getMessageWithContext(String input, ArrayList<ArrayList<String>> history){
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofSeconds(10)) //连接超时时间10秒
                .setReadTimeout(Duration.ofSeconds(30)); //读取超时时间30秒

        RestTemplate restTemplate = restTemplateBuilder.build();


        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());

        JSONObject param = new JSONObject();
        param.put("prompt", input);
        param.put("history", history);

        HttpEntity<String> formEntity = new HttpEntity<String>(param.toJSONString(), headers);

        try {
            String result = restTemplate.postForObject(Url2, formEntity, String.class);
            ChatGLMResponse response = JSON.parseObject(result,ChatGLMResponse.class);

            return response.getResponse();
        }catch (Exception e){
            return "ChatGLM暂时掉线";
        }
    }
}
