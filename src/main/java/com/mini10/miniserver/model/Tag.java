package com.mini10.miniserver.model;

import javax.persistence.*;

@Table(name = "tag")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String tagName;

    /**
     * 0-外在,1-内在,2-兴趣3-其他
     */
    private Integer tagClassify;

    /**
     * 0 男对女的要求,1 男生标签,2 女生对男生的要求,3 女生标签
     */
    private Integer tagType;

    private Integer sort;

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

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
     * 0-外在,1-内在,2-兴趣3-其他
     * @return
     */
    public Integer getTagClassify() {
        return tagClassify;
    }

    /**
     * 0-外在,1-内在,2-兴趣3-其他
     * @param tagClassify
     */
    public void setTagClassify(Integer tagClassify) {
        this.tagClassify = tagClassify;
    }

    /**
     * @return name
     */
    public String getTagName() {
        return tagName;
    }

    /**
     * @param tagName
     */
    public void setTagName(String tagName) {
        this.tagName = tagName == null ? null : tagName.trim();
    }

    /**
     * 获取1为男 0为女
     *
     * @return gender - 1为男 0为女
     */
    public Integer getTagType() {
        return tagType;
    }

    /**
     * 设置
     *
     * @param tagType
     */
    public void setTagType(Integer tagType) {
        this.tagType = tagType;
    }


}