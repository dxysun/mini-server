package com.mini10.miniserver;

import com.alibaba.fastjson.JSONObject;
import com.mini10.miniserver.common.utils.HttpUtil;
import com.mini10.miniserver.mapper.UserMapper;
import com.mini10.miniserver.model.User;
import com.mini10.miniserver.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.beans.Transient;

/**
 * @author dongxiyan
 * @date 2019-07-19 16:12
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;


    @Test
    public void getOpenInfoByCodeTest() {
        JSONObject res = userService.getOpenInfoByCode("021h5NZl0MPkDq1IlZVl0JxQZl0h5NZQ");
        if(res != null){
            logger.info(res.toJSONString());
        }else{
            logger.info("结果为空");
        }
    }

    @Test
    public void getUserByOpenIdTest(){
        User user = userService.getUserByOpenId("oB5mm5DaCM_umc_cpAIJS8IejqQU");
        logger.info(user.getNickName());
    }

    @Test
    @Transactional
    public void registerUserTest(){
        User user1 = new User();
        user1.setOpenId("testOpenId");
        userService.registerUser(user1);
    }

    @Test
    @Transactional
    public void updateUserDataTest(){

        User user1 =  userService.getUserByOpenId("oB5mm5DaCM_umc_cpAIJS8IejqQU");
        user1.setSchool("南京理工大学");
        user1.setCompany("网易");
        Integer res = userService.updateUserData(user1);
        logger.info("res:" + res);
    }

    @Test
    public void getNickNameByOpenIdTest(){

        String nickName = userService.getNickNameByOpenId("oB5mm5DaCM_umc_cpAIJS8IejqQU");
        logger.info("nickName:" + nickName);

    }

    @Test
    public void getUserScoreTest(){

        Integer score = userService.getUserScore("oB5mm5DaCM_umc_cpAIJS8IejqQU");
        logger.info("score:" + score);

    }




}
