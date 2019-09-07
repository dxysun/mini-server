package com.mini10.miniserver.service;

import com.alibaba.fastjson.JSONObject;
import com.mini10.miniserver.model.User;

import java.util.Map;


public interface UserService {

    JSONObject getOpenInfoByCode(String code);

    User getUserByOpenId(String openId);

    void registerUser(User user);

    Integer updateUserData(User user);

    Integer getUserScore(String openId);

    String getNickNameByOpenId(String openId);
}
