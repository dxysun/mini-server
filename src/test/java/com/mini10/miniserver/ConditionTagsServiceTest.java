package com.mini10.miniserver;

import com.mini10.miniserver.model.ConditionTag;
import com.mini10.miniserver.service.ConditionTagsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Xiang Jiangnan
 * @date 2019-08-11 17:30
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ConditionTagsServiceTest {

    @Autowired
    private ConditionTagsService conditionTagsService;

    @Test
    @Transactional
    public void setConditionTagsTest(){
        String openId = "91492";
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        boolean flag = conditionTagsService.setConditionTags(openId, list);
        System.out.println(flag);
    }

    @Test
    public void getConditionTagsTest(){
        List<ConditionTag> list = conditionTagsService.getConditionTags("oB5mm5O-BnFEKiU1rpTwgiCtxU-w");
        System.out.println(list.size());
    }

    @Test
    public void getUserTagTest(){
        List<Map<String,String>> list = conditionTagsService.getUserTag("oB5mm5O-BnFEKiU1rpTwgiCtxU-w");
        System.out.println(list);
    }
}
