package com.mini10.miniserver.model.param;

import com.mini10.miniserver.model.Tag;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author ChenShaoJie
 * @Date 2019-07-19 15:57
 * 用户匹配结果类
 */
public class MatchingResult implements Comparable<MatchingResult> {
    private String selfOpenId;  //使用者的openId
    private String selfNickName;     //使用者的昵称
    private String selfAvatarUrl;   //使用者头像Url
    private String targetNickName;  //匹配目标的昵称
    private String targetAvatarUrl;     //匹配目标的头像Url
    private String targetOpenId;    //匹配目标的openId
    private String groupId; //匹配的群组Id
    private String groupName; //匹配的群名称
   /* private List<Integer> qualifiedSelfTagIds; //对方符合我条件的标签Id
    private List<Integer> qualifiedTargetTagIds; //我符合对方的标签Id*/
    private List<Tag> qualifiedSelfNecessaryTags; //对方符合我条件的标签
    private List<Tag> qualifiedTargetNecessaryTags; //我符合对方的标签
    private List<Tag> qualifiedSelfRequireTags;
    private List<Tag> qualifiedTargetRequireTags;


    /*private List<String> qualifiedSelfSpecialRequires; //对方符合我的特殊数值的需求
    private List<String> qualifiedTargetSpecialRequires; //我符合对方的特殊数值的需求*/

    private double matchScore; //匹配分数

    public MatchingResult() {
        /*qualifiedSelfTagIds = new ArrayList<>();
        qualifiedTargetTagIds = new ArrayList<>();
        qualifiedSelfSpecialRequires = new ArrayList<>();
        qualifiedTargetSpecialRequires = new ArrayList<>();*/
        qualifiedSelfNecessaryTags = new ArrayList<>();
        qualifiedTargetNecessaryTags = new ArrayList<>();
        qualifiedSelfRequireTags = new ArrayList<>();
        qualifiedTargetRequireTags = new ArrayList<>();
    }

    public String getSelfOpenId() {
        return selfOpenId;
    }

    public void setSelfOpenId(String selfOpenId) {
        this.selfOpenId = selfOpenId;
    }

    public String getSelfNickName() {
        return selfNickName;
    }

    public void setSelfNickName(String selfNickName) {
        this.selfNickName = selfNickName;
    }

    public String getSelfAvatarUrl() {
        return selfAvatarUrl;
    }

    public void setSelfAvatarUrl(String selfAvatarUrl) {
        this.selfAvatarUrl = selfAvatarUrl;
    }

    public String getTargetNickName() {
        return targetNickName;
    }

    public void setTargetNickName(String targetNickName) {
        this.targetNickName = targetNickName;
    }

    public String getTargetAvatarUrl() {
        return targetAvatarUrl;
    }

    public void setTargetAvatarUrl(String targetAvatarUrl) {
        this.targetAvatarUrl = targetAvatarUrl;
    }

    public String getTargetOpenId() {
        return targetOpenId;
    }

    public void setTargetOpenId(String targetOpenId) {
        this.targetOpenId = targetOpenId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }



    public double getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(double matchScore) {
        this.matchScore = matchScore;
    }


    public List<Tag> getQualifiedSelfRequireTags() {
        return qualifiedSelfRequireTags;
    }

    public void setQualifiedSelfRequireTags(List<Tag> qualifiedSelfRequireTags) {
        this.qualifiedSelfRequireTags = qualifiedSelfRequireTags;
    }

    public List<Tag> getQualifiedTargetRequireTags() {
        return qualifiedTargetRequireTags;
    }

    public void setQualifiedTargetRequireTags(List<Tag> qualifiedTargetRequireTags) {
        this.qualifiedTargetRequireTags = qualifiedTargetRequireTags;
    }

    public List<Tag> getQualifiedSelfNecessaryTags() {
        return qualifiedSelfNecessaryTags;
    }

    public void setQualifiedSelfNecessaryTags(List<Tag> qualifiedSelfNecessaryTags) {
        this.qualifiedSelfNecessaryTags = qualifiedSelfNecessaryTags;
    }

    public List<Tag> getQualifiedTargetNecessaryTags() {
        return qualifiedTargetNecessaryTags;
    }

    public void setQualifiedTargetNecessaryTags(List<Tag> qualifiedTargetNecessaryTags) {
        this.qualifiedTargetNecessaryTags = qualifiedTargetNecessaryTags;
    }


    @Override
    public int compareTo(MatchingResult o) {
        if(this.getTargetOpenId().equals(o.getTargetOpenId()))
        {
            return 0;
        }
        else
        {
            if(this.getMatchScore()>=o.getMatchScore())
            {
                return -1;
            }
            else
            {
                return 1;
            }
        }

    }


//    @Override
//    public boolean equals(Object o) {
//        if (this == o) {
//            return true;
//        }
//        if (o == null || getClass() != o.getClass()) {
//            return false;
//        }
//        MatchingResult that = (MatchingResult) o;
//        return targetOpenId.equals(that.targetOpenId);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(targetOpenId);
//    }


}
