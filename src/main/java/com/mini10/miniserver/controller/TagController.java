package com.mini10.miniserver.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mini10.miniserver.common.Result;
import com.mini10.miniserver.common.utils.AjaxObject;
import com.mini10.miniserver.model.SpecialRequirement;
import com.mini10.miniserver.model.Tag;
import com.mini10.miniserver.service.RequirementTagsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import com.mini10.miniserver.service.ConditionTagsService;
import com.mini10.miniserver.service.TagService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/tag")
public class TagController {

    private static Logger log = LoggerFactory.getLogger(TagController.class);
    @Autowired
    private RequirementTagsService requirementTagsService;

    @Autowired
    private TagService tagService;

    @Autowired
    private ConditionTagsService conditionTagsService;

    /**
     * 设置用户的非必需标签
     * @param jsonObject
     * @return
     */
    @RequestMapping(value = "/setRequirementTags", method = RequestMethod.POST)
    public Result setRequirementTags(@RequestBody JSONObject jsonObject) {
        String openId = jsonObject.getString("openId");
        JSONArray array = jsonObject.getJSONArray("tags");
        List<Integer> list = array.toJavaList(Integer.class);
        if (StringUtils.isEmpty(openId)) {
            return AjaxObject.error("获取opedId失败");
        }
        if (CollectionUtils.isEmpty(list)) {
            return AjaxObject.error("需求标签为空");
        }

        try {
            requirementTagsService.setRequirementTags(openId, list);
        } catch (Exception e) {
            log.error(AjaxObject.getStackTrace(e));
            return AjaxObject.error(e);
        }

        return AjaxObject.success("设置需求成功");
    }

    /**
     * 设置用户的非必需标签
     * @param jsonObject
     * @returns
     */
    @RequestMapping(value = "/setNecessaryTags", method = RequestMethod.POST)
    public Result setNecessaryTags(@RequestBody JSONObject jsonObject) {
        String openId = jsonObject.getString("openId");
        JSONArray array = jsonObject.getJSONArray("tags");
        List<Integer> list = array.toJavaList(Integer.class);
        if (StringUtils.isEmpty(openId)) {
            return AjaxObject.error("获取opedId失败");
        }
        if (CollectionUtils.isEmpty(list)) {
            return AjaxObject.error("需求标签为空");
        }

        try {
            requirementTagsService.setNecessaryTags(openId, list);
        } catch (Exception e) {
            log.error(AjaxObject.getStackTrace(e));
            return AjaxObject.error(e);
        }

        return AjaxObject.success("设置需求成功");
    }

    /**
     * 根据标签类型获取标签集 0：男->女 1：男 2：女->男 3：女
     * @param tagType
     * @return
     */
    @GetMapping("/getTags")
    public Result getTags(@RequestParam("tagType") Integer tagType) {
        if (StringUtils.isEmpty(tagType)) {
            return AjaxObject.error("标签类型为空");
        }
        try {
            return AjaxObject.success("获取标签集成功",tagService.getTags(tagType));
        }
        catch (Exception e){
            log.error(AjaxObject.getStackTrace(e));
            return AjaxObject.error(e);
        }
    }

    /**
     * 根据标签类型获取标签集 0：男->女 1：男 2：女->男 3：女
     * @param tagType
     * @return
     */
    @GetMapping("/getTagsNew")
    public Result getTagsNew(@RequestParam("tagType") Integer tagType){
        if (StringUtils.isEmpty(tagType)) {
            return AjaxObject.error("标签类型为空");
        }
        try {
            return AjaxObject.success("获取标签集成功",tagService.getTagsNew(tagType));
        }
        catch (Exception e){
            log.error(AjaxObject.getStackTrace(e));
            return AjaxObject.error(e);
        }
    }

    /**
     * 该方法用于设置用户自身必须标签@created by Xiang Jiangnan
     * @param jsonObject 被选择的标签字符串
     * @return 返回设置自身标签状态
     */
    @PostMapping("/setConditionTags")
    public Result setConditionTags(@RequestBody JSONObject jsonObject){
        String openId = jsonObject.getString("openId");
        JSONArray tagsArray = jsonObject.getJSONArray("tags");
        if(!CollectionUtils.isEmpty(tagsArray)){
            List<Integer> tagsList = tagsArray.toJavaList(Integer.class);
            if(conditionTagsService.setConditionTags(openId, tagsList)){
                return AjaxObject.success("保存自身标签成功");
            }else{
                return AjaxObject.error("保存自身标签失败");
            }
        } else {
            return AjaxObject.error("tags参数为空");
        }
    }

    @PostMapping("/addSpecialRequirement")
    public Result addSpecialRequirement(@RequestBody SpecialRequirement specialRequirement){
        if (specialRequirement != null && !StringUtils.isEmpty(specialRequirement.getOpenId())) {
            try {
                requirementTagsService.addSpecialRequirement(specialRequirement);
                return AjaxObject.success("添加特殊需求数值信息成功");
            }catch (Exception e){
                return AjaxObject.error(e);
            }
        }else {
            return AjaxObject.error("特殊数值需求为空");
        }
    }

    @PostMapping("/updateSpecialRequirement")
    public Result updateSpecialRequirement(@RequestBody SpecialRequirement specialRequirement){
        if (specialRequirement != null && !StringUtils.isEmpty(specialRequirement.getOpenId())) {
            try {
                requirementTagsService.updateSpecialRequirement(specialRequirement);
                return AjaxObject.success("更新特殊需求数值信息成功");
            }catch (Exception e){
                return AjaxObject.error(e);
            }
        }else {
            return AjaxObject.error("特殊数值需求为空");

        }
    }

    /**
     * 设置对方的标签，包括必须的和非必须的
     * @param jsonObject
     * @return
     */
    @PostMapping(value = "/setOtherTags")
    public Result setOtherTags(@RequestBody JSONObject jsonObject) {
        String openId = jsonObject.getString("openId");
        if (StringUtils.isEmpty(openId)) {
            return AjaxObject.error("获取opedId失败");
        }
        JSONObject tagsObject = jsonObject.getJSONObject("tags");
        JSONArray necessaryArray = tagsObject.getJSONArray("necessary");
        JSONArray extraArray = tagsObject.getJSONArray("extra");
        List<Integer> necessaryList = null;
        List<Integer> extraList = null;
        if (!CollectionUtils.isEmpty(necessaryArray)){
            necessaryList = necessaryArray.toJavaList(Integer.class);
        }else {
            return AjaxObject.error("设置需求失败，必需项不能为空");
        }
        if (extraArray != null){
            extraList = extraArray.toJavaList(Integer.class);
        }
        try {
            if(!CollectionUtils.isEmpty(necessaryArray)){
                requirementTagsService.setNecessaryTags(openId, necessaryList);
            }
            if(extraList != null){
                requirementTagsService.setRequirementTags(openId, extraList);
            }
        } catch (Exception e) {
            log.error(AjaxObject.getStackTrace(e));
            return AjaxObject.error(e);
        }
        return AjaxObject.success("设置需求成功");
    }
}
