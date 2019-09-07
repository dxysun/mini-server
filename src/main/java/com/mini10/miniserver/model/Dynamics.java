package com.mini10.miniserver.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author corn1ng
 */
public class Dynamics {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private String openId;

    private String diary;

    private String poster;

    // 情感倾向度
    @Column(name ="emotion_rate")
    private Integer emotionRate;

    private Boolean secret;

    // 填写时间
    @Column(name = "create_time")
    private Date createTime;

    private String location;


    public Dynamics(String openId, String diary, String poster, Integer emotionRate, Boolean secret, Date createTime, String location) {
        this.openId = openId;
        this.diary = diary;
        this.poster = poster;
        this.emotionRate = emotionRate;
        this.secret = secret;
        this.createTime = createTime;
        this.location =location;
    }

    public Dynamics() {
    }

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
}
