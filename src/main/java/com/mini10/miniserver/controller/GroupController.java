package com.mini10.miniserver.controller;

import com.alibaba.fastjson.JSONObject;
import com.mini10.miniserver.common.Constant;
import com.mini10.miniserver.common.Result;
import com.mini10.miniserver.common.utils.AjaxObject;
import com.mini10.miniserver.common.utils.RequestUtil;
import com.mini10.miniserver.common.utils.WxCoreUtil;
import com.mini10.miniserver.model.GroupRelation;
import com.mini10.miniserver.model.VirtualGroupInfo;
import com.mini10.miniserver.service.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * @author Dongxiyan
 * @date 2019-07-26 17:07
 */
@RestController
@RequestMapping("/api/group")
public class GroupController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private GroupService groupService;

    @Autowired
    private RequestUtil requestUtil;

    /**
     * 添加群组信息
     *
     * @param jsonObject
     * @return
     */
    @PostMapping("/addGroupInfo")
    public Result addGroupInfo(@RequestBody JSONObject jsonObject) {
        String encryptedData = jsonObject.getString("encryptedData");
        String iv = jsonObject.getString("iv");
        String openId = jsonObject.getString("openId");
        JSONObject res = null;
        String sessionKey = jsonObject.getString("sessionKey");
        logger.info("addGroupInfo sessionKey:" + sessionKey);
        if (StringUtils.isEmpty(encryptedData)) {
            return AjaxObject.error("encryptedData为空");
        }
        if (StringUtils.isEmpty(iv)) {
            return AjaxObject.error("iv为空");
        }
        if (StringUtils.isEmpty(sessionKey)) {
            return AjaxObject.error("sessionKey为空");
        }
        try {
            res = WxCoreUtil.decrypt(encryptedData, iv, sessionKey);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("解密出错");
            return AjaxObject.error("解密出错", e);
        }
        if (res != null) {
            String openGroupId = res.getString("openGId");
            List<String> groupIds = groupService.getGroupIdsByOpenId(openId);
            if (groupIds != null && groupIds.size() > 0) {
                for (String gId : groupIds) {
                    if (gId.equals(openGroupId)) {
                        return AjaxObject.success("群组信息已存在", null);
                    }
                }
            }
            GroupRelation groupRelation = new GroupRelation();
            groupRelation.setGroupId(openGroupId);
            groupRelation.setOpenId(openId);
            if (groupService.addGroupRelationInfo(groupRelation)) {
                return AjaxObject.success("添加群组信息成功", null);
            } else {
                return AjaxObject.error("添加群组信息失败");
            }
        } else {
            return AjaxObject.error("解密的群组信息为空");
        }
    }

    @PostMapping("/deleteGroupInfo")
    public Result deleteGroupInfo(@RequestBody JSONObject jsonObject) {
        String groupId = jsonObject.getString("groupId");
        String openId = jsonObject.getString("openId");
        if (groupService.deleteGroupInfo(groupId, openId)) {
            return AjaxObject.success("删除群组信息成功", null);
        } else {
            return AjaxObject.error("删除群组信息失败");
        }
    }


    /**
     * 添加虚拟群组信息
     *
     * @param jsonObject
     * @return Result
     */
    @PostMapping("/addVirtualGroupInfo")
    public Result addVirtualGroupInfo(@RequestBody JSONObject jsonObject) {
        String openId = jsonObject.getString("openId");
        String longitude = jsonObject.getString("longitude");
        String latitude = jsonObject.getString("latitude");
        if (StringUtils.isEmpty(longitude) || StringUtils.isEmpty(latitude)) {
            return AjaxObject.error("经纬度为空");
        }
        VirtualGroupInfo virtualGroupInfo = groupService.getVirtualGroupByPosition(openId,longitude,latitude);
        if(virtualGroupInfo != null){
            JSONObject res = new JSONObject();
            res.put("virtualGroupInfo", virtualGroupInfo);
            return AjaxObject.success("获取虚拟群组信息成功", res);
        } else {
            return AjaxObject.error("获取群组信息失败");
        }
    }


}
