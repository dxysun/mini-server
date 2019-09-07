package com.mini10.miniserver.controller;

import com.alibaba.fastjson.JSONObject;
import com.mini10.miniserver.common.Result;
import com.mini10.miniserver.common.utils.AjaxObject;
import com.mini10.miniserver.common.utils.netease.NetEaseApi;
import com.mini10.miniserver.service.UserFeedbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author XiaBin
 * @date 2019-07-24 15:52
 * 用户反馈表controller
 */
@RestController
@RequestMapping("/api/feedback")
public class UserFeedbackController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserFeedbackService updateUserData;

    /**
     * 用户提交反馈信息
     * @param jsonObject
     * @return
     */
    @PostMapping("/addFeedback")
    public Result updateUserData(@RequestBody JSONObject jsonObject) {
        logger.info("打印传过来的body=" + jsonObject);
        String openId = (String)jsonObject.get("openId");
        String content = (String)jsonObject.get("content");
        logger.info("openId=" + openId);
        logger.info("content=" + content);
        if (StringUtils.isEmpty(openId)) {
            return AjaxObject.error("无法获取用户信息");
        }
        if (StringUtils.isEmpty(content)) {
            return AjaxObject.error("用户反馈不能为空");
        }
        // 校验色情暴力
        JSONObject qualified = NetEaseApi.checkTxtApi("ebfcad1c-dba1-490c-b4de-e784c2691768",content);
        if(qualified != null){
            Integer action = qualified.getJSONObject("result").getInteger("action");
            if (action == 2){
                return AjaxObject.incivilization("请文明用语");
            }
        }
        try {
            updateUserData.insertFeedback(openId,content);
        }catch (Exception e){
            return AjaxObject.error(e);
        }
        return AjaxObject.success("添加用户反馈信息成功",null);
    }
}
