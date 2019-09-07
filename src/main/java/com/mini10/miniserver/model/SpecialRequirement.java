package com.mini10.miniserver.model;

import javax.persistence.*;

@Table(name = "special_requirement")
public class SpecialRequirement {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "open_id")
    private String openId;

    /**
     * 最高身高
     */
    @Column(name = "height_min")
    private Integer heightMin;

    /**
     * 最低身高
     */
    @Column(name = "height_max")
    private Integer heightMax;

    /**
     * 最小体重
     */
    @Column(name = "weight_min")
    private Integer weightMin;

    /**
     * 最大体重
     */
    @Column(name = "weight_max")
    private Integer weightMax;

    /**
     * 要求学校
     */
    private String school;

    /**
     * 要求城市
     */
    private String city;

    /**
     * 家乡城市
     */
    private String hometownCity;

    /**
     * 家乡省份
     */
    private String hometownProvince;

    /**
     * 工作地省份
     */
    private String province;

    /**
     * 星座
     */
    private String constellation;

    /**
     * 0-专科 1-本科 2-研究生 3-博士 4-其他
     */
    private Integer degree;

    /**
     *  0-学生党  1-工作党
     */
    private Integer workStatus;

    /**
     * 年龄最小
     */
    private Integer ageMin;

    /**
     * 年龄最大
     */
    private Integer ageMax;
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
     * 获取最高身高
     *
     * @return height_min - 最高身高
     */
    public Integer getHeightMin() {
        return heightMin;
    }

    /**
     * 设置最高身高
     *
     * @param heightMin 最高身高
     */
    public void setHeightMin(Integer heightMin) {
        this.heightMin = heightMin;
    }

    /**
     * 获取最低身高
     *
     * @return height_max - 最低身高
     */
    public Integer getHeightMax() {
        return heightMax;
    }

    /**
     * 设置最低身高
     *
     * @param heightMax 最低身高
     */
    public void setHeightMax(Integer heightMax) {
        this.heightMax = heightMax;
    }

    /**
     * 获取最小体重
     *
     * @return weight_min - 最小体重
     */
    public Integer getWeightMin() {
        return weightMin;
    }

    /**
     * 设置最小体重
     *
     * @param weightMin 最小体重
     */
    public void setWeightMin(Integer weightMin) {
        this.weightMin = weightMin;
    }

    /**
     * 获取最大体重
     *
     * @return weight_max - 最大体重
     */
    public Integer getWeightMax() {
        return weightMax;
    }

    /**
     * 设置最大体重
     *
     * @param weightMax 最大体重
     */
    public void setWeightMax(Integer weightMax) {
        this.weightMax = weightMax;
    }

    /**
     * 获取要求学校
     *
     * @return school - 要求学校
     */
    public String getSchool() {
        return school;
    }

    /**
     * 设置要求学校
     *
     * @param school 要求学校
     */
    public void setSchool(String school) {
        this.school = school == null ? null : school.trim();
    }

    /**
     * 获取要求城市
     *
     * @return city - 要求城市
     */
    public String getCity() {
        return city;
    }

    /**
     * 设置要求城市
     *
     * @param city 要求城市
     */
    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getHometownCity() {
        return hometownCity;
    }

    public void setHometownCity(String hometownCity) {
        this.hometownCity = hometownCity;
    }

    public String getHometownProvince() {
        return hometownProvince;
    }

    public void setHometownProvince(String hometownProvince) {
        this.hometownProvince = hometownProvince;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public Integer getDegree() {
        return degree;
    }

    public void setDegree(Integer degree) {
        this.degree = degree;
    }

    public Integer getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(Integer workStatus) {
        this.workStatus = workStatus;
    }

    public Integer getAgeMin() {
        return ageMin;
    }

    public void setAgeMin(Integer ageMin) {
        this.ageMin = ageMin;
    }

    public Integer getAgeMax() {
        return ageMax;
    }

    public void setAgeMax(Integer ageMax) {
        this.ageMax = ageMax;
    }
}