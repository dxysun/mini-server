package com.mini10.miniserver.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.mini10.miniserver.common.Constant;
import com.mini10.miniserver.config.ScheduledForDynamicConfig;
import com.netease.cloud.services.nos.model.ObjectMetadata;
import com.netease.cloud.services.nos.transfer.TransferManager;
import com.netease.cloud.services.nos.transfer.Upload;
import com.netease.cloud.services.nos.transfer.model.UploadResult;
import org.apache.tomcat.util.bcel.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Component("RequestUtil")
public class RequestUtil {
    private final Logger log = LoggerFactory.getLogger(RequestUtil.class);
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    public TransferManager transferManager;

    @Autowired
    private HttpUtil httpUtil;

    @Autowired
    ScheduledForDynamicConfig scheduledForDynamicConfig;

    public String getOpenId(HttpServletRequest request) {
        String sessionId = request.getRequestedSessionId();
        return (String) redisUtil.get(sessionId);
    }

    public String getSessionKey(HttpServletRequest request) {
        String sessionId = request.getRequestedSessionId();
        return (String) redisUtil.get(sessionId + Constant.REDIS_SESSION_KEY);
    }

    public JSONObject getShareCodeUrl(String openId) throws Exception {
        JSONObject jsonObject = new JSONObject();
        System.out.println("getShareCodeUrl:" + openId);
        jsonObject.put("scene", openId);
        JSONObject resultJson = new JSONObject();
        byte[] buffer = httpUtil.doPostForBuffer("https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + Constant.ACCESS_TOKEN, jsonObject);
        if (buffer != null && buffer.length > 80000) {
            InputStream inputStream = new ByteArrayInputStream(buffer);
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(buffer.length);
            Upload upload = transferManager.upload("mini10", "miniprogramcode/" + UUID.randomUUID().toString() + ".png", inputStream, objectMetadata);
            UploadResult uploadResult = upload.waitForUploadResult();
            String filePath = Constant.NeteaseYun.YUN_URL + uploadResult.getKey();
            redisUtil.set(openId + Constant.REDIS_SHARE_CODE_URL, filePath, Constant.SHARE_CODE_EXPIRED_TIME);
            resultJson.put("filePath", filePath);
            return resultJson;
        } else {
            if (buffer != null) {
                JSONObject errorJson = JSONObject.parseObject(new String(buffer));
                if (errorJson.getInteger("errcode").equals(40001)) {
                    scheduledForDynamicConfig.getLatestToken();
                    return errorJson;
                }
            }
            return null;
        }
    }

    public Double getSimilarity(String text1, String text2) {
        String url = "https://aip.baidubce.com/rpc/2.0/nlp/v2/simnet?access_token=" + Constant.BAIDU_ACCESS_TOKEN + "&charset=UTF-8" ;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("text_1",text1);
        jsonObject.put("text_2",text2);
        JSONObject res = httpUtil.doPost(url,jsonObject);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return res.getDouble("score");
    }

    public JSONObject getAddress(String longitude, String latitude) {
        String d = "https://restapi.amap.com/v3/geocode/regeo?output=json&location=" + longitude + "," + latitude + "&key=" + Constant.Amap.AMAP_KEY + "&radius=1000&extensions=all";
        return httpUtil.doGet(d);
    }

}
