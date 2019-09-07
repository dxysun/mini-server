package com.mini10.miniserver.mapper;

import com.mini10.miniserver.model.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;
@Component
public interface UserMapper extends Mapper<User> {

    User queryUserByOpenId(@Param("openId") String openId);

    void addUser(User user);

    Integer updateUserDate(User user);

    String queryNickNameByOpenId(@Param("openId")String openId);
}