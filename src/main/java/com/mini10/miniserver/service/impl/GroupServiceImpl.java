package com.mini10.miniserver.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mini10.miniserver.common.utils.AjaxObject;
import com.mini10.miniserver.common.utils.RequestUtil;
import com.mini10.miniserver.mapper.GroupRelationMapper;
import com.mini10.miniserver.mapper.VirtualGroupInfoMapper;
import com.mini10.miniserver.mapper.VirtualGroupRelationMapper;
import com.mini10.miniserver.model.GroupRelation;
import com.mini10.miniserver.model.VirtualGroupInfo;
import com.mini10.miniserver.model.VirtualGroupRelation;
import com.mini10.miniserver.service.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

/**
 * @Author ChenShaoJie
 * @Date 2019-07-26 17:20
 */
@Service
public class GroupServiceImpl implements GroupService {

    private static Logger log = LoggerFactory.getLogger(GroupServiceImpl.class);

    @Autowired
    private GroupRelationMapper groupRelationMapper;

    @Autowired
    private VirtualGroupInfoMapper virtualGroupInfoMapper;

    @Autowired
    private VirtualGroupRelationMapper virtualGroupRelationMapper;

    @Autowired
    private RequestUtil requestUtil;



    @Override
    public boolean addGroupRelationInfo(GroupRelation groupRelation) {
        if (groupRelation == null) {
            return false;
        }
        return groupRelationMapper.addGroupRelationInfo(groupRelation) > 0;
    }

    @Override
    public boolean addVirtualGroupRelationInfo(VirtualGroupRelation virtualGroupRelation) {
        return virtualGroupRelationMapper.addVirtualGroupRelationInfo(virtualGroupRelation) > 0;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public boolean addVirtualGroupInfo(String openId, VirtualGroupInfo virtualGroupInfo) {
        if (virtualGroupInfoMapper.addVirtualGroupInfo(virtualGroupInfo) > 0) {
            VirtualGroupRelation virtualGroupRelation = new VirtualGroupRelation();
            virtualGroupRelation.setOpenId(openId);
            virtualGroupRelation.setVirtualGroupId(virtualGroupInfo.getVirtualGroupId());
            addVirtualGroupRelationInfo(virtualGroupRelation);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<String> getGroupIdsByOpenId(String openId) {
        return groupRelationMapper.getGroupIdsByOpenId(openId);
    }

    @Override
    public VirtualGroupInfo getVirtualGroupByPosition(String openId,String longitude,String latitude) {
        VirtualGroupInfo virtualGroupInfo = new VirtualGroupInfo();
        JSONObject positionInfo = requestUtil.getAddress(longitude, latitude);
        if (positionInfo != null && positionInfo.getInteger("status") == 1) {
            JSONObject addressComponent = positionInfo.getJSONObject("regeocode").getJSONObject("addressComponent");
            String district = addressComponent.getString("district");
            String citycode = addressComponent.getString("citycode");
            String adcode = addressComponent.getString("adcode");
            String province = addressComponent.getString("province");
            String city = addressComponent.getString("city");
            virtualGroupInfo.setDistrict(district);
            virtualGroupInfo.setCitycode(citycode);
            virtualGroupInfo.setAdcode(adcode);
            virtualGroupInfo.setProvince(province);
            virtualGroupInfo.setVirtualGroupId(UUID.randomUUID().toString());
            if (StringUtils.isEmpty(city)) {
                city = province;
            }
            virtualGroupInfo.setCity(city);
            virtualGroupInfo.setVirtualGroupName(city + district);
            virtualGroupInfo.setCreatorId(openId);
            List<VirtualGroupInfo> virtualGroupInfoList = virtualGroupInfoMapper.getVirtualGroupListByPosition(virtualGroupInfo);
            if(!CollectionUtils.isEmpty(virtualGroupInfoList)){
                virtualGroupInfo =  virtualGroupInfoList.get(0);
            } else {
                if (!addVirtualGroupInfo(openId, virtualGroupInfo)) {
                    return null;
                }
            }
            return virtualGroupInfo;
        } else {
            return null;
        }
    }

    @Override
    public boolean deleteGroupInfo(String groupId, String openId) {
        GroupRelation groupRelation = new GroupRelation();
        groupRelation.setOpenId(openId);
        groupRelation.setGroupId(groupId);
        return groupRelationMapper.deleteGroupInfo(groupRelation) > 0;
    }


}
