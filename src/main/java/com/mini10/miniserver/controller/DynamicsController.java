package com.mini10.miniserver.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.mini10.miniserver.common.Constant;
import com.mini10.miniserver.common.Result;
import com.mini10.miniserver.common.utils.AjaxObject;
import com.mini10.miniserver.common.utils.EmotionUtil;
import com.mini10.miniserver.common.utils.ListUtils;
import com.mini10.miniserver.common.utils.netease.NetEaseApi;
import com.mini10.miniserver.model.Dynamics;
import com.mini10.miniserver.model.param.MatchingResult;
import com.mini10.miniserver.service.DynamicsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author corn1ng
 */
@RestController
@RequestMapping("/api/dynamics")
public class DynamicsController {

    @Autowired
    private DynamicsService dynamicsService;

    @Autowired
    private EmotionUtil emotionUtil;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @PostMapping(value = "/addDiary")
    public Result addDiary(@RequestBody JSONObject jsonObject) {
        String str1 = jsonObject.toJSONString();
        Dynamics dynamics = JSONObject.parseObject(str1, Dynamics.class);

        String diary = dynamics.getDiary();
        JSONObject diaryJson = NetEaseApi.checkTxtApi("ebfcad1c-dba1-490c-b4de-e784c2691768", diary);
        if (diaryJson != null && Integer.parseInt(diaryJson.getJSONObject("result").getString("action")) == 2) {
            return AjaxObject.error(Constant.ResultCode.UQUALIFIED_CODE, "请文明用语");
        }
        Integer emotionRate = emotionUtil.getEmotion(diary);
        if (emotionRate != 1 && emotionRate != -1 && emotionRate != 0) {
            dynamics.setEmotionRate(EmotionUtil.DEFAULT_EMOTION);
        }
        dynamics.setEmotionRate(emotionRate);

        Integer num = dynamicsService.insertDynamics(dynamics);
        if (num == 1) {
            // 为安全将openId设置为null返回到前端。
            JSONObject jsonObject1 = dynamicsService.getNewestDynamics(dynamics.getOpenId());
            return AjaxObject.success("新建日记成功", jsonObject1);
        } else {
            return AjaxObject.error("新建日记错误");
        }
    }


    /**
     * @param openId      用户的唯一ID
     * @param nowPage     当前前端需要第几页的数据
     * @param itemPerPage 每一页有多少条数据
     * @return 返回的JSON数据
     */
    @GetMapping(value = "/getAllDiary")
    public Result getAllDiary(@RequestParam("openId") String openId,
                              @RequestParam(defaultValue = "2") Integer itemPerPage,
                              @RequestParam(defaultValue = "3") Integer nowPage) {
        JSONObject diaries = dynamicsService.selectAllDynamics(nowPage, itemPerPage, openId);
        if (diaries != null) {
            return AjaxObject.success("用户日记分页查找成功", diaries);
        }
        return AjaxObject.error("用户日记分页查找失败");
    }

    @PostMapping("/deleteDiary")
    public Result deleteDiary(@RequestBody JSONObject jsonObject) {
        String id = jsonObject.getString("id");

        if (dynamicsService.deleteDiary(Integer.valueOf(id))) {
            return AjaxObject.success("删除日记成功", null);
        }
        return AjaxObject.error("删除日记失败");
    }

    @GetMapping(value = "/getNewest")
    public Result getNewest(@RequestParam("openId") String openId) {
        JSONObject data = dynamicsService.getNewestDynamics(openId);
        if (data != null) {
            return AjaxObject.success("获取最新日记成功", data);
        } else {
            return AjaxObject.error("获取最新日记失败");
        }
    }

    @GetMapping(value = "/diaryMatch")
    public Result diaryMatch(@RequestParam("openId") String openId) {
        JSONArray data = dynamicsService.diaryMatch(openId);
        if (data != null) {
            return AjaxObject.success("日记匹配成功", data);
        } else {
            return AjaxObject.error("日记匹配失败");
        }
    }

    @PostMapping("/getMatchedUserDiary")
    public Result getMatchedUserDiary(@RequestBody JSONObject jsonObject) {
        String targetOpenId = jsonObject.getString("targetOpenId");
        if (StringUtils.isEmpty(targetOpenId)) {
            return AjaxObject.error("被匹配用户open id为null");
        }
        Integer itemPerPage = jsonObject.getInteger("itemPerPage");
        Integer nowPage = jsonObject.getInteger("nowPage");
        if (itemPerPage == null) {
            itemPerPage = 10;
        }
        if (nowPage == null) {
            nowPage = 1;
        }
        JSONObject data = dynamicsService.getMatchedUserDiary(targetOpenId, nowPage, itemPerPage);
        return AjaxObject.success("获取匹配用户日记成功", data);
    }

    /**
     * 用户个人信息排行榜
     * 返回前20个数据
     *
     * @return
     */
    @GetMapping(value = "/matchUser")
    public Result<Object> matchUser(@RequestParam("openId") String openId) {
        logger.info("openId= " + openId);
        if (StringUtils.isEmpty(openId)) {
            return AjaxObject.error("无法获取到用户信息");
        }
        Set<MatchingResult> matchingResults;
        List<MatchingResult> tmpResults;
        List<MatchingResult> results;
        try {
            matchingResults = dynamicsService.getMatchingUser(openId);
            tmpResults = new ArrayList<>(matchingResults);
            if (tmpResults.size() > 20) {
                results = ListUtils.getSubList(tmpResults, 0, 20);
            } else {
                results = tmpResults;
            }
        } catch (Exception e) {
            return AjaxObject.error(e);
        }
        return AjaxObject.success("获取个人匹配排行榜成功", results);
    }

    @PostMapping(value = "/addDynamicsBlacklist")
    public Result<Object> addDynamicBlacklist(@RequestBody JSONObject jsonObject) {
        String openId = jsonObject.getString("openId");
        Integer dynamicId = jsonObject.getInteger("dynamicsId");
        if(dynamicId == null){
            return AjaxObject.error("日记id为空");
        }
        Boolean res = dynamicsService.addDynamicBlackList(openId, dynamicId);
        if (res) {
            return AjaxObject.success("添加黑名单成功", null);
        } else {
            return AjaxObject.error("添加黑名单失败");
        }
    }


}
