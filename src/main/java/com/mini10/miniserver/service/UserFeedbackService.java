package com.mini10.miniserver.service;

import com.mini10.miniserver.model.UserFeedback;

/**
 * @author XiaBin
 * @date 2019-07-24 15:48
 */
public interface UserFeedbackService {
    Integer insertFeedback(String openId,String content);
}
