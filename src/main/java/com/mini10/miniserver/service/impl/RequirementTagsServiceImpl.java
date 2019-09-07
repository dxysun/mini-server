package com.mini10.miniserver.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mini10.miniserver.mapper.RequirementTagMapper;
import com.mini10.miniserver.mapper.SpecialRequirementMapper;
import com.mini10.miniserver.mapper.TagMapper;
import com.mini10.miniserver.model.RequirementTag;
import com.mini10.miniserver.model.SpecialRequirement;
import com.mini10.miniserver.model.Tag;
import com.mini10.miniserver.service.RequirementTagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RequirementTagsServiceImpl implements RequirementTagsService {
    @Autowired
    private RequirementTagMapper requirementTagMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private SpecialRequirementMapper specialRequirementMapper;
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void setRequirementTags(String openId, List<Integer> tags) {
        RequirementTag requirementTag = new RequirementTag();
        requirementTag.setOpenId(openId);
        requirementTag.setNecessary(0);
        requirementTagMapper.deleteRequirementTag(requirementTag);
        if(CollectionUtils.isEmpty(tags)){
            return;
        }
        List<RequirementTag> requirementTags = new ArrayList<>();
        for (Integer tagId : tags) {
            if(tagId == null){
                continue;
            }
            String tagName = tagMapper.queryTagNameById(tagId);
            requirementTag = new RequirementTag();
            requirementTag.setOpenId(openId);
            requirementTag.setRequireTagId(tagId);
            requirementTag.setRequireTagName(tagName);
            requirementTags.add(requirementTag);
        }
        if (!StringUtils.isEmpty(openId)) {
            requirementTagMapper.setRequirementTagMapper(requirementTags);
        }
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void setNecessaryTags(String openId, List<Integer> tags){
        RequirementTag requirementTag = new RequirementTag();
        requirementTag.setOpenId(openId);
        requirementTag.setNecessary(1);
        requirementTagMapper.deleteRequirementTag(requirementTag);
        List<RequirementTag> requirementTags = new ArrayList<>();
        for (Integer tagId : tags) {
            if(tagId == null){
                continue;
            }
            String tagName = tagMapper.queryTagNameById(tagId);
            requirementTag = new RequirementTag();
            requirementTag.setOpenId(openId);
            requirementTag.setRequireTagId(tagId);
            requirementTag.setRequireTagName(tagName);
            requirementTags.add(requirementTag);
        }
        if (!StringUtils.isEmpty(openId)) {
            requirementTagMapper.setNecessaryTagMapper(requirementTags);
        }
    }

    @Override
    public List<RequirementTag> getAllRequirementTagsByOpenId(String openId) {
        if(!StringUtils.isEmpty(openId)){
            return requirementTagMapper.getAllRequireTagsByOpenId(openId);
        }
        return null;
    }

    /**
     * 获取用户所有的非必需要求标签
     * @param openId
     * @return
     */
    @Override
    public List<RequirementTag> getRequirementTagsByOpenId(String openId){
        if(!StringUtils.isEmpty(openId)){
            return requirementTagMapper.getRequirementTagsByOpenId(openId);
        }
        return null;
    }

    /**
     * 获取用户所有的必需要求标签
     * @param openId
     * @return
     */
    @Override
    public List<RequirementTag> getNecessaryTagsByOpenId(String openId){
        if(!StringUtils.isEmpty(openId)){
            return requirementTagMapper.getNecessaryTagsByOpenId(openId);
        }
        return null;
    }

    /**
     * 得到用户对对方的要求
     * @param openId
     * @return
     */
    @Override
    public JSONObject getUserTag(String openId) {
        JSONObject jsonObject = new JSONObject();
        List<RequirementTag> necessaryTagList = requirementTagMapper.getNecessaryTagsByOpenId(openId);
        List<RequirementTag> extraTagList = requirementTagMapper.getRequirementTagsByOpenId(openId);
        List<Map<String, String>> necessaryList = new ArrayList<>();
        for (RequirementTag necessaryTag: necessaryTagList) {
            Map<String, String> map = new HashMap<>();
            Integer tagClassify = tagMapper.queryTagClassifyById(necessaryTag.getRequireTagId());
            if(tagClassify == null){
                continue;
            }
            if(tagClassify == 0){
                map.put("type","outer");
            }
            if(tagClassify == 1){
                map.put("type","inner");
            }
            if(tagClassify == 2){
                map.put("type","interest");
            }
            if(tagClassify == 3){
                map.put("type","other");
            }
            map.put("tag",necessaryTag.getRequireTagName());
            map.put("number","" + necessaryTag.getRequireTagId());
            necessaryList.add(map);
        }

        List<Map<String, String>> extraList = new ArrayList<>();
        for (RequirementTag extraTag: extraTagList) {
            Map<String, String> map = new HashMap<>();
            Integer tagClassify = tagMapper.queryTagClassifyById(extraTag.getRequireTagId());
               if(tagClassify == null){
                continue;
            }
            if(tagClassify == 0){
                map.put("type","outer");
            }
            if(tagClassify == 1){
                map.put("type","inner");
            }
            if(tagClassify == 2){
                map.put("type","interest");
            }
            if(tagClassify == 3){
                map.put("type","other");
            }
            map.put("tag",extraTag.getRequireTagName());
            map.put("number","" + extraTag.getRequireTagId());
            extraList.add(map);
        }
        jsonObject.put("necessary",necessaryList);
        jsonObject.put("extra",extraList);
        return jsonObject;
    }

    /**
     * 添加用户的特殊数值需求
     * @param specialRequirement
     */
    @Override
    public void addSpecialRequirement(SpecialRequirement specialRequirement){
        if (specialRequirement != null && !StringUtils.isEmpty(specialRequirement.getOpenId())){
            try {
                specialRequirementMapper.addSpecialRequirement(specialRequirement);
            }catch (Exception e){
                throw e;
            }
        }
    }

    /**
     * 更新用户的特殊数值需求
     * @param specialRequirement
     */
    @Override
    public void updateSpecialRequirement(SpecialRequirement specialRequirement){
        if (specialRequirement != null && !StringUtils.isEmpty(specialRequirement.getOpenId())){
            try {
                specialRequirementMapper.updateSpecialRequirementByOpenId(specialRequirement);
            }catch (Exception e){
                throw e;
            }
        }
    }
}
