package com.mini10.miniserver.service;

import com.alibaba.fastjson.JSONObject;
import com.mini10.miniserver.model.RequirementTag;
import com.mini10.miniserver.model.SpecialRequirement;

import java.util.List;
import java.util.Map;

public interface RequirementTagsService {
    void setRequirementTags(String openId, List<Integer> tags);

    void setNecessaryTags(String openId, List<Integer> tags);

    /**
     * 获取用户对他人的索要需求标签（包括必要和非必要）
     * @param openId
     * @return
     */
    List<RequirementTag> getAllRequirementTagsByOpenId(String openId);

    JSONObject getUserTag(String openId);

    void addSpecialRequirement(SpecialRequirement specialRequirement);

    void updateSpecialRequirement(SpecialRequirement specialRequirement);

    List<RequirementTag> getNecessaryTagsByOpenId(String openId);

    List<RequirementTag> getRequirementTagsByOpenId(String openId);
}
