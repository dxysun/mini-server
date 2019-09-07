package com.mini10.miniserver.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mini10.miniserver.common.Constant;
import com.mini10.miniserver.common.utils.HttpUtil;
import com.mini10.miniserver.mapper.UserMapper;
import com.mini10.miniserver.model.User;
import com.mini10.miniserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private HttpUtil httpUtil;

    @Resource
    private UserMapper userMapper;

    @Override
    public JSONObject getOpenInfoByCode(String code) {
        StringBuilder url = new StringBuilder("https://api.weixin.qq.com/sns/jscode2session?");
        // appid设置
        url.append("appid=");
        url.append(Constant.APPID);
        // secret设置
        url.append("&secret=");
        url.append(Constant.APPSECRET);
        // code设置
        url.append("&js_code=");
        url.append(code);
        url.append("&grant_type=authorization_code");
        JSONObject json = httpUtil.doGet(url.toString());
        if (json == null || StringUtils.isEmpty(json.getString("openid"))){
            return null;
        }
        return json;
    }

    @Override
    public User getUserByOpenId(String openId) {
        if (!StringUtils.isEmpty(openId)){
            return userMapper.queryUserByOpenId(openId);
        }
        return null;
    }

    @Override
    public void registerUser(User user) {
        if (user != null){
            userMapper.addUser(user);
        }
    }

    @Override
    public Integer updateUserData(User user) {
        //多填一项信息加2分，带头像的图片加6分
        Integer score = 0;
        if (!StringUtils.isEmpty(user.getUploadAvatarUrl())) {
            score += 2;
        }
        if (!StringUtils.isEmpty(user.getHometownCity())) {
            score += 2;
        }
        if (!StringUtils.isEmpty(user.getHometownProvince())) {
            score += 2;
        }
        if (!StringUtils.isEmpty(user.getSchool())) {
            score += 2;
        }
        if (user.getAge() != null && user.getAge() != 0) {
            score += 2;
        }
        if (!StringUtils.isEmpty(user.getBirth())) {
            score += 2;
        }
        if (user.getHeight() != null && user.getHeight() != 0) {
            score += 2;
        }
        if (user.getWeight() != null && user.getWeight() != 0) {
            score += 2;
        }
        if (user.getWorkStatus() != null) {
            score += 2;
        }
        if (user.getDegree() != null) {
            score += 2;
        }
        if (!StringUtils.isEmpty(user.getConstellation())) {
            score += 2;
        }
        if (!StringUtils.isEmpty(user.getCompany())) {
            score += 2;
        }
        if (!StringUtils.isEmpty(user.getJob())) {
            score += 2;
        }
        if (!StringUtils.isEmpty(user.getWechatId())) {
            score += 2;
        }
        if (!StringUtils.isEmpty(user.getPhone())) {
            score += 2;
        }
        if (!StringUtils.isEmpty(user.getImgsInfo())) {
            JSONArray imgInfos = JSONObject.parseArray(user.getImgsInfo());
            for (Object imgInfo : imgInfos) {
                JSONObject img = (JSONObject) imgInfo;
                if (img.getInteger("score") > 0) {
                    score += 6;
                }
            }
        }
        user.setUserScore(score);
        return userMapper.updateUserDate(user);
    }

    @Override
    public Integer getUserScore(String openId) {
        User user = userMapper.queryUserByOpenId(openId);
        if(user.getUserScore() != null){
            return user.getUserScore();
        }else{
            return 0;
        }
    }

    @Override
    public String getNickNameByOpenId(String openId){
        return userMapper.queryNickNameByOpenId(openId);
    }
}
