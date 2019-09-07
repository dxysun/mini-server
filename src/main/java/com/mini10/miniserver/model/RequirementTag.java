package com.mini10.miniserver.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "requirement_tag")
public class RequirementTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String openId;

    /**
     * 用户需求标签名称
     */
    @Column(name = "require_tag_name")
    private String requireTagName;

    /**
     * 用户需求标签ID
     */
    @Column(name = "require_tag_id")
    private Integer requireTagId;

    /**
     * 用户添加该需求的时间
     */
    @Column(name = "create_time")
    private Date createTime;
    /**
     * 必要性 1:必要 0:非必要
     */
    @Column(name = "necessary")
    private Integer necessary;

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
     * 获取用户需求标签名称
     *
     * @return require_tag_name - 用户需求标签名称
     */
    public String getRequireTagName() {
        return requireTagName;
    }

    /**
     * 设置用户需求标签名称
     *
     * @param requireTagName 用户需求标签名称
     */
    public void setRequireTagName(String requireTagName) {
        this.requireTagName = requireTagName == null ? null : requireTagName.trim();
    }

    /**
     * 获取用户需求标签ID
     *
     * @return require_tag_id - 用户需求标签ID
     */
    public Integer getRequireTagId() {
        return requireTagId;
    }

    /**
     * 设置用户需求标签ID
     *
     * @param requireTagId 用户需求标签ID
     */
    public void setRequireTagId(Integer requireTagId) {
        this.requireTagId = requireTagId;
    }

    /**
     * 获取用户添加该需求的时间
     *
     * @return create_time - 用户添加该需求的时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置用户添加该需求的时间
     *
     * @param createTime 用户添加该需求的时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getNecessary() {
        return necessary;
    }

    public void setNecessary(Integer necessary) {
        this.necessary = necessary;
    }
}