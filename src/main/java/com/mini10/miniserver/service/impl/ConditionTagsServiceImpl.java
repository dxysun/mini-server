package com.mini10.miniserver.service.impl;

import com.mini10.miniserver.mapper.ConditionTagMapper;
import com.mini10.miniserver.mapper.TagMapper;
import com.mini10.miniserver.model.ConditionTag;
import com.mini10.miniserver.service.ConditionTagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConditionTagsServiceImpl implements ConditionTagsService {

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private ConditionTagMapper conditionTagMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public boolean setConditionTags(String openId, List<Integer> tagsArray){
        Integer res = conditionTagMapper.deleteConditionTagsByOpenId(openId);
        System.out.println("delete res :" + res);
        ArrayList<ConditionTag> conditionTagArray = new ArrayList<>();
        for (Integer id: tagsArray) {
           String tagName =  tagMapper.queryTagNameById(id);
           if(!StringUtils.isEmpty(tagName)){
               ConditionTag conditionTag = new ConditionTag();
               conditionTag.setOpenId(openId);
               conditionTag.setConditionTagName(tagName);
               conditionTag.setConditionTagId(id);
               conditionTagArray.add(conditionTag);
           }
        }
        if(!StringUtils.isEmpty(openId)){
            conditionTagMapper.addConditionTags(conditionTagArray);
        }else{
            return false;
        }
        return true;
    }

    @Override
    public List<ConditionTag> getConditionTags(String openId) {
       if(!StringUtils.isEmpty(openId)){
           return conditionTagMapper.getConditionTagsByOpenId(openId);
       }
       return null;
    }

    @Override
    public List<Map<String, String>> getUserTag(String openId) {
        List<ConditionTag> conditionTagList = conditionTagMapper.getConditionTagsByOpenId(openId);
        List<Map<String, String>> list = new ArrayList<>();
        for (ConditionTag conditionTag: conditionTagList) {
            Map<String, String> map = new HashMap<>();
            Integer tagClassify = tagMapper.queryTagClassifyById(conditionTag.getConditionTagId());
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
            map.put("tag",conditionTag.getConditionTagName());
            map.put("number","" + conditionTag.getConditionTagId());
            list.add(map);
        }
        return list;
    }
}
