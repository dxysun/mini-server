package com.mini10.miniserver.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mini10.miniserver.common.Constant;
import com.mini10.miniserver.common.utils.*;
import com.mini10.miniserver.common.utils.MyComparator;

import com.mini10.miniserver.common.utils.TimeTransferUtil;
import com.mini10.miniserver.mapper.*;
import com.mini10.miniserver.model.Dynamics;
import com.mini10.miniserver.model.DynamicsBlacklist;
import com.mini10.miniserver.model.User;
import com.mini10.miniserver.model.param.DynamicDto;
import com.mini10.miniserver.model.DynamicsTemplate;
import com.mini10.miniserver.model.param.MatchingResult;
import com.mini10.miniserver.service.DynamicsService;
import com.mini10.miniserver.service.MatchService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.SocketUtils;
import org.springframework.util.StringUtils;

/**
 * @author corn1ng
 */
import javax.annotation.Resource;
import java.util.*;

@Service
public class DynamicsServiceImpl implements DynamicsService {


    @Autowired
    private DynamicsMapper dynamicsMapper;

    @Autowired
    private TimeTransferUtil timeTransferUtil;

    @Autowired
    private DynamicsTemplateMapper dynamicsTemplateMapper;

    @Autowired
    private GroupRelationMapper groupRelationMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MatchService matchService;

    @Resource
    private RedisUtil redisUtil;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private DynamicsBlacklistMapper dynamicsBlacklistMapper;


    /**
     * 此方法用于匹配用户排行榜中匹配程度高的用户的日记
     *
     * @param openId
     * @return 返回日记的JSON字符串
     */
    @Override
    public JSONArray diaryMatch(String openId) {
        List<DynamicDto> list = diaryMatchUsers(openId);
        JSONArray jsonArray = new JSONArray();
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        //随机打乱元素顺序
        Collections.shuffle(list);
        for (DynamicDto dynamics : list) {

            JSONObject json = new JSONObject();
            //返回被匹配用户id
            json.put("id", dynamics.getId());
            json.put("diary", dynamics.getDiary());
            json.put("poster", dynamics.getPoster());
            json.put("nickname", dynamics.getNickName());
            json.put("createTime", timeTransferUtil.date2String(dynamics.getCreateTime()));
            json.put("date", timeTransferUtil.getYMD((String) json.get("createTime")));
            json.put("month", timeTransferUtil.getMonth((String) json.get("createTime")));
            json.put("day", timeTransferUtil.getDay((String) json.get("createTime")));
            json.put("groupId", dynamics.getGroupId());
            jsonArray.add(json);
        }
        return jsonArray;
    }


    private List<DynamicDto> diaryMatchUsers(String openId) {
        List<DynamicDto> resultList = new ArrayList<>();
        List<DynamicDto> alldynamics = new ArrayList<>();
        List<DynamicDto> fianlResultList =new ArrayList<>();

        // 获取当前openId 的所有匹配过的用户（Set 为了去重）
        Set<MatchingResult> matchingResultSet = getMatchingUser(openId);
        if (!CollectionUtils.isEmpty(matchingResultSet)) {

            // 将Set 转为List(为了按index取元素)
            List<MatchingResult> lst = new ArrayList<MatchingResult>(matchingResultSet);

            // 遍历所有匹配过的用户
            for (int i = 0; i < lst.size(); i++) {   // 获取该用户的所有Dynamic.
                String targetOpenId = lst.get(i).getTargetOpenId();
                String groupId = lst.get(i).getGroupId();
                Double matchScore = lst.get(i).getMatchScore();
                String targetNickName = lst.get(i).getTargetNickName();
                List<Dynamics> dynamicsList = dynamicsMapper.selectByOpenId(targetOpenId);
                for (int j = 0; j < dynamicsList.size(); j++) {
                    Dynamics dynamics = dynamicsList.get(j);
                    DynamicDto dynamicDto = new DynamicDto();
                    BeanUtils.copyProperties(dynamics, dynamicDto);
                    dynamicDto.setGroupId(groupId);
                    dynamicDto.setMatchResult(matchScore);
                    //获取对方用户nickname
                    dynamicDto.setNickName(targetNickName);
                    //获取情感值
                    dynamicDto.setEmotionRate(dynamics.getEmotionRate());
                    dynamicDto.setId(dynamics.getId());
                    alldynamics.add(dynamicDto);
                }
            }

            //获取用户最新动态的情感值
            int emotionRate = -2;
            Dynamics newestDynamic = dynamicsMapper.queryNewestByOpenId(openId);
            if(newestDynamic != null){
                emotionRate = newestDynamic.getEmotionRate();
            }

            // 排序
            Collections.sort(alldynamics, new MyComparator());
            if (alldynamics.size() < 4) {
                resultList.addAll(alldynamics);
            } else {
                //  this is XJN algorithm . knee it
                int index = 1;
                String openId0 = alldynamics.get(0).getOpenId();
                resultList.add(alldynamics.get(0));
                for (int i = 1; i < alldynamics.size(); i++) {
                    // 取前21条
                    if (resultList.size() > 21) {
                        break;
                    }
                    //添加对情感的比较
                    if (!alldynamics.get(i).getOpenId().equals(openId0) &&
                            (emotionRate == -2 ? true : alldynamics.get(i).getEmotionRate() == emotionRate)) {
                        //遇到不同的openId更新openId0的值
                        openId0 = alldynamics.get(i).getOpenId();
                        resultList.add(alldynamics.get(i));
                        index = 1;
                    } else if (alldynamics.get(i).getOpenId().equals(openId0) &&
                            (emotionRate == -2 ? true : alldynamics.get(i).getEmotionRate() == emotionRate) && index < 3) {
                        index++;
                        resultList.add(alldynamics.get(i));
                    } else if (alldynamics.get(i).getOpenId().equals(openId0) &&
                            (emotionRate == -2 ? true : alldynamics.get(i).getEmotionRate() == emotionRate) && index >= 3) {
                        index++;
                    }
                }
            }
        }
        // 补充模板
        Integer realDataSize = resultList.size();
        int offset = 22 - realDataSize;
        if(offset > 0){
            List<DynamicsTemplate> templateList = dynamicsTemplateMapper.queryTemplateByNum(offset);
            for(DynamicsTemplate dynamicsTemplate : templateList){
                DynamicDto dynamics = new DynamicDto();
                //模板的ID设置为负数。
                dynamics.setId((0-dynamicsTemplate.getId()));
                dynamics.setOpenId(Constant.DEFAULTOPENID);
                dynamics.setDiary(dynamicsTemplate.getDiary());
                dynamics.setPoster(dynamicsTemplate.getPoster());
                dynamics.setCreateTime(dynamicsTemplate.getCreateTime());
                dynamics.setGroupId(Constant.DEFAULTGROUPID);
                dynamics.setNickName(Constant.DEFAULTNICKNAME);
                resultList.add(dynamics);
            }
        }

        // 获取当前用户不想看到的所有动态
        List<DynamicsBlacklist> dynamicsBlacklists =dynamicsBlacklistMapper.selectBlacklistByOpenId(openId);
        if(CollectionUtils.isEmpty(dynamicsBlacklists)){
            return resultList;
        }
        List<Integer> blackList = new ArrayList<>();
        for(int j=0;j<dynamicsBlacklists.size();j++)
        {
            blackList.add(dynamicsBlacklists.get(j).getDynamicId());
        }
        for(int i=0;i<resultList.size();i++)
        {
            DynamicDto dynamicDto = resultList.get(i);
            // 获取当前动态的ID
            Integer id = dynamicDto.getId();
            // 和黑名单中的ID对比 如果不存在加入最后的列表
           if(!blackList.contains(id))
           {
               fianlResultList.add(dynamicDto);
           }
        }
        return fianlResultList;
    }

    @Override
    public Integer insertDynamics(Dynamics dynamics) {
        //海报和日记不能为null
        if (StringUtils.isEmpty(dynamics.getDiary()) || StringUtils.isEmpty(dynamics.getPoster())) {
            return 0;
        }
        return dynamicsMapper.insertDynamics(dynamics);
    }

    @Override
    public JSONObject selectAllDynamics(int pageNo, int pageSize, String openId) {
        PageHelper.startPage(pageNo, pageSize);
        PageInfo<Dynamics> pageinfo = new PageInfo<>(dynamicsMapper.selectAllDynamics(openId));
        JSONObject jsonObject = new JSONObject();
        //jsonObject.put("openId", openId);
        jsonObject.put("total", pageinfo.getTotal());
        jsonObject.put("size", pageinfo.getSize());
        jsonObject.put("pageNum", pageinfo.getPageNum());
        jsonObject.put("pageSize", pageinfo.getPageSize());
        JSONArray jsonArray = new JSONArray();
        List<Dynamics> list = pageinfo.getList();
        if (!CollectionUtils.isEmpty(list)) {
            for (Dynamics dynamics : list) {
                if (dynamics != null) {
                    JSONObject json = new JSONObject();
                    json = setDynamicReturnInfo(json, dynamics);
                    //json.put("openId", dynamics.getOpenId());
                    json.put("id", dynamics.getId());
                    json.put("secret", dynamics.getSecret());
                    jsonArray.add(json);
                }
            }
            jsonObject.put("diarys", jsonArray);
            return jsonObject;
        }
        return null;
    }

    @Override
    public boolean deleteDiary(Integer id) {
        Integer num = dynamicsMapper.deleteDiary(id);
        if (num > 0) {
            return true;
        }
        return false;
    }

    /**
     * 此方法用于查询用户发表的最新日记，如果没有查到则返回系统提供的模板
     *
     * @param openId
     * @return 返回具体的日记JSON数据
     */
    @Override
    public JSONObject getNewestDynamics(String openId) {
        JSONObject jsonObject = new JSONObject();
        Dynamics dynamics = dynamicsMapper.queryNewestByOpenId(openId);
        if (dynamics != null) {
            jsonObject = setDynamicReturnInfo(jsonObject, dynamics);
            jsonObject.put("secret", dynamics.getSecret());
            jsonObject.put("id",dynamics.getId());
            return jsonObject;
        } else {
            DynamicsTemplate dynamicsTemplate = dynamicsTemplateMapper.queryTemplate();
            if (dynamicsTemplate != null) {
                jsonObject = setDynamicTemplateReturnInfo(jsonObject, dynamicsTemplate);
                return jsonObject;
            } else {
                return null;
            }
        }
    }

    private JSONObject setDynamicTemplateReturnInfo(JSONObject jsonObject, DynamicsTemplate dynamicsTemplate) {
        jsonObject.put("diary", dynamicsTemplate.getDiary());
        jsonObject.put("poster", dynamicsTemplate.getPoster());
        jsonObject.put("createTime", timeTransferUtil.date2String(dynamicsTemplate.getCreateTime()));
        jsonObject.put("date", timeTransferUtil.getYMD((String) jsonObject.get("createTime")));
        jsonObject.put("month", timeTransferUtil.getMonth((String) jsonObject.get("createTime")));
        jsonObject.put("day", timeTransferUtil.getDay((String) jsonObject.get("createTime")));
        return jsonObject;
    }

    private JSONObject setDynamicReturnInfo(JSONObject jsonObject, Dynamics dynamics) {
        jsonObject.put("diary", dynamics.getDiary());
        jsonObject.put("poster", dynamics.getPoster());
        jsonObject.put("createTime", timeTransferUtil.date2String(dynamics.getCreateTime()));
        jsonObject.put("date", timeTransferUtil.getYMD((String) jsonObject.get("createTime")));
        jsonObject.put("month", timeTransferUtil.getMonth((String) jsonObject.get("createTime")));
        jsonObject.put("day", timeTransferUtil.getDay((String) jsonObject.get("createTime")));
        return jsonObject;
    }

    @Override
    public Set<MatchingResult> getMatchingUser(String openId) {
        //获取该用户参加的所有群组
        List<String> groupIdList = groupRelationMapper.getGroupIdsByOpenId(openId);
        if (CollectionUtils.isEmpty(groupIdList)) {
            return null;
        }
        Set<MatchingResult> matchingResults = new TreeSet<>();
        //遍历每一个群组Id，遍历所有参加的用户
        for (String groupId : groupIdList) {
            //通过groupId获取该群组中已经参加的所有群员
            List<String> openIds = groupRelationMapper.getOpenIdsByGroupId(groupId);
            //当前群只有自己一个人，则不进行匹配
            if (openIds.size() == 1) {
                continue;
            }
            for (String groupMemberIds : openIds) {
                //如果是本人则跳过
                if (groupMemberIds.equals(openId)) {
                    continue;
                }
                User targetUser = userMapper.queryUserByOpenId(groupMemberIds);
                User selfUser = userMapper.queryUserByOpenId(openId);
                if (selfUser==null||targetUser==null){
                    continue;
                }
                // 性别没填 跳过
                if (targetUser.getGender() == null || selfUser.getGender() == null) {
                    continue;
                }
                //如果性别相同则跳过
                if (targetUser.getGender().equals(selfUser.getGender())) {
                    continue;
                }
                MatchingResult result = matchService.matchUser(selfUser, targetUser);
                result.setTargetAvatarUrl(targetUser.getAvatarUrl());
                result.setTargetNickName(targetUser.getNickName());
                result.setTargetOpenId(targetUser.getOpenId());
                result.setGroupId(groupId);
                matchingResults.add(result);
            }
        }

        //将每个群的匹配结果Map根据openId存放到Redis缓存中,
        //暂时没用
        String jsonString = JSON.toJSONString(matchingResults);
        redisUtil.set(Constant.USERKEY, jsonString);
        return matchingResults;
    }

    @Override
    public JSONObject getMatchedUserDiary(String targetOpenId, Integer nowPage, Integer itemPerPage){
        PageHelper.startPage(nowPage, itemPerPage);
        PageInfo<Dynamics> pageinfo = new PageInfo<>(dynamicsMapper.selectMatchedUserDiary(targetOpenId));
        String nickname = userService.getNickNameByOpenId(targetOpenId);
        JSONObject jsonObject = new JSONObject();
        if(!StringUtils.isEmpty(nickname)){
            jsonObject.put("nickname", nickname);
        }else{
            jsonObject.put("nickname", null);
        }
        jsonObject.put("total", pageinfo.getTotal());
        jsonObject.put("size", pageinfo.getSize());
        jsonObject.put("pageNum", pageinfo.getPageNum());
        jsonObject.put("pageSize", pageinfo.getPageSize());
        JSONArray jsonArray = new JSONArray();
        List<Dynamics> list = pageinfo.getList();
        if (!CollectionUtils.isEmpty(list)) {
            for (Dynamics dynamics : list) {
                if (dynamics != null) {
                    JSONObject json = new JSONObject();
                    json = setDynamicReturnInfo(json, dynamics);
                    json.put("id", dynamics.getId());
                    jsonArray.add(json);
                }
            }
            jsonObject.put("diarys", jsonArray);
            return jsonObject;
        }
        return null;
    }

    @Override
    public boolean addDynamicBlackList(String openId, Integer dynamicId) {
        if(dynamicId==null || openId==null)
        {
            return false;
        }
        Integer num = dynamicsBlacklistMapper.addBlackList(openId,dynamicId);
        if(num==1)
        {
            return true;
        }
        return false;
    }

}
