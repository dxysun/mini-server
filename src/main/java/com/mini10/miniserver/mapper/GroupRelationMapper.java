package com.mini10.miniserver.mapper;

import com.mini10.miniserver.model.GroupRelation;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
@Component
public interface GroupRelationMapper extends Mapper<GroupRelation> {

    /**
     * 通过用户OpenId获取该用户参加的所有的群
     * @param openId
     * @return
     */
    List<String> getGroupIdsByOpenId(@Param("openId") String openId);

    /**
     * 通过群组Id获取该群组中所有的群员OpenId
     * @param groupId
     * @return
     */
    List<String> getOpenIdsByGroupId(@Param("groupId") String groupId);

    /**
     * 添加群组关系信息
     * @param groupRelation
     * @return
     */
    Integer addGroupRelationInfo(GroupRelation groupRelation);

    Integer deleteGroupInfo(GroupRelation groupRelation);


 }