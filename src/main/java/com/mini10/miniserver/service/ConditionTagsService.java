package com.mini10.miniserver.service;

import com.mini10.miniserver.model.ConditionTag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ConditionTagsService {
    boolean setConditionTags(String openId, List<Integer> tagsArray);

    List<ConditionTag> getConditionTags(String openId);

    List<Map<String,String>> getUserTag(String openId);
}
