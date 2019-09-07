package com.mini10.miniserver;

import com.alibaba.fastjson.JSONObject;
import com.mini10.miniserver.model.RequirementTag;
import com.mini10.miniserver.service.RequirementTagsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Xiang Jiangnan
 * @date 2019-08-11 19:30
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RequirementTagServiceTest {

    @Autowired
    private RequirementTagsService requirementTagsService;

    @Test
    @Transactional
    public void setRequirementTags(){
        List<Integer> tags = new ArrayList<>();
        tags.add(1);
        tags.add(2);
        requirementTagsService.setRequirementTags("91492", tags);
    }

    @Test
    @Transactional
    public void setNecessaryTags(){
        List<Integer> tags = new ArrayList<>();
        tags.add(1);
        tags.add(2);
        requirementTagsService.setNecessaryTags("91492", tags);
    }

    @Test
    public void getAllRequirementTagsByOpenId(){
        List<RequirementTag> list = requirementTagsService.getAllRequirementTagsByOpenId("oB5mm5O-BnFEKiU1rpTwgiCtxU-w");
        System.out.println(list.size());
    }

    @Test
    public void getUserTag(){
        JSONObject jsonObject = requirementTagsService.getUserTag("oB5mm5O-BnFEKiU1rpTwgiCtxU-w");
        System.out.println(jsonObject.toJSONString());
    }

    @Test
    public void getNecessaryTagsByOpenId(){
        List<RequirementTag> list = requirementTagsService.getNecessaryTagsByOpenId("oB5mm5O-BnFEKiU1rpTwgiCtxU-w");
        System.out.println(list.size());
    }

    @Test
    public void getRequirementTagsByOpenId(){
        List<RequirementTag> list = requirementTagsService.getRequirementTagsByOpenId("oB5mm5O-BnFEKiU1rpTwgiCtxU-w");
        System.out.println(list.size());
    }
}
