package com.mini10.miniserver.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "virtual_group_info")
public class VirtualGroupInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 虚拟群ID
     */
    @Id
    @Column(name = "virtual_group_id")
    private String virtualGroupId;

    /**
     * 虚拟群名称
     */
    @Column(name = "virtual_group_name")
    private String virtualGroupName;

    /**
     * 虚拟群创建者id
     */
    @Column(name = "creator_id")
    private String creatorId;

    private String district;

    private String adcode;

    private String city;

    private String citycode;

    private String province;

    /**
     * 群创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name = "modify_time")
    private Date modifyTime;

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
     * 获取虚拟群ID
     *
     * @return virtual_group_id - 虚拟群ID
     */
    public String getVirtualGroupId() {
        return virtualGroupId;
    }

    /**
     * 设置虚拟群ID
     *
     * @param virtualGroupId 虚拟群ID
     */
    public void setVirtualGroupId(String virtualGroupId) {
        this.virtualGroupId = virtualGroupId == null ? null : virtualGroupId.trim();
    }

    /**
     * 获取虚拟群名称
     *
     * @return virtual_group_name - 虚拟群名称
     */
    public String getVirtualGroupName() {
        return virtualGroupName;
    }

    /**
     * 设置虚拟群名称
     *
     * @param virtualGroupName 虚拟群名称
     */
    public void setVirtualGroupName(String virtualGroupName) {
        this.virtualGroupName = virtualGroupName == null ? null : virtualGroupName.trim();
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId == null ? null : creatorId.trim();
    }

    /**
     * @return district
     */
    public String getDistrict() {
        return district;
    }

    /**
     * @param district
     */
    public void setDistrict(String district) {
        this.district = district == null ? null : district.trim();
    }

    /**
     * @return adcode
     */
    public String getAdcode() {
        return adcode;
    }

    /**
     * @param adcode
     */
    public void setAdcode(String adcode) {
        this.adcode = adcode == null ? null : adcode.trim();
    }

    /**
     * @return city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city
     */
    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    /**
     * @return citycode
     */
    public String getCitycode() {
        return citycode;
    }

    /**
     * @param citycode
     */
    public void setCitycode(String citycode) {
        this.citycode = citycode == null ? null : citycode.trim();
    }

    /**
     * @return province
     */
    public String getProvince() {
        return province;
    }

    /**
     * @param province
     */
    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    /**
     * 获取群创建时间
     *
     * @return create_time - 群创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置群创建时间
     *
     * @param createTime 群创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取修改时间
     *
     * @return modify_time - 修改时间
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * 设置修改时间
     *
     * @param modifyTime 修改时间
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}