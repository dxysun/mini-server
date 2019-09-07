package com.mini10.miniserver;

import com.mini10.miniserver.model.GroupRelation;
import com.mini10.miniserver.model.VirtualGroupInfo;
import com.mini10.miniserver.model.VirtualGroupRelation;
import com.mini10.miniserver.service.GroupService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author ChenShaoJie
 * @Date 2019-08-11 17:05
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class GroupServiceTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private GroupService groupService;

    @Transactional
    @Test
    public void addGroupRelationInfoTest() {
        GroupRelation groupRelation = new GroupRelation();
        groupRelation.setGroupId("133");
        groupRelation.setOpenId("131");
        try {
            groupService.addGroupRelationInfo(groupRelation);

        } catch (Exception e) {
            logger.info("" + e);

        }
    }

    @Transactional
    @Test
    public void addVirtualGroupRelationInfoTest() {
        VirtualGroupRelation virtualGroupRelation = new VirtualGroupRelation();
        virtualGroupRelation.setOpenId("13");
        virtualGroupRelation.setVirtualGroupId("1231");
        virtualGroupRelation.setId(123);
        try {
            groupService.addVirtualGroupRelationInfo(virtualGroupRelation);
        } catch (Exception e) {
            logger.info("" + e);
        }
    }

    @Transactional
    @Test
    public void addVirtualGroupInfoTest() {
        VirtualGroupInfo virtualGroupRelation = new VirtualGroupInfo();
        virtualGroupRelation.setVirtualGroupId("1231");
        virtualGroupRelation.setVirtualGroupName("1313");
        try {
            groupService.addVirtualGroupInfo("123",virtualGroupRelation);
        } catch (Exception e) {
            logger.info("" + e);
        }
    }

    @Transactional
    @Test
    public void getGroupIdsByOpenIdTest(){
        List<String> list = groupService.getGroupIdsByOpenId("oB5mm5M1JJCIVOAcf0GLO0Tt4Lwo");
    }

    @Transactional
    @Test
    public void getVirtualGroupByPositionTest(){
        groupService.getVirtualGroupByPosition("oB5mm5M1JJCIVOAcf0GLO0Tt4Lwo","123","213");
    }

    @Transactional
    @Test
    public void deleteGroupInfoTest(){
        groupService.deleteGroupInfo("oB5mm5HGzU3zLC9Hfmcqnni6-3Lk","GB5mm5CSo9xKsvmSRjRHtqG3fI_0");
    }
}
