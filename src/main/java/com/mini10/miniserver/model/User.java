package com.mini10.miniserver.model;

import org.springframework.util.StringUtils;

import java.util.Date;
import javax.persistence.*;

@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Id
    @Column(name = "open_id")
    private String openId;

    /**
     * 工作地城市
     */
    @Column(name = "city")
    private String city;

    /**
     * 家乡城市
     */
    @Column(name = "hometown_city")
    private String hometownCity;

    /**
     * 国家
     */
    private String country;

    /**
     * 1为男 0为女
     */
    private Integer gender;

    /**
     * zh_CN
     */
    private String language;

    /**
     * 昵称
     */
    @Column(name = "nick_name")
    private String nickName;

    /**
     * 工作地省份
     */
    @Column(name = "province")
    private String province;

    /**
     * 家乡省份
     */
    @Column(name = "hometown_province")
    private String hometownProvince;

    /**
     * 学校
     */
    private String school;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 生日
     */
    private String birth;

    /**
     * 身高(cm)
     */
    private Integer height;

    /**
     * 体重(kg)
     */
    private Integer weight;

    /**
     * 星座 用星座名存
     */
    private String constellation;

    /**
     * 1 - 工作党,0 - 学生党
     */
    @Column(name = "work_status")
    private Integer workStatus;

    /**
     * 0-专科 1-本科 2-研究生 3-博士 4-其他
     */
    private Integer degree;

    /**
     * 用户添加信息增加的总分
     */
    private Integer userScore;

    private String company;

    private String job;

    @Column(name = "imgs_info")
    private String imgsInfo;

    @Column(name = "upload_imgs")
    private String uploadImgs;

    @Column(name = "wechat_id")
    private String wechatId;

    private String phone;

    private String wish;
    private String description;

    /**
     * 是否访问过动态页面  0-未访问 1-访问过
     */
    @Column(name = "activity_home")
    private Integer activityHome;

    /**
     * 是否访问过动态页面  0-未访问 1-访问过
     */
    @Column(name = "is_share")
    private Integer isShare;

    /**
     * 用户注册时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 用户信息修改时间
     */
    @Column(name = "modify_time")
    private Date modifyTime;

    /**
     * 头像url
     */
    @Column(name = "avatar_url")
    private String avatarUrl;

    /**
     * 上传文件头像url
     */
    @Column(name = "upload_avatar_url")
    private String uploadAvatarUrl;

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

    public Integer getIsShare() {
        return isShare;
    }

    public void setIsShare(Integer isShare) {
        this.isShare = isShare;
    }

    public Integer getUserScore() {
        return userScore;
    }

    public void setUserScore(Integer userScore) {
        this.userScore = userScore;
    }

    public Integer getActivityHome() {
        return activityHome;
    }

    public void setActivityHome(Integer activityHome) {
        this.activityHome = activityHome;
    }

    public String getImgsInfo() {
        return imgsInfo;
    }

    public void setImgsInfo(String imgsInfo) {
        this.imgsInfo = imgsInfo;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job == null ? null : job.trim();
    }

    public String getUploadImgs() {
        return uploadImgs;
    }

    public void setUploadImgs(String uploadImgs) {
        this.uploadImgs = uploadImgs == null ? null : uploadImgs.trim();
    }

    public String getWish() {
        return wish;
    }

    public void setWish(String wish) {
        this.wish = wish == null ? null : wish.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    /**
     * 获取工作地城市
     *
     * @return live_city - 工作地城市
     */
    public String getCity() {
        return city;
    }

    /**
     * 设置工作地城市
     *
     * @param city 工作地城市
     */
    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    /**
     * 获取家乡城市
     *
     * @return hometown_city - 家乡城市
     */
    public String getHometownCity() {
        return hometownCity;
    }

    /**
     * 设置家乡城市
     *
     * @param hometownCity 家乡城市
     */
    public void setHometownCity(String hometownCity) {
        this.hometownCity = hometownCity == null ? null : hometownCity.trim();
    }

    /**
     * 获取国家
     *
     * @return country - 国家
     */
    public String getCountry() {
        return country;
    }

    /**
     * 设置国家
     *
     * @param country 国家
     */
    public void setCountry(String country) {
        this.country = country == null ? null : country.trim();
    }

    /**
     * 获取1为男 0为女
     *
     * @return gender - 1为男 0为女
     */
    public Integer getGender() {
        return gender;
    }

    /**
     * 设置1为男 0为女
     *
     * @param gender 1为男 0为女
     */
    public void setGender(Integer gender) {
        this.gender = gender;
    }

    /**
     * 获取zh_CN
     *
     * @return language - zh_CN
     */
    public String getLanguage() {
        return language;
    }

    /**
     * 设置zh_CN
     *
     * @param language zh_CN
     */
    public void setLanguage(String language) {
        this.language = language == null ? null : language.trim();
    }

    /**
     * 获取昵称
     *
     * @return nick_name - 昵称
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * 设置昵称
     *
     * @param nickName 昵称
     */
    public void setNickName(String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
    }

    /**
     * 获取工作地省份
     *
     * @return live_province - 工作地省份
     */
    public String getProvince() {
        return province;
    }

    /**
     * 设置工作地省份
     *
     * @param province 工作地省份
     */
    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    /**
     * 获取家乡省份
     *
     * @return hometown_provice - 家乡省份
     */
    public String getHometownProvince() {
        return hometownProvince;
    }

    /**
     * 设置家乡省份
     *
     * @param hometownProvince 家乡省份
     */
    public void setHometownProvince(String hometownProvince) {
        this.hometownProvince = hometownProvince == null ? null : hometownProvince.trim();
    }

    /**
     * 获取学校
     *
     * @return school - 学校
     */
    public String getSchool() {
        return school;
    }

    /**
     * 设置学校
     *
     * @param school 学校
     */
    public void setSchool(String school) {
        this.school = school == null ? null : school.trim();
    }

    /**
     * 获取年龄
     *
     * @return age - 年龄
     */
    public Integer getAge() {
        return age;
    }

    /**
     * 设置年龄
     *
     * @param age 年龄
     */
    public void setAge(Integer age) {
        this.age = age;
    }

    /**
     * 获取生日
     *
     * @return birth - 生日
     */
    public String getBirth() {
        return birth;
    }

    /**
     * 设置生日
     *
     * @param birth 生日
     */
    public void setBirth(String birth) {
        this.birth = birth == null ? null : birth.trim();
    }

    /**
     * 获取身高(cm)
     *
     * @return height - 身高(cm)
     */
    public Integer getHeight() {
        return height;
    }

    /**
     * 设置身高(cm)
     *
     * @param height 身高(cm)
     */
    public void setHeight(Integer height) {
        this.height = height;
    }

    /**
     * 获取体重(kg)
     *
     * @return weight - 体重(kg)
     */
    public Integer getWeight() {
        return weight;
    }

    /**
     * 设置体重(kg)
     *
     * @param weight 体重(kg)
     */
    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    /**
     * 获取星座 用星座名存
     *
     * @return constellation - 星座 用星座名存
     */
    public String getConstellation() {
        return constellation;
    }

    /**
     * 设置星座 用星座名存
     *
     * @param constellation 星座 用星座名存
     */
    public void setConstellation(String constellation) {
        this.constellation = constellation == null ? null : constellation.trim();
    }

    /**
     * 获取工作党,学生党
     *
     * @return work_status - 工作党,学生党
     */
    public Integer getWorkStatus() {
        return workStatus;
    }

    /**
     * 设置工作党,学生党
     *
     * @param workStatus 工作党,学生党
     */
    public void setWorkStatus(Integer workStatus) {
        this.workStatus = workStatus;
    }

    /**
     * 获取0-专科 1-本科 2-研究生 3-博士 4-其他
     *
     * @return degree - 0-专科 1-本科 2-研究生 3-博士 4-其他
     */
    public Integer getDegree() {
        return degree;
    }

    /**
     * 设置0-专科 1-本科 2-研究生 3-博士 4-其他
     *
     * @param degree 0-专科 1-本科 2-研究生 3-博士 4-其他
     */
    public void setDegree(Integer degree) {
        this.degree = degree;
    }

    /**
     * @return wechat_id
     */
    public String getWechatId() {
        return wechatId;
    }

    /**
     * @param wechatId
     */
    public void setWechatId(String wechatId) {
        this.wechatId = wechatId == null ? null : wechatId.trim();
    }

    /**
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * 获取用户注册时间
     *
     * @return create_time - 用户注册时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置用户注册时间
     *
     * @param createTime 用户注册时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取用户信息修改时间
     *
     * @return modify_time - 用户信息修改时间
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * 设置用户信息修改时间
     *
     * @param modifyTime 用户信息修改时间
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * 获取头像url
     *
     * @return avatar_url - 头像url
     */
    public String getAvatarUrl() {
        return avatarUrl;
    }

    /**
     * 设置头像url
     *
     * @param avatarUrl 头像url
     */
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl == null ? null : avatarUrl.trim();
    }

    /**
     * 获取上传文件头像url
     *
     * @return upload_avatar_url - 上传文件头像url
     */
    public String getUploadAvatarUrl() {
        return uploadAvatarUrl;
    }

    /**
     * 设置上传文件头像url
     *
     * @param uploadAvatarUrl 上传文件头像url
     */
    public void setUploadAvatarUrl(String uploadAvatarUrl) {
        this.uploadAvatarUrl = uploadAvatarUrl == null ? null : uploadAvatarUrl.trim();
    }
}