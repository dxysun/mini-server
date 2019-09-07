package com.mini10.miniserver.config;

import com.alibaba.fastjson.JSONObject;
import com.mini10.miniserver.common.Constant;
import com.mini10.miniserver.common.utils.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * @author dongxiyan
 * @date 2019-07-19 16:12
 */
@Component
public class ScheduledForDynamicConfig implements SchedulingConfigurer {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static Integer expiresIn = 1;
    private static Integer TRANSITION_TIME = 300;

    @Autowired
    private HttpUtil httpUtil;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(() -> {
            // 定时任务的业务逻辑
            JSONObject jsonObject = httpUtil.doGet("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" +Constant.APPID + "&secret=" + Constant.APPSECRET);
            Constant.ACCESS_TOKEN = (String)jsonObject.get("access_token");
            expiresIn = (Integer) jsonObject.get("expires_in");
            logger.info("ACCESS_TOKEN：" + jsonObject.toJSONString());
        }, (triggerContext) -> {
            Trigger trigger  = new Trigger(){
                @Override
                public Date nextExecutionTime(TriggerContext triggerContext) {
                    LocalDateTime localDateTime = LocalDateTime.now();
                    expiresIn /= 2;
                    localDateTime =  localDateTime.plusSeconds(expiresIn);
                    ZoneId zoneId = ZoneId.systemDefault();
                    ZonedDateTime zdt = localDateTime.atZone(zoneId);
                    return Date.from(zdt.toInstant());
                }
            };
            return trigger.nextExecutionTime(triggerContext);
        });
    }
    public void getLatestToken(){
        // 定时任务的业务逻辑
        JSONObject jsonObject = httpUtil.doGet("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" +Constant.APPID + "&secret=" + Constant.APPSECRET);
        Constant.ACCESS_TOKEN = (String)jsonObject.get("access_token");
        expiresIn = (Integer) jsonObject.get("expires_in");
    }
}
