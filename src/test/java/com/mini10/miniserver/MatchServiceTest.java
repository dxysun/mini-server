package com.mini10.miniserver;

import com.hankcs.hanlp.mining.word2vec.DocVectorModel;
import com.hankcs.hanlp.mining.word2vec.WordVectorModel;
import com.mini10.miniserver.mapper.GroupRelationMapper;
import com.mini10.miniserver.mapper.UserMapper;
import com.mini10.miniserver.model.User;
import com.mini10.miniserver.service.DynamicsService;
import com.mini10.miniserver.service.MatchService;
import com.mini10.miniserver.service.TagService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;

import static com.mini10.miniserver.common.Constant.MODEL_FILE_NAME;
import static com.mini10.miniserver.common.Constant.docVectorModel;
import static com.mini10.miniserver.common.Constant.wordVectorModel;
import static java.lang.Thread.sleep;

/**
 * @Author ChenShaoJie
 * @Date 2019-08-11 11:01
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class MatchServiceTest {

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

    @Test
    public void getMatchResultTest() throws IOException {
        User targetUser = userMapper.queryUserByOpenId("oB5mm5M1JJCIVOAcf0GLO0Tt4Lwo");
        User selfUser = userMapper.queryUserByOpenId("oB5mm5N5y4frKfVLQrto21y_CwR4");
        //   MatchingResult result = matchService.matchUser(selfUser,targetUser);

        wordVectorModel = new WordVectorModel(MODEL_FILE_NAME);
        docVectorModel = new DocVectorModel(wordVectorModel);
        while(wordVectorModel == null || docVectorModel == null){
            continue;
        }
        matchService.getMatchResult("oB5mm5M1JJCIVOAcf0GLO0Tt4Lwo");
        logger.info("");
    }

    @Test
    public void matchUserTest() throws IOException, InterruptedException {
        User targetUser = userMapper.queryUserByOpenId("oB5mm5N5y4frKfVLQrto21y_CwR4");
        User selfUser = userMapper.queryUserByOpenId("oB5mm5HAq6pl_jZLbDWG5SDUgWMQ");
        wordVectorModel = new WordVectorModel(MODEL_FILE_NAME);
        docVectorModel = new DocVectorModel(wordVectorModel);

        matchService.matchUser(selfUser,targetUser);
        logger.info("");
    }

}
