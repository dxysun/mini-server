package com.mini10.miniserver.model;

import javax.persistence.*;

@Table(name = "group_relation")
public class GroupRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 群ID
     */
    private String openId;

    /**
     * 群里用户的openId
     */
    private String groupId;

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
     * 获取群ID
     *
     * @return openid - openID
     */
    public String getOpenId() {
        return openId;
    }

    /**
     * 设置群ID
     *
     * @param openId openID
     */
    public void setOpenId(String openId) {
        this.openId = openId == null ? null : openId.trim();
    }

    /**
     * 获取群里用户的openId
     *
     * @return groupid - 群里用户的openId
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * 设置群里用户的openId
     *
     * @param groupId 群里用户的openId
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId == null ? null : groupId.trim();
    }
}