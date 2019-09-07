package com.mini10.miniserver.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;

import com.tencentcloudapi.nlp.v20190408.NlpClient;

import com.tencentcloudapi.nlp.v20190408.models.SentimentAnalysisRequest;
import com.tencentcloudapi.nlp.v20190408.models.SentimentAnalysisResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component("emotionUtil")
/**
 * 此类用于调用腾讯云人工智能情感分析SDK对文本分析
 * @author xiangjiangnan
 * @date 20190722
 */
public class EmotionUtil {

    public static final int EMOTION_NEGATIVE = -1;
    public static final int EMOTION_POSITIVE = 1;
    public static final int EMOTION_NEUTRAL = 0;
    public static final int DEFAULT_EMOTION = -2;
    public static final BigDecimal OFFSET = BigDecimal.valueOf(0.1);

    /**
     * 此方法依据用户的文本信息返回情感状态
     * @param diary 用户的日记文本
     * @return 返回对应的三种情感状态-1,0,1
     */
    public int getEmotion(String diary){
        BigDecimal negative = BigDecimal.valueOf(0);
        BigDecimal positive = BigDecimal.valueOf(0);
        try{
            Credential cred = new Credential("AKID0oRp7AWWrxnRp6nuRjmZAVigPuAABTlu", "4tSlobR2QZS6UdWCcuxlJcfT4QfgS9cq");

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("nlp.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            NlpClient client = new NlpClient(cred, "ap-guangzhou", clientProfile);

            String params = "{\"Text\":\"" + diary + "\"}";
            SentimentAnalysisRequest req = SentimentAnalysisRequest.fromJsonString(params, SentimentAnalysisRequest.class);
            SentimentAnalysisResponse resp = client.SentimentAnalysis(req);
            JSONObject jsonObject = JSON.parseObject(SentimentAnalysisRequest.toJsonString(resp));
            negative = (BigDecimal) jsonObject.get("Negative");
            positive = (BigDecimal) jsonObject.get("Positive");
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }
        return getEmotionStatus(negative, positive);
    }

    /**
     * 此方法作为一个私有方法用于判断情感状态
     * @param negative 负面参数值
     * @param positive 正面参数值
     * @return 返回情感状态值
     */
    private int getEmotionStatus(BigDecimal negative, BigDecimal positive){
        if(negative.compareTo(positive) > 0){
            if(negative.subtract(OFFSET).compareTo(positive) > 0){
                return EMOTION_NEGATIVE;
            }
            return EMOTION_NEUTRAL;
        }else{
            if(positive.subtract(OFFSET).compareTo(negative) > 0){
                return EMOTION_POSITIVE;
            }
            return EMOTION_NEUTRAL;
        }
    }
}
