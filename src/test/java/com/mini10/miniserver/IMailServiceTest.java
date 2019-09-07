package com.mini10.miniserver;

import com.mini10.miniserver.common.entity.Email;
import com.mini10.miniserver.service.IMailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author dongxiyan
 * @date 2019-07-19 16:12
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class IMailServiceTest {

    @Autowired
    private IMailService iMailService;

    @Test
    public void sendTest(){
        Email email = new Email();
        iMailService.send(email);
    }



}
