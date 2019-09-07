package com.mini10.miniserver.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mini10.miniserver.mapper.TagMapper;
import com.mini10.miniserver.model.Tag;
import com.mini10.miniserver.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Override
    public Map<String,Map<String,Integer>> getTags(Integer tagType) {
        List<Tag> tagList = tagMapper.queryTagByTagType(tagType);
        Map<String,Map<String,Integer>> tagMap = new LinkedHashMap<>();
        if(!CollectionUtils.isEmpty(tagList)){
            for(Tag tag : tagList){
                if(tag.getTagClassify() == 0){
                    tagMap.computeIfAbsent("outer", k -> new HashMap<>()).put(tag.getTagName(),tag.getId());
                }
                if(tag.getTagClassify() == 1){
                    tagMap.computeIfAbsent("inner", k -> new HashMap<>()).put(tag.getTagName(),tag.getId());
                }
                if(tag.getTagClassify() == 2){
                    tagMap.computeIfAbsent("interest", k -> new HashMap<>()).put(tag.getTagName(),tag.getId());
                }
                if(tag.getTagClassify() == 3){
                    tagMap.computeIfAbsent("other", k -> new HashMap<>()).put(tag.getTagName(),tag.getId());
                }
            }
        }
        return tagMap;
    }

    @Override
    public void bathAddTags(List<Tag> tagList) {
            tagMapper.bathAddTags(tagList);
    }


    @Override
    public JSONArray getTagsNew(Integer tagType){
        if (tagType == null){
            return null;
        }
        List<Tag> tagList = tagMapper.queryTagByTagType(tagType);
        JSONArray jsonArray = new JSONArray();
        if (!CollectionUtils.isEmpty(tagList)){
            for (Tag tag : tagList){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id",tag.getId());
                jsonObject.put("tagName",tag.getTagName());
                jsonObject.put("tagType",tag.getTagType());
                if (tag.getTagClassify().equals(0)){
                    jsonObject.put("tagClassify","outer");
                }else if(tag.getTagClassify().equals(1)){
                    jsonObject.put("tagClassify","inner");
                }else if (tag.getTagClassify().equals(2)){
                    jsonObject.put("tagClassify","interest");
                }else if (tag.getTagClassify().equals(3)){
                    jsonObject.put("tagClassify","other");
                }
                jsonArray.add(jsonObject);
            }
            return jsonArray;
        }else {
            return null;
        }

    }
}
