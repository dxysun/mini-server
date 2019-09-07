package com.mini10.miniserver.common.utils;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component("httpUtil")
public class HttpUtil {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RestTemplate restTemplate;


    public JSONObject doPost(String wxUrl,JSONObject resultJson){
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        HttpEntity<String> httpEntity = new HttpEntity<>(resultJson.toJSONString(), headers);
        return restTemplate.postForObject(wxUrl, httpEntity, JSONObject.class);

    }

    public JSONObject doGet(String wxUrl){
        String result = restTemplate.getForObject(wxUrl,String.class);
        return JSONObject.parseObject(result);
    }

    public byte[] doPostForBuffer(String wxUrl,JSONObject resultJson){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> httpEntity = new HttpEntity<>(resultJson.toJSONString(), headers);
        ResponseEntity<byte[]> entity = restTemplate.exchange(wxUrl, HttpMethod.POST, httpEntity, byte[].class);
        return entity.getBody();
    }

}
