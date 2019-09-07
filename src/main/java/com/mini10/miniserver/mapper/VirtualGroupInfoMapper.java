package com.mini10.miniserver.mapper;

import com.mini10.miniserver.model.VirtualGroupInfo;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Component
public interface VirtualGroupInfoMapper extends Mapper<VirtualGroupInfo> {

    Integer addVirtualGroupInfo(VirtualGroupInfo virtualGroupInfo);

    VirtualGroupInfo getVirtualGroupInfoByGroupId(String virtualGroupId);

    List<VirtualGroupInfo> getVirtualGroupListByPosition(VirtualGroupInfo virtualGroupInfo);
    List<VirtualGroupInfo> getVirtualGroupListByCreatorId(String  creatorId);
}