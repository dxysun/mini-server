package com.mini10.miniserver.service;

import com.mini10.miniserver.model.GroupRelation;
import com.mini10.miniserver.model.VirtualGroupInfo;
import com.mini10.miniserver.model.VirtualGroupRelation;

import java.util.List;

/**
 * @Author ChenShaoJie
 * @Date 2019-07-26 17:19
 */
public interface GroupService {

    boolean addGroupRelationInfo(GroupRelation groupRelation);

    boolean addVirtualGroupRelationInfo(VirtualGroupRelation virtualGroupRelation);

    boolean addVirtualGroupInfo(String openId, VirtualGroupInfo virtualGroupInfo);

    List<String> getGroupIdsByOpenId(String openId);

    VirtualGroupInfo getVirtualGroupByPosition(String openId, String longitude,String latitude);

    /**
     * 根据openId和groupId删除群组信息
     *
     * @param groupId
     * @param openId
     * @return
     */
    boolean deleteGroupInfo(String groupId, String openId);
}
