package com.mini10.miniserver;

import com.mini10.miniserver.service.UserFeedbackService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author XiaBin
 * @date 2019-08-11 10:50
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserFeedbackServiceTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    private UserFeedbackService userFeedbackService;

    @Test
    public void insertFeedback(){
        String openId = "orq0K46PLC4gQEnFDbZeXXsJq6Nm";
        String content = "测试test";
        Integer result = userFeedbackService.insertFeedback(openId,content);
        logger.info("测试insertFeedback=" +result);
    }
}
