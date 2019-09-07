package com.mini10.miniserver.model.param;

import java.util.List;

/**
 * @Author ChenShaoJie
 * @Date 2019-07-22 15:47
 */
public class MatchGroupResult {
    private String groupId;
    private String groupName;
    private Integer memberNumber = 0; //这个群参加该活动的人数
    private Integer perfectMatchNumber = 0; //这个群内与用户完美匹配的人数
    private Integer oppositeSexNumber = 0;

    public Integer getOppositeSexNumber() {
        return oppositeSexNumber;
    }

    public void setOppositeSexNumber(Integer oppositeSexNumber) {
        this.oppositeSexNumber = oppositeSexNumber;
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

    public Integer getMemberNumber() {
        return memberNumber;
    }

    public void setMemberNumber(Integer memberNumber) {
        this.memberNumber = memberNumber;
    }

    public Integer getPerfectMatchNumber() {
        return perfectMatchNumber;
    }

    public void setPerfectMatchNumber(Integer perfectMatchNumber) {
        this.perfectMatchNumber = perfectMatchNumber;
    }


}
