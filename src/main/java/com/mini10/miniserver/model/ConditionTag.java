package com.mini10.miniserver.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "condition_tag")
public class ConditionTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String openId;

    /**
     * 自身标签名称
     */
    @Column(name = "condition_tag_name")
    private String conditionTagName;

    /**
     * 自身标签ID
     */
    @Column(name = "condition_tag_id")
    private Integer conditionTagId;

    /**
     * 用户添加该条件的时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return openId
     */
    public String getOpenId() {
        return openId;
    }

    /**
     * @param openId
     */
    public void setOpenId(String openId) {
        this.openId = openId == null ? null : openId.trim();
    }

    /**
     * 获取自身标签名称
     *
     * @return condition_tag_name - 自身标签名称
     */
    public String getConditionTagName() {
        return conditionTagName;
    }

    /**
     * 设置自身标签名称
     *
     * @param conditionTagName 自身标签名称
     */
    public void setConditionTagName(String conditionTagName) {
        this.conditionTagName = conditionTagName == null ? null : conditionTagName.trim();
    }

    /**
     * 获取自身标签ID
     *
     * @return condition_tag_id - 自身标签ID
     */
    public Integer getConditionTagId() {
        return conditionTagId;
    }

    /**
     * 设置自身标签ID
     *
     * @param conditionTagId 自身标签ID
     */
    public void setConditionTagId(Integer conditionTagId) {
        this.conditionTagId = conditionTagId;
    }

    /**
     * 获取用户添加该条件的时间
     *
     * @return create_time - 用户添加该条件的时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置用户添加该条件的时间
     *
     * @param createTime 用户添加该条件的时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}