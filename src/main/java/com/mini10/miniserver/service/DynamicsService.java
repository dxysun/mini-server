package com.mini10.miniserver.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mini10.miniserver.model.Dynamics;
import com.mini10.miniserver.model.param.MatchingResult;
import java.util.Set;

public interface DynamicsService {

    JSONArray diaryMatch(String openId);

    Integer insertDynamics(Dynamics dynamics);

    JSONObject selectAllDynamics(int pageNo, int pageSize, String openId);

    boolean deleteDiary(Integer id);

    JSONObject getNewestDynamics(String openId);

    Set<MatchingResult> getMatchingUser(String openId);

    JSONObject getMatchedUserDiary(String targetOpenId, Integer nowPage, Integer itemPerPage);

    boolean addDynamicBlackList(String openId,Integer dynamicId);


}
