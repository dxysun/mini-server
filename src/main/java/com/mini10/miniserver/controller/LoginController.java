package com.mini10.miniserver.controller;

import com.alibaba.fastjson.JSONObject;
import com.mini10.miniserver.common.Constant;
import com.mini10.miniserver.common.Result;
import com.mini10.miniserver.common.utils.AjaxObject;
import com.mini10.miniserver.common.utils.RedisUtil;
import com.mini10.miniserver.model.User;
import com.mini10.miniserver.service.ConditionTagsService;
import com.mini10.miniserver.service.RequirementTagsService;
import com.mini10.miniserver.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
@RequestMapping("/user")
public class LoginController {

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ConditionTagsService conditionTagsService;
    @Autowired
    private RequirementTagsService requirementTagsService;

    @Value("${spring.profiles.active}")
    private String springProfilesActive;

    @GetMapping("/check")
    public Result check(HttpServletRequest request) {

        return AjaxObject.success(Constant.HOST_NAME + ":服务运行良好：" + springProfilesActive, null);
    }

    /**
     * 登录，根据code得到openId
     * @param code
     * @param request
     * @return
     */
    @GetMapping("/login")
    public Result login(@RequestParam(value = "code", required = false) String code, HttpServletRequest request) {
        if (StringUtils.isEmpty(code)) {
            return AjaxObject.error("code为空，无法获取到用户信息");
        }
        String openId = null;
        JSONObject openInfo = null;
        String sessionKey = null;
        try {
            openInfo = userService.getOpenInfoByCode(code);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxObject.error(Constant.ResultCode.LOGIN_TIME_OUT,"获取用户信息超时");
        }
        openId = openInfo.getString("openid");
        sessionKey = openInfo.getString("session_key");
        if (StringUtils.isEmpty(openId)) {
            return AjaxObject.error("openId为空，无法获取到用户信息");
        }
        User user = userService.getUserByOpenId(openId);
        // 自动生成Session ID
        String sessionId = request.getSession(true).getId();
        logger.info("login sessionId:" + sessionId);
        if (!redisUtil.set(sessionId, openId, Constant.SESSION_EXPIRED_TIME)) {
            return AjaxObject.error("设置redis缓存失败");
        } else {
            redisUtil.set(sessionId + Constant.REDIS_SESSION_KEY, sessionKey, Constant.SESSION_EXPIRED_TIME);
        }
        if (user == null) {
            user = new User();
            user.setOpenId(openId);
            userService.registerUser(user);
            JSONObject dataJson = new JSONObject();
            dataJson.put("openId", openId);
            dataJson.put("loginStatus", 0);
            dataJson.put("user", user);
            dataJson.put("requirementTagFlag", 0);
            dataJson.put("necessaryTagFlag", 0);
            dataJson.put("conditionTagFlag", 0);
            return AjaxObject.success("用户未注册", dataJson);
        } else {
            JSONObject dataJson = new JSONObject();
            dataJson.put("openId", openId);
            dataJson.put("loginStatus", 1);
            dataJson.put("user", user);
            if (!CollectionUtils.isEmpty(requirementTagsService.getNecessaryTagsByOpenId(openId))) {
                dataJson.put("necessaryTagFlag", 1);
            } else {
                dataJson.put("necessaryTagFlag", 0);
            }
            if (!CollectionUtils.isEmpty(requirementTagsService.getRequirementTagsByOpenId(openId))) {
                dataJson.put("requirementTagFlag", 1);
            } else {
                dataJson.put("requirementTagFlag", 0);
            }
            if (!CollectionUtils.isEmpty(conditionTagsService.getConditionTags(openId))) {
                dataJson.put("conditionTagFlag", 1);
            } else {
                dataJson.put("conditionTagFlag", 0);
            }
            return AjaxObject.success("用户已注册", dataJson);
        }
    }
}
