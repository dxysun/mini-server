package com.mini10.miniserver.model;

import javax.persistence.*;

@Table(name = "dynamics_blacklist")
public class DynamicsBlacklist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "open_id")
    private String openId;

    @Column(name = "dynamic_id")
    private Integer dynamicId;

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
     * @return open_id
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
     * @return dynamic_id
     */
    public Integer getDynamicId() {
        return dynamicId;
    }

    /**
     * @param dynamicId
     */
    public void setDynamicId(Integer dynamicId) {
        this.dynamicId = dynamicId;
    }
}