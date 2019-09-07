package com.mini10.miniserver.model.param;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

public class DynamicDto{

    private Integer id;

    private String openId;

    private String diary;

    private String poster;

    // 情感倾向度

    private Integer emotionRate;

    private Boolean secret;
    // 填写时间

    private Date createTime;

    private String location;

    // 群ID
    private String groupId;

    // 匹配结果
    private Double matchResult;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    //用户昵称
    private String nickName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getDiary() {
        return diary;
    }

    public void setDiary(String diary) {
        this.diary = diary;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public Integer getEmotionRate() {
        return emotionRate;
    }

    public void setEmotionRate(Integer emotionRate) {
        this.emotionRate = emotionRate;
    }

    public Boolean getSecret() {
        return secret;
    }

    public void setSecret(Boolean secret) {
        this.secret = secret;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Double getMatchResult() {
        return matchResult;
    }

    public void setMatchResult(Double matchResult) {
        this.matchResult = matchResult;
    }

    public DynamicDto(Integer id, String openId, String diary, String poster, Integer emotionRate, Boolean secret, Date createTime, String location, String groupId, Double matchResult) {
        this.id = id;
        this.openId = openId;
        this.diary = diary;
        this.poster = poster;
        this.emotionRate = emotionRate;
        this.secret = secret;
        this.createTime = createTime;
        this.location = location;
        this.groupId = groupId;
        this.matchResult = matchResult;
    }

    public DynamicDto() {
    }


}
