package com.mini10.miniserver.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mini10.miniserver.common.Constant;
import com.mini10.miniserver.common.Result;
import com.mini10.miniserver.common.utils.AjaxObject;
import com.mini10.miniserver.common.utils.RedisUtil;
import com.mini10.miniserver.model.User;
import com.mini10.miniserver.model.param.MatchGroupResult;
import com.mini10.miniserver.model.param.MatchingResult;
import com.mini10.miniserver.service.MatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @Author ChenShaoJie
 * @Date 2019-07-19 16:12
 */
@RestController
@RequestMapping("/api/match")
public class MatchController {

    private static Logger log = LoggerFactory.getLogger(MatchController.class);

    @Autowired
    private MatchService matchService;

    @Autowired
    private RedisUtil redisUtil;

    @GetMapping("/getMatchGroups")
    public Result getMatchGroups(@RequestParam("openId") String openId, @RequestParam("isRefresh") Integer isRefresh) {
        if (StringUtils.isEmpty(openId)) {
            return AjaxObject.error("无法获取用户信息");
        }
        if (isRefresh == null) {
            return AjaxObject.error("请求标识出错");
        }
        List<MatchGroupResult> result = null;
        //如果传入刷新标识，则重新对用户的匹配进行计算
        if (isRefresh.equals(1)) {
            //获取开始时间
            long startTime = System.currentTimeMillis();
            result = matchService.getMatchResult(openId);
            //获取结束时间
            long endTime = System.currentTimeMillis();
            log.info("本次匹配算法运行时间： " + (endTime - startTime) + "ms");
            if (!CollectionUtils.isEmpty(result)) {
                log.info("本次重新计算获取群匹配结果数据不为空");
                String jsonString = JSON.toJSONString(result);
                redisUtil.hset(Constant.REDIS_GROUP_RESULT, openId, jsonString);
            }
        } else if (isRefresh.equals(0)) {
            //如果不刷新，则从redis中取出上次的存储结果
            String resultJsonList = (String) redisUtil.hget(Constant.REDIS_GROUP_RESULT, openId);
            if (!StringUtils.isEmpty(resultJsonList)) {
                log.info("本次从Redis缓存中获取群匹配结果数据不为空");
                result = JSON.parseArray(resultJsonList, MatchGroupResult.class);
            }
        }
        if (CollectionUtils.isEmpty(result)) {
            return AjaxObject.success("当前没有群匹配信息", null);
        }
        return AjaxObject.success("获取用户群匹配信息成功", result);
    }

    @GetMapping("/getGroupMatchResult")
    public Result getGroupMatchResult(@RequestParam("groupId") String groupId, @RequestParam("openId") String openId) {
        if (StringUtils.isEmpty(openId)) {
            return AjaxObject.error("无法获取用户信息");
        }
        if (StringUtils.isEmpty(groupId)) {
            return AjaxObject.error("无法获取到群信息");
        }
        String mapJsonString = (String) redisUtil.hget(Constant.REDIS_DETAIL_GROUP_RESULT, openId);
        JSONObject matchingResultMap = JSON.parseObject(mapJsonString);
        if (matchingResultMap == null || matchingResultMap.get(groupId) == null){
            return AjaxObject.error("在该群的匹配信息为空");
        }
        JSONArray matchingResultJsons = (JSONArray) matchingResultMap.get(groupId);
        if (CollectionUtils.isEmpty(matchingResultJsons)) {
            return AjaxObject.success("当前没有匹配结果", null);
        }
        List<MatchingResult> matchingResults = JSONObject.parseArray(matchingResultJsons.toJSONString(), MatchingResult.class);
        Collections.sort(matchingResults, new Comparator<MatchingResult>() {
            @Override
            public int compare(MatchingResult o1, MatchingResult o2) {
                double score1 = o1.getMatchScore();
                double score2 = o2.getMatchScore();
                if (score1 > score2) {
                    return -1;
                } else if (score1 == score2) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });

        return AjaxObject.success("获取详细匹配信息成功", matchingResults);


    }

    @GetMapping("/getMatchGroupsForVirtual")
    public Result getMatchGroupsForVirtual(@RequestParam("openId") String openId,
                                           @RequestParam("isRefresh") Integer isRefresh,
                                           @RequestParam("longitude") String longitude,
                                           @RequestParam("latitude") String latitude) {
        if (StringUtils.isEmpty(openId)) {
            return AjaxObject.error("无法获取用户信息");
        }
        if (isRefresh == null) {
            return AjaxObject.error("请求标识出错");
        }
        if (StringUtils.isEmpty(longitude) || StringUtils.isEmpty(latitude)) {
            return AjaxObject.error("经纬度信息错误");
        }
        List<MatchGroupResult> result = null;
        //如果传入刷新标识，则重新对用户的匹配进行计算
        if (isRefresh.equals(1)) {
            //获取开始时间
            long startTime = System.currentTimeMillis();
            result = matchService.getVirtualMatchResult(openId, longitude, latitude);
            //获取结束时间
            long endTime = System.currentTimeMillis();
            log.info("本次匹配算法运行时间： " + (endTime - startTime) + "ms");
            if (!CollectionUtils.isEmpty(result)) {
                log.info("本次重新计算获取虚拟群匹配结果数据不为空");
                String jsonString = JSON.toJSONString(result);
                redisUtil.hset(Constant.REDIS_VIRTUAL_GROUP_RESULT, openId, jsonString);
            }
        } else if (isRefresh.equals(0)) {
            //如果不刷新，则从redis中取出上次的存储结果
            String resultJsonList = (String) redisUtil.hget(Constant.REDIS_VIRTUAL_GROUP_RESULT, openId);
            if (!StringUtils.isEmpty(resultJsonList)) {
                log.info("本次从Redis缓存中获取虚拟群匹配结果数据不为空");
                result = JSON.parseArray(resultJsonList, MatchGroupResult.class);
            }
        }
        if (CollectionUtils.isEmpty(result)) {
            return AjaxObject.success("当前没有虚拟群匹配信息", null);
        }
        return AjaxObject.success("获取用户虚拟群匹配信息成功", result);
    }

    @GetMapping("/getGroupMatchResultForVirtual")
    public Result getGroupMatchResultForVirtual(@RequestParam("groupId") String groupId, @RequestParam("openId") String openId) {
        if (StringUtils.isEmpty(openId)) {
            AjaxObject.error("无法获取用户信息");
        }
        if (StringUtils.isEmpty(groupId)) {
            AjaxObject.error("无法获取到虚拟群信息");
        }
        String mapJsonString = (String) redisUtil.hget(Constant.REDIS_DETAIL_VIRTUAL_GROUP_RESULT, openId);
        JSONObject matchingResultMap = JSON.parseObject(mapJsonString);
        JSONArray matchingResultJsons = (JSONArray) matchingResultMap.get(groupId);
        if (CollectionUtils.isEmpty(matchingResultJsons)) {
            return AjaxObject.success("当前没有匹配结果", null);
        }
        List<MatchingResult> matchingResults = JSONObject.parseArray(matchingResultJsons.toJSONString(), MatchingResult.class);
        Collections.sort(matchingResults, new Comparator<MatchingResult>() {
            @Override
            public int compare(MatchingResult o1, MatchingResult o2) {
                double score1 = o1.getMatchScore();
                double score2 = o2.getMatchScore();
                if (score1 > score2) {
                    return -1;
                } else if (score1 == score2) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });
        return AjaxObject.success("获取详细匹配信息成功", matchingResults);

    }


    /**
     * 匹配两个人的信息匹配度
     *
     * @param openId
     * @param targetId
     * @return
     */
    @RequestMapping("/personMatch")
    public Result matchUser(@RequestParam("openId") String openId, @RequestParam("targetId") String targetId) {
        if (StringUtils.isEmpty(openId)) {
            AjaxObject.error("无法获取用户信息");
        }
        MatchingResult matchingResult;
        User self = new User();
        self.setOpenId(openId);
        User targetUser = new User();
        targetUser.setOpenId(targetId);
        try {
            matchingResult = matchService.matchUser(self, targetUser);
        } catch (Exception e) {
            return AjaxObject.error(e);
        }
        return AjaxObject.success("获取详细匹配信息成功", matchingResult);
    }

}
