package com.mini10.miniserver.service.impl;

import com.mini10.miniserver.mapper.UserFeedbackMapper;
import com.mini10.miniserver.model.UserFeedback;
import com.mini10.miniserver.service.UserFeedbackService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author XiaBin
 * @date 2019-07-24 15:49
 */
@Service
public class UserFeedbackServiceImpl implements UserFeedbackService {

    @Resource
    private UserFeedbackMapper userFeedbackMapper;

    /**
     * 添加用户反馈信息
     * @param openId
     * @param content
     * @return
     */
    @Override
    public Integer insertFeedback(String openId,String content) {
        UserFeedback userFeedback = new UserFeedback();
        userFeedback.setOpenId(openId);
        userFeedback.setContent(content);
        return userFeedbackMapper.insertFeedback(userFeedback);
    }
}
