package com.mini10.miniserver.mapper;

import com.mini10.miniserver.model.VirtualGroupRelation;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Component
public interface VirtualGroupRelationMapper extends Mapper<VirtualGroupRelation> {

    Integer addVirtualGroupRelationInfo(VirtualGroupRelation virtualGroupRelation);

    List<String> getOpenIdsByGroupId(@Param("virtualGroupId") String virtualGroupId);
}