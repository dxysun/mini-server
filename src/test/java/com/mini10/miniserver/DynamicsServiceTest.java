package com.mini10.miniserver;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hankcs.hanlp.corpus.io.IOUtil;
import com.hankcs.hanlp.mining.word2vec.DocVectorModel;
import com.hankcs.hanlp.mining.word2vec.Word2VecTrainer;
import com.hankcs.hanlp.mining.word2vec.WordVectorModel;
import com.mini10.miniserver.common.utils.AesUtil;
import com.mini10.miniserver.mapper.GroupRelationMapper;
import com.mini10.miniserver.mapper.UserMapper;
import com.mini10.miniserver.model.Dynamics;
import com.mini10.miniserver.model.Tag;
import com.mini10.miniserver.model.User;
import com.mini10.miniserver.model.param.MatchingResult;
import com.mini10.miniserver.service.DynamicsService;
import com.mini10.miniserver.service.MatchService;
import com.mini10.miniserver.service.TagService;
import com.mini10.miniserver.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author XiaBin
 * @date 2019-07-24 17:14
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DynamicsServiceTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    private DynamicsService dynamicsService;
    @Resource
    private GroupRelationMapper groupRelationMapper;
    @Autowired
    private TagService tagService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MatchService matchService;
    /**
     * 测试用户匹配排行榜接口
     */
    @Test
    public void getMatchingUserTest(){
        String openId = "orq0K47qNssjmrD62x5nHYrOHYkc";
        Set<MatchingResult> matchingResults = dynamicsService.getMatchingUser(openId);
        List<String> list =  groupRelationMapper.getGroupIdsByOpenId(openId);
        //JSONObject.toJSON(matchingResults);
        logger.info("个人匹配排行榜 = " + JSONObject.toJSON(matchingResults));
        logger.info("该用户参加的所有群组 = " + list);
        logger.info("结束啦哆啦A梦");
    }

    @Test
    public void testAes(){
        String openId = "orq0K47qNssjmrD62x5nHYrOHYkc";
        String passOpenId = AesUtil.AESEncode(openId);
        String newOpenId = AesUtil.AESDecode(passOpenId);
        System.out.println(newOpenId);
    }



    @Test
    public void contextsLoad(){
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null; //用于包装InputStreamReader,提高处理性能。因为BufferedReader有缓冲的，而InputStreamReader没有。

        try {
            String str = "";
            fis = new FileInputStream("e:\\新建文本文档.txt");// FileInputStream
            // 从文件系统中的某个文件中获取字节
            isr = new InputStreamReader(fis);// InputStreamReader 是字节流通向字符流的桥梁,
            br = new BufferedReader(isr);// 从字符输入流中读取文件中的内容,封装了一个new InputStreamReader的对象
            int i = 0;
            List<Tag> tags = new ArrayList<>();
            int j = 1;
            while ((str = br.readLine()) != null) {
                if (str.equals("外在")){
                    i = 0;
                    continue;
                }else if (str.equals("内在")){
                    i = 1;
                    continue;
                }else if (str.equals("兴趣")){
                    i = 2;
                    continue;
                }else if (str.equals("其他")){
                    i = 3;
                    continue;
                }
                Tag tag = new Tag();
                tag.setTagName(str);
                tag.setTagType(3);
                tag.setTagClassify(i);
                tag.setSort(j);
                j++;
                tags.add(tag);
            }
            tagService.bathAddTags(tags);


        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                br.close();
                isr.close();
                fis.close();
                // 关闭的时候最好按照先后顺序关闭最后开的先关闭所以先关s,再关n,最后关m
            } catch (IOException e) {
                e.printStackTrace();
            }
    }


}

    @Test
    public void insertDynamicsTest(){
        Dynamics dynamics = new Dynamics();
        dynamics.setOpenId("8888");
        dynamics.setDiary("啦啦啦，啦啦啦，我是卖报的小行家");
        dynamics.setPoster("http://baozhine.com/12345");
        dynamics.setSecret(false);
        dynamics.setEmotionRate(0);
        int num = dynamicsService.insertDynamics(dynamics);
        System.out.println("插入日记数:" + num);
    }


    @Test
    @Transactional
    public void deleteDiaryTest(){
        boolean flag = dynamicsService.deleteDiary(34);
        System.out.println("删除日记状态:" + flag);
    }

    @Test
    public void selectAllDynamicsTest(){
        JSONObject jsonObject = dynamicsService.selectAllDynamics(1, 3, "91492");
        System.out.println(jsonObject.toJSONString());
    }

    @Test
    public void getNewestDynamicsTest(){
        JSONObject jsonObject = dynamicsService.getNewestDynamics("91492");
        System.out.println(jsonObject.toJSONString());
    }

    @Test
    public void diaryMatchTest(){
        JSONArray jsonArray = dynamicsService.diaryMatch("orq0K47qNssjmrD62x5nHYrOHYkc");
        System.out.println(jsonArray.toJSONString());
    }

    @Test
    public void getMatchedUserDiaryTest(){
        JSONObject jsonObject = dynamicsService.getMatchedUserDiary("orq0K47qNssjmrD62x5nHYrOHYkc", 1, 2);
        System.out.println(jsonObject.toJSONString());
    }

}


