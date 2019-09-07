package com.mini10.miniserver.service;

import com.alibaba.fastjson.JSONArray;
import com.mini10.miniserver.model.Tag;

import java.util.List;
import java.util.Map;

public interface TagService {

    Map<String,Map<String,Integer>> getTags(Integer gender);

    JSONArray getTagsNew(Integer tagType);
    void bathAddTags(List<Tag> tagList);
}
