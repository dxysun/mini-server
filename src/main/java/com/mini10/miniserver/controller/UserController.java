package com.mini10.miniserver.controller;


import com.alibaba.fastjson.JSONObject;
import com.mini10.miniserver.common.Constant;
import com.mini10.miniserver.common.Result;
import com.mini10.miniserver.common.utils.AjaxObject;
import com.mini10.miniserver.common.utils.HttpUtil;
import com.mini10.miniserver.common.utils.RedisUtil;
import com.mini10.miniserver.common.utils.RequestUtil;
import com.mini10.miniserver.common.utils.netease.NetEaseApi;
import com.mini10.miniserver.config.ScheduledForDynamicConfig;
import com.mini10.miniserver.model.ConditionTag;
import com.mini10.miniserver.model.RequirementTag;
import com.mini10.miniserver.model.User;
import com.mini10.miniserver.service.ConditionTagsService;
import com.mini10.miniserver.service.RequirementTagsService;
import com.mini10.miniserver.service.UserService;
import com.netease.cloud.services.nos.model.ObjectMetadata;
import com.netease.cloud.services.nos.transfer.TransferManager;
import com.netease.cloud.services.nos.transfer.Upload;
import com.netease.cloud.services.nos.transfer.model.UploadResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * chenshaojie
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    private static Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private RequirementTagsService requirementTagsService;
    @Autowired
    private ConditionTagsService conditionTagsService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RequestUtil requestUtil;

    /**
     * 设置用户基础信息 @created by chenshaojie
     *
     * @param user
     * @return Result
     */
    @PostMapping("/updateUserData")
    public Result updateUserData(@RequestBody User user) {
        if (user == null) {
            return AjaxObject.error("用户信息为空,无法修改");
        }
        try {
            if (!StringUtils.isEmpty(user.getOpenId())) {
                if(user.getBirth() != null){
                    LocalDate localDate = LocalDate.now();
                    int nowYear = localDate.getYear();
                    LocalDate birthDate = LocalDate.parse(user.getBirth(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    user.setAge(nowYear - birthDate.getYear());
                }
                if(!StringUtils.isEmpty(user.getSchool())){
                    JSONObject jsonObject = NetEaseApi.checkTxtApi(UUID.randomUUID().toString(),user.getSchool());
                    if(jsonObject != null){
                        if (Integer.parseInt(jsonObject.getJSONObject("result").getString("action")) == 2) {
                            user.setSchool("");
                            userService.updateUserData(user);
                            return AjaxObject.error(Constant.ResultCode.UQUALIFIED_CODE,"请对学校名称使用文明用语");
                        }
                    }
                }
                userService.updateUserData(user);
                return AjaxObject.success("用户数据更新成功",user);
            } else {
                return AjaxObject.error("openId为空");
            }
        } catch (Exception e) {
            log.error(AjaxObject.getStackTrace(e));
            return AjaxObject.error(e);
        }
    }

    /**
     * 获取用户所有信息
     *
     * @param openId
     * @return
     */
    @GetMapping("/getUserAllInfo")
    public Result getUserAllInfo(@RequestParam("openId") String openId) {
        if (openId == null) {
            return AjaxObject.sessionIdError();
        }
        try {
            return AjaxObject.success("获取用户信息成功", userService.getUserByOpenId(openId));
        } catch (Exception e) {
            log.error(AjaxObject.getStackTrace(e));
            return AjaxObject.error(e);
        }
    }

    /**
     * 获取个人主页用户信息
     *
     * @param openId
     * @return Result
     * @author cuiyang
     */
    @GetMapping("/getPersonInfo")
    public Result getPersonInfo(@RequestParam("openId") String openId) {
        if (openId == null) {
            return AjaxObject.sessionIdError();
        }
        Map<String, Object> map = new HashMap<String, Object>();
        User user = null;
        List<RequirementTag> requirementTagList = null;
        List<ConditionTag> conditionTagsServiceList = null;
        try {
            user = userService.getUserByOpenId(openId);
            requirementTagList = requirementTagsService.getAllRequirementTagsByOpenId(openId);
            conditionTagsServiceList = conditionTagsService.getConditionTags(openId);
        } catch (Exception e) {
            log.error(AjaxObject.getStackTrace(e));
            return AjaxObject.error(e);
        }
        map.put("user", user);
        map.put("requirementTagList", requirementTagList);
        map.put("conditionTagsServiceList", conditionTagsServiceList);
        return AjaxObject.success("获取信息成功", map);

    }

    /**
     * 获取用户自己的标签
     *
     * @param openId
     * @return Result
     */
    @GetMapping("/getUserTag")
    public Result getUserTag(@RequestParam("openId") String openId, @RequestParam("type") Integer type) {
        if (openId == null) {
            return AjaxObject.sessionIdError();
        }
        if (type == null) {
            return AjaxObject.error("type为空");
        }
        try {
            Map<String, Object> map = new HashMap<>();
            List<Map<String, String>> conditionMap = null;
            JSONObject requirementMap = new JSONObject();

            if (type == 0) {
                conditionMap = conditionTagsService.getUserTag(openId);
            } else if (type == 1) {
                requirementMap = requirementTagsService.getUserTag(openId);
            } else {
                conditionMap = conditionTagsService.getUserTag(openId);
                requirementMap = requirementTagsService.getUserTag(openId);
            }
            map.put("my", conditionMap);
            map.put("other", requirementMap);

            return AjaxObject.success("获取用户标签成功", map);
        } catch (Exception e) {
            log.error(AjaxObject.getStackTrace(e));
            return AjaxObject.error(e);
        }
    }

    /**
     * 获取匹配对象的信息
     *
     * @param jsonObject
     * @return Result
     */
    @PostMapping("/getTargetInfo")
    public Result getTargetInfo(@RequestBody JSONObject jsonObject) {
        String targetOpenId = jsonObject.getString("targetOpenId");
        if (StringUtils.isEmpty(targetOpenId)) {
            return AjaxObject.error("对方的openId为空");
        }
        try {
            User user = userService.getUserByOpenId(targetOpenId);
            if (user == null) {
                return AjaxObject.error("Target用户为空");
            }
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("age", user.getAge());
            userMap.put("location", user.getCity());
            if (user.getWorkStatus() != null) {
                userMap.put("workStatus", user.getWorkStatus() == 0 ? "学生党" : "工作党");
            } else {
                userMap.put("workStatus", null);
            }
            userMap.put("images", new String[]{user.getAvatarUrl()});
            userMap.put("wechat", user.getWechatId());
            return AjaxObject.success("获取目标用户数据成功", userMap);
        } catch (Exception e) {
            log.error(AjaxObject.getStackTrace(e));
            return AjaxObject.error(e);
        }
    }

    /**
     * 得到用于分享的小程序码
     *
     * @param openId
     * @return Result
     */
    @GetMapping("/getShareCode")
    public Result getShareCode(@RequestParam("openId") String openId) {
        if (openId == null) {
            return AjaxObject.sessionIdError();
        }
        String shareCodeUrl = (String) redisUtil.get(openId + Constant.REDIS_SHARE_CODE_URL);
        if (shareCodeUrl != null) {
            Map<String, String> map = new HashMap<>();
            map.put("filePath", shareCodeUrl);
            return AjaxObject.success("获取二维码成功", map);
        } else {
            if (StringUtils.isEmpty(Constant.ACCESS_TOKEN)) {
                return AjaxObject.error("ACCESS_TOKEN为空");
            }
            try {
                JSONObject result = requestUtil.getShareCodeUrl(openId);
                if (result == null) {
                    return AjaxObject.error("得到小程序二维码失败");
                }
                if (result.get("errcode") == null) {
                    return AjaxObject.success("成功得到小程序二维码", result);
                } else {
                    result = requestUtil.getShareCodeUrl(openId);
                    if(result != null && result.get("errcode") == null){
                        return AjaxObject.success("成功得到小程序二维码", result);
                    } else {
                        return AjaxObject.error("得到小程序二维码失败");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return AjaxObject.error("得到小程序二维码异常", e);
            }
        }
    }
}
