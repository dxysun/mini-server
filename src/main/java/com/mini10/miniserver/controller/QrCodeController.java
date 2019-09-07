package com.mini10.miniserver.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mini10.miniserver.common.Result;
import com.mini10.miniserver.common.utils.AjaxObject;
import com.mini10.miniserver.common.utils.RedisUtil;
import com.mini10.miniserver.model.ConditionTag;
import com.mini10.miniserver.model.RequirementTag;
import com.mini10.miniserver.model.User;
import com.mini10.miniserver.model.param.MatchingResult;
import com.mini10.miniserver.service.ConditionTagsService;
import com.mini10.miniserver.service.MatchService;
import com.mini10.miniserver.service.RequirementTagsService;
import com.mini10.miniserver.service.UserService;
import javafx.beans.binding.ObjectExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xiabin
 * @date 2019/7/22
 */
@RestController
@RequestMapping("/api/qrc")
public class QrCodeController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    // 需要收集的总信息页数
//    private static final int STATE = 4;
    @Resource
    private UserService userService;
    @Resource
    private RequirementTagsService requirementTagsService;
    @Resource
    private ConditionTagsService conditionTagsService;
    @Resource
    private MatchService matchService;



    /**
     * 扫描个人二维码计算两个人匹配度
     * @author xiabin，cuiyang
     * @return
     */
    @RequestMapping("/shareQrc")
    @ResponseBody
    public Result matchUser(@RequestBody JSONObject jsonObject) {
        logger.info("打印传过来的body=" + jsonObject);
        if (jsonObject == null){
            return AjaxObject.error("传入的数据为空");
        }
        String openId = (String)jsonObject.get("openId");
        String targetId = (String)jsonObject.get("targetId");
        if (StringUtils.isEmpty(targetId)) {
            AjaxObject.error("targetId为空");
        }
        if (StringUtils.isEmpty(openId)) {
            AjaxObject.error("openId为空，无法获取用户信息");
        }
        MatchingResult matchingResult;
        User self = userService.getUserByOpenId(openId);
        User targetUser = userService.getUserByOpenId(targetId);
        try {
            // 匹配两个人信息，填充返回必要信息，这里输入的User必须是全部信息不能只有一个openId
            matchingResult = matchService.matchUser(self,targetUser);
            matchingResult.setSelfOpenId(self.getOpenId());
            matchingResult.setTargetOpenId(targetUser.getOpenId());
            matchingResult.setSelfNickName(self.getNickName());
            matchingResult.setTargetNickName(targetUser.getNickName());
            matchingResult.setSelfAvatarUrl(self.getAvatarUrl());
            matchingResult.setTargetAvatarUrl(targetUser.getAvatarUrl());
        }catch (Exception e){
            return AjaxObject.error(e);
        }
        String jsonString = JSON.toJSONString(matchingResult);
        JSONObject matchObject = JSON.parseObject(jsonString);
        matchObject.put("targetAge",targetUser.getAge());
        matchObject.put("targetLocation",targetUser.getCity());
        if(targetUser.getWorkStatus() == null){
            matchObject.put("targetWorkStatus",null);
        }else{
            matchObject.put("targetWorkStatus",targetUser.getWorkStatus() == 0 ? "学生党" : "工作党");
        }
        matchObject.put("targetWeChatID",targetUser.getWechatId());
        matchObject.put("targetImgInfo",targetUser.getUploadImgs());
        return AjaxObject.success("匹配用户信息成功",matchObject);
    }

    /**
     * 判断用户填写信息到第几页
     * @param openId
     * @return
     */
    private Integer checkState(String openId){
        int state = 0;
        User user = userService.getUserByOpenId(openId);
        List<RequirementTag> requirementTagList = requirementTagsService.getAllRequirementTagsByOpenId(openId);
        List<ConditionTag> conditionTagList = conditionTagsService.getConditionTags(openId);
        if(user.getGender()!=null){
            state++;
        }
        if(requirementTagList != null && requirementTagList.size()!=0){
            state++;
        }
        if(user.getAge()!=null && user.getHeight()!=null){
            state++;
        }
        if(conditionTagList != null && conditionTagList.size()!=0){
            state++;
        }
      return  state;
    }

}
