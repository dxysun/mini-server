package com.mini10.miniserver;

import com.alibaba.fastjson.JSONArray;
import com.mini10.miniserver.model.Tag;
import com.mini10.miniserver.service.TagService;
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
public class TagServiceTest {

    @Autowired
    private TagService tagService;

    @Test
    public void getTagsTest(){
        Map<String, Map<String,Integer>> map = tagService.getTags(0);
        System.out.println(map);
    }

    @Test
    public void getTagsNewTest(){
        JSONArray jsonArray = tagService.getTagsNew(0);
        System.out.println(jsonArray);
    }

    @Test
    @Transactional
    public void bathAddTagsTest(){
        List<Tag> list = new ArrayList<>();
        Tag tag1 = new Tag();
        Tag tag2 = new Tag();

        tag1.setTagName("天下第一");
        tag1.setSort(1);
        tag1.setTagClassify(2);
        tag1.setTagType(1);
        list.add(tag1);

        tag2.setTagName("宇宙第一");
        tag2.setSort(1);
        tag2.setTagClassify(2);
        tag2.setTagType(1);
        list.add(tag2);
        tagService.bathAddTags(list);
    }
}
