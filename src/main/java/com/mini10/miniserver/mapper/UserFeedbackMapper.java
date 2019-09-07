package com.mini10.miniserver.mapper;

import com.mini10.miniserver.model.UserFeedback;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

@Component
public interface UserFeedbackMapper extends Mapper<UserFeedback> {
    Integer insertFeedback(UserFeedback userFeedback);
}