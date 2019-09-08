package com.mini10.miniserver.service.impl;

import com.alibaba.fastjson.JSON;
import com.mini10.miniserver.common.Constant;
import com.mini10.miniserver.common.utils.RedisUtil;
import com.mini10.miniserver.common.utils.RequestUtil;
import com.mini10.miniserver.mapper.*;
import com.mini10.miniserver.model.*;
import com.mini10.miniserver.model.param.MatchGroupResult;
import com.mini10.miniserver.model.param.MatchTagSimilarity;
import com.mini10.miniserver.model.param.MatchingResult;
import com.mini10.miniserver.service.GroupService;
import com.mini10.miniserver.service.MatchService;
import com.mini10.miniserver.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.mini10.miniserver.common.Constant.SpecialCode.*;



/**
 * @Author ChenShaoJie
 * @Date 2019-07-22 09:15
 */
@Service
public class MatchServiceImpl implements MatchService {

    private static Logger log = LoggerFactory.getLogger(MatchServiceImpl.class);

    @Autowired
    private SpecialRequirementMapper specialRequirementMapper;
    @Autowired
    private GroupRelationMapper groupRelationMapper;
    @Autowired
    private RequirementTagMapper requirementTagMapper;
    @Autowired
    private ConditionTagMapper conditionTagMapper;
    @Autowired
    private RequestUtil requestUtil;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private VirtualGroupRelationMapper virtualGroupRelationMapper;
    private int NECESSARYSCORE_MAX = 70; //必须条件最高分
    private int NECESSARYSCORE = 10; //必须条件加分

    private int REQUIRESCORE_MAX = 20; //非必须条件最高分
    private int REQUIRESCORE = 4; //非必需条件加分

    private int PERFECTMATCHSCORE = 60; //完美匹配的阈值

    private int NECESSARY_FLAG = 1;
    private int REQUIRE_FLAG = 0;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public List<MatchGroupResult> getMatchResult(String openId) {
        //根据群组Id来区分所有的匹配结果
        Map<String, Object> groupMatchMap = new HashMap<>();
        List<MatchGroupResult> matchGroupResults = new ArrayList<>();
        //获取该用户参加的所有群组
        List<String> groupIdList = groupRelationMapper.getGroupIdsByOpenId(openId);
        if (CollectionUtils.isEmpty(groupIdList)) {
            return null;
        }
        User selfUser = userMapper.queryUserByOpenId(openId);
        if (selfUser == null) {
            log.info("匹配时无法获取到用户本身");
            return null;
        }

        //遍历每一个群组Id，遍历所有参加的用户
        for (String groupId : groupIdList) {
            //该群组内参加匹配的异性人数
            int oppositeSexNumber = 0;
            log.info("当前所在群组ID:" + groupId);
            List<MatchingResult> matchingResults = new ArrayList<>();
            //通过groupId获取该群组中已经参加的所有群员
            List<String> openIds = groupRelationMapper.getOpenIdsByGroupId(groupId);
            MatchGroupResult matchGroupResult = new MatchGroupResult();
            matchGroupResult.setGroupId(groupId);
            matchGroupResult.setMemberNumber(openIds.size());

            //当前群只有自己一个人，则不进行匹配
            if (CollectionUtils.isEmpty(openIds) || openIds.size() == 1) {
                matchGroupResult.setPerfectMatchNumber(0);
                matchGroupResults.add(matchGroupResult);
                continue;
            }
            for (String groupMemberIds : openIds) {
                //如果是本人则跳过
                if (groupMemberIds.equals(openId)) {
                    continue;
                }
                User targetUser = userMapper.queryUserByOpenId(groupMemberIds);
                //用户为空则跳过
                if (targetUser == null) {
                    continue;
                }

                //如果性别相同则跳过
                if (targetUser.getGender() == null || selfUser.getGender() == null) {
                    //  log.info("对方不男不女，无法匹配");
                    continue;
                }
                if (targetUser.getGender().equals(selfUser.getGender())) {
                    //  log.info("性别相同，不能匹配");
                    continue;
                }
                oppositeSexNumber++;
                MatchingResult result = matchUser(selfUser, targetUser);
                result.setGroupId(groupId);
                //  result.setGroupName(groupInfo.getGroupName());
                result.setTargetAvatarUrl(targetUser.getAvatarUrl());
                result.setTargetNickName(targetUser.getNickName());
                result.setTargetOpenId(targetUser.getOpenId());
                result.setSelfAvatarUrl(selfUser.getAvatarUrl());
                result.setSelfNickName(selfUser.getNickName());
                result.setSelfOpenId(selfUser.getOpenId());

                if (result.getMatchScore() >= PERFECTMATCHSCORE) {
                    int num = matchGroupResult.getPerfectMatchNumber();
                    matchGroupResult.setPerfectMatchNumber(num + 1);
                }
                matchingResults.add(result);

            }
            matchGroupResult.setOppositeSexNumber(oppositeSexNumber);
            groupMatchMap.put(groupId, matchingResults);
            matchGroupResults.add(matchGroupResult);
        }

        //将每个群的匹配结果Map根据openId存放到Redis缓存中
        String jsonString = JSON.toJSONString(groupMatchMap);
        redisUtil.hset(Constant.REDIS_DETAIL_GROUP_RESULT, openId, jsonString);

        return matchGroupResults;
    }

    /**
     * 获取虚拟群的匹配结果
     *
     * @param openId
     * @return
     */
    @Override
    public List<MatchGroupResult> getVirtualMatchResult(String openId, String longitude, String latitude) {
        //根据群组Id来区分所有的匹配结果
        Map<String, Object> groupMatchMap = new HashMap<>();
        List<MatchGroupResult> matchGroupResults = new ArrayList<>();
        //获取该用户参加的所有群组
        VirtualGroupInfo virtualGroupInfo = groupService.getVirtualGroupByPosition(openId, longitude, latitude);
        if (virtualGroupInfo == null) {
            log.info("无法获取到地理位置群组信息");
            return null;
        }
        String virtualGroupId = virtualGroupInfo.getVirtualGroupId();
        String virtualGroupName = virtualGroupInfo.getVirtualGroupName();
        if (StringUtils.isEmpty(virtualGroupId)) {
            log.info("无法获取到地理位置群组的Id");
            return null;
        }
        User selfUser = userMapper.queryUserByOpenId(openId);
        if (selfUser == null) {
            log.info("匹配时无法获取到用户本身");
            return null;
        }

        //遍历每一个群组Id，遍历所有参加的用户

        //该群组内参加匹配的异性人数
        int oppositeSexNumber = 0;
        log.info("当前所在群组ID:" + virtualGroupId);
        List<MatchingResult> matchingResults = new ArrayList<>();
        //通过groupId获取该群组中已经参加的所有群员
        List<String> openIds = virtualGroupRelationMapper.getOpenIdsByGroupId(virtualGroupId);
        MatchGroupResult matchGroupResult = new MatchGroupResult();
        matchGroupResult.setGroupId(virtualGroupId);
        matchGroupResult.setMemberNumber(openIds.size());
        matchGroupResult.setGroupName(virtualGroupName);
        //当前群只有自己一个人，则不进行匹配
        if (CollectionUtils.isEmpty(openIds) || openIds.size() == 1) {
            matchGroupResult.setPerfectMatchNumber(0);
            matchGroupResults.add(matchGroupResult);
            return null;
        }
        for (String groupMemberIds : openIds) {
            //如果是本人则跳过
            if (groupMemberIds.equals(openId)) {
                continue;
            }
            User targetUser = userMapper.queryUserByOpenId(groupMemberIds);
            //用户为空则跳过
            if (targetUser == null) {
                continue;
            }
            //如果性别相同则跳过
            if (targetUser.getGender() == null || selfUser.getGender() == null) {
                continue;
            }
            if (targetUser.getGender().equals(selfUser.getGender())) {
                continue;
            }
            oppositeSexNumber++;
            MatchingResult result = matchUser(selfUser, targetUser);
            result.setGroupId(virtualGroupId);
            //  result.setGroupName(groupInfo.getGroupName());
            result.setTargetAvatarUrl(targetUser.getAvatarUrl());
            result.setTargetNickName(targetUser.getNickName());
            result.setTargetOpenId(targetUser.getOpenId());
            result.setSelfAvatarUrl(selfUser.getAvatarUrl());
            result.setSelfNickName(selfUser.getNickName());
            result.setSelfOpenId(selfUser.getOpenId());

            if (result.getMatchScore() >= PERFECTMATCHSCORE) {
                int num = matchGroupResult.getPerfectMatchNumber();
                matchGroupResult.setPerfectMatchNumber(num + 1);
            }
            matchingResults.add(result);

        }
        matchGroupResult.setOppositeSexNumber(oppositeSexNumber);
        groupMatchMap.put(virtualGroupId, matchingResults);
        matchGroupResults.add(matchGroupResult);
        //将每个群的匹配结果Map根据openId存放到Redis缓存中
        String jsonString = JSON.toJSONString(groupMatchMap);
        redisUtil.hset(Constant.REDIS_DETAIL_VIRTUAL_GROUP_RESULT, openId, jsonString);
        return matchGroupResults;
    }

    /**
     * 匹配两个人的个人信息匹配度
     *
     * @param selfUser   用户的User对象，需要所有数据
     * @param targetUser 对方的User对象，需要所有数据
     * @return
     */
    @Override
    public MatchingResult matchUser(User selfUser, User targetUser) {
        MatchingResult result = new MatchingResult();
        double selfToTargetNecessaryScore = 0;
        double selfToTargetRequireScore = 0;
        double targetToSelfNecessaryScore = 0;
        double targetToSelfRequireScore = 0;
        double selfPersonInformation = 0;
        double targetPersonInformation = 0;
        /**
         * 对方能够满足自己条件的总分
         */
        double totalSelfToTargetScore = 0;
        /**
         * 自己能够满足对方条件的总分
         */
        double totalTargetToSelfScore = 0;

        String selfOpenId = selfUser.getOpenId();
        String targetOpenId = targetUser.getOpenId();
        List<RequirementTag> selfNecessaryTags = requirementTagMapper.getNecessaryTagsByOpenId(selfOpenId);
        List<RequirementTag> selfRequireTags = requirementTagMapper.getRequirementTagsByOpenId(selfOpenId);
        List<ConditionTag> selfConditionTags = conditionTagMapper.getConditionTagsByOpenId(selfOpenId);
        List<RequirementTag> targetNecessaryTags = requirementTagMapper.getNecessaryTagsByOpenId(targetOpenId);
        List<RequirementTag> targetRequireTags = requirementTagMapper.getRequirementTagsByOpenId(targetOpenId);
        List<ConditionTag> targetConditionTags = conditionTagMapper.getConditionTagsByOpenId(targetOpenId);
        selfToTargetNecessaryScore = getMatchScoreBySimilarity(selfNecessaryTags, NECESSARY_FLAG,
                selfUser, targetUser, targetConditionTags,
                result.getQualifiedSelfNecessaryTags());
        selfToTargetRequireScore = getMatchScoreBySimilarity(selfRequireTags, REQUIRE_FLAG,
                selfUser, targetUser, targetConditionTags,
                result.getQualifiedSelfRequireTags());
        //个人信息的分数，总分100，*0.3的系数
        selfPersonInformation = userService.getUserScore(selfOpenId) * 0.1;
        totalSelfToTargetScore = selfToTargetNecessaryScore + selfToTargetRequireScore + selfPersonInformation;
        totalSelfToTargetScore = totalSelfToTargetScore > 100 ? 100 : totalSelfToTargetScore;

        targetToSelfNecessaryScore = getMatchScoreBySimilarity(targetNecessaryTags, NECESSARY_FLAG,
                targetUser, selfUser, selfConditionTags,
                result.getQualifiedTargetNecessaryTags());
        targetToSelfRequireScore = getMatchScoreBySimilarity(targetRequireTags, REQUIRE_FLAG,
                targetUser, selfUser, selfConditionTags,
                result.getQualifiedTargetRequireTags());
        targetPersonInformation = userService.getUserScore(targetOpenId) * 0.1;
        totalTargetToSelfScore = targetToSelfNecessaryScore + targetToSelfRequireScore + targetPersonInformation;
        totalTargetToSelfScore = totalTargetToSelfScore > 100 ? 100 : totalTargetToSelfScore;

        double matchScore = 0;
        matchScore = (totalSelfToTargetScore + totalTargetToSelfScore) / 2;
       /* if (selfUser.getGender().equals(MALE)) {
            matchScore = totalSelfToTargetScore * MToF + totalTargetToSelfScore * FToM;
        } else if (selfUser.getGender().equals(FEMAL)) {
            matchScore = totalSelfToTargetScore * FToM + totalTargetToSelfScore * MToF;
        }*/
        result.setMatchScore(matchScore);
        //计算得出的匹配标签结果中可能有重复，要进行去重
        List<Tag> qualifiedSelfTags = result.getQualifiedSelfNecessaryTags();
        List<Tag> qualifiedTargetTags = result.getQualifiedTargetNecessaryTags();
        List<Tag> distinctSelfList = qualifiedSelfTags.stream()
                .collect(
                        Collectors.collectingAndThen(
                                Collectors.toCollection(() -> new TreeSet<Tag>(Comparator.comparing(Tag::getTagName))), ArrayList::new)
                );
        List<Tag> distinctTargetList = qualifiedTargetTags.stream()
                .collect(
                        Collectors.collectingAndThen(
                                Collectors.toCollection(() -> new TreeSet<Tag>(Comparator.comparing(Tag::getTagName))), ArrayList::new)
                );
        result.setQualifiedSelfNecessaryTags(distinctSelfList);
        result.setQualifiedTargetNecessaryTags(distinctTargetList);
        return result;
    }

    /**
     * @param requireTags         己方的需求标签
     * @param flag                标识是对必要条件进行匹配还是非必要条件
     * @param selfUser            己方用户
     * @param targetUser          对方用户
     * @param targetConditionTags 对方用户的详细需求
     * @param qualifiedTags       对方符合己方的标签（根据传入的参数分为必要和不必要）
     * @return
     */
    public double getMatchScoreBySimilarity(List<RequirementTag> requireTags,
                                            int flag,
                                            User selfUser,
                                            User targetUser,
                                            List<ConditionTag> targetConditionTags,
                                            List<Tag> qualifiedTags
    ) {
        //如果需求条件为空，则直接返回固定分值  0分
        if (CollectionUtils.isEmpty(requireTags)) {
            return 0;
            /*if (flag == NECESSARY_FLAG) {
                return NECESSARYSCORE_MAX;
            } else if (flag == REQUIRE_FLAG) {
                return REQUIRESCORE_MAX;
            }*/
        }
        //如果对方没有填写自身标签，则直接给0分
        if (CollectionUtils.isEmpty(targetConditionTags)) {
            //   log.info("没有填写自身条件标签,此项标签匹配得0分");
            return 0;
        }
        double totalScore = 0;
        double addScore = 0;
        if (flag == NECESSARY_FLAG) {
            //addScore = NECESSARYSCORE_MAX / requireTags.size();
            addScore = NECESSARYSCORE;
        } else if (flag == REQUIRE_FLAG) {
            // addScore = REQUIRESCORE_MAX / requireTags.size();
            addScore = REQUIRESCORE;
            //  int num = 6-
        }

        //遍历普通标签需求,计算两个标签之间的相似度，并存储到一个List中作为最初结果集
        List<MatchTagSimilarity> matchTagSimilarities = new ArrayList<>();
        //获取特殊数值需求的分数，并获取标签相似度的结果集
        totalScore += TraversingSpecialRequirement(requireTags,
                targetConditionTags, matchTagSimilarities,
                addScore, targetUser, selfUser, qualifiedTags);

        //如果初始结果集为空，表明没有进行标签相似度匹配，则返回0分
        if (CollectionUtils.isEmpty(matchTagSimilarities)) {
            //log.info("没有匹配结果集，此项标签匹配得0分");
            return 0;
        }
        //对所有标签的相似度结果进行排序，假设requirementTag有n个，conditionTags有m个，则总结果有n*每个
        if (!CollectionUtils.isEmpty(matchTagSimilarities)) {
            Collections.sort(matchTagSimilarities, new Comparator<MatchTagSimilarity>() {
                @Override
                public int compare(MatchTagSimilarity o1, MatchTagSimilarity o2) {
                    double score1 = o1.getSimilarity();
                    double score2 = o2.getSimilarity();
                    if (score1 > score2) {
                        return -1;
                    } else if (score1 == score2) {
                        return 0;
                    } else {
                        return 1;
                    }
                }
            });
        }
        //对最初结果集进行清洗，获得最终的标签相似度结果集
        List<MatchTagSimilarity> tagSimilarityResultTags = new ArrayList<>();
        for (int i = 0; i < requireTags.size(); ) {
            //先获取结果集合中分数最大的一组标签结果，并从结果集合中删除带有这个需求标签名称或条件标签名称的结果
            MatchTagSimilarity matchTagSimilarity = matchTagSimilarities.get(i);
            String requirementTagName = matchTagSimilarity.getRequirementTag().getRequireTagName();
            String conditionTagName = matchTagSimilarity.getConditionTag().getConditionTagName();
            //遍历结果集,进行删除操作
            Iterator<MatchTagSimilarity> iterator1 = matchTagSimilarities.iterator();
            while (iterator1.hasNext()) {
                MatchTagSimilarity similarity = iterator1.next();
                if (similarity.getRequirementTag().getRequireTagName().equals(requirementTagName) ||
                        similarity.getConditionTag().getConditionTagName().equals(conditionTagName)) {
                    iterator1.remove();
                }
            }
            //将目前最大结果保存起来
            tagSimilarityResultTags.add(matchTagSimilarity);
            //如果结果集已空则跳出
            if (matchTagSimilarities.size() == 0) {
                break;
            }
        }

        //遍历最终结果集，获取被匹配的条件标签以及相似度,并根据标签进行评分
        for (MatchTagSimilarity matchTagSimilarity : tagSimilarityResultTags) {
            ConditionTag conditionTag = matchTagSimilarity.getConditionTag();
            double similarity = matchTagSimilarity.getSimilarity();
            totalScore += similarity * addScore;
            Tag tag = new Tag();
            tag.setTagName(conditionTag.getConditionTagName());
            tag.setId(conditionTag.getConditionTagId());
            qualifiedTags.add(tag);
        }
        //如果这是必要标签的需求，且在标签匹配后分数不为0，则再加40分
        if (flag == NECESSARY_FLAG && totalScore != 0) {
            totalScore += 40;
        }
        return totalScore;
    }

    /**
     * 遍历用户的需求标签，先判断是否是特殊数值需求标签，如果是，则不进行标签相似度判断，如果不是，则进行标签相似度获取并存入结果集
     *
     * @param requireTags
     * @param addScore
     * @param targetUser
     * @param selfUser
     * @return 返回特殊数值需求标签的匹配分数，标签相似度的分数不在此函数中获取
     */
    public double TraversingSpecialRequirement(List<RequirementTag> requireTags,
                                               List<ConditionTag> targetConditionTags,
                                               List<MatchTagSimilarity> matchTagSimilarities,
                                               double addScore,
                                               User targetUser,
                                               User selfUser,
                                               List<Tag> qualifiedTags) {
        double totalScore = 0;
        //先对用户的需求标签进行数值标签遍历
        Iterator<RequirementTag> iterator = requireTags.iterator();
        while (iterator.hasNext()) {
            RequirementTag requirementTag = iterator.next();
            Integer tagId = requirementTag.getRequireTagId();
            String tagName = requirementTag.getRequireTagName();
            if (StringUtils.isEmpty(tagName)) {
                continue;
            }
            if (tagName.equals(HEIGH170)) {
                Integer targetHeigh = targetUser.getHeight();
                iterator.remove();
                if (targetHeigh != null && targetHeigh >= 170) {
                    totalScore += addScore;
                    Tag tag = new Tag();
                    tag.setTagName(tagName);
                    tag.setId(tagId);
                    qualifiedTags.add(tag);
                }
                continue;
            } else if (tagName.equals(HEIGH175)) {
                Integer targetHeigh = targetUser.getHeight();
                iterator.remove();
                if (targetHeigh != null && targetHeigh >= 175) {
                    totalScore += addScore;
                    Tag tag = new Tag();
                    tag.setTagName(tagName);
                    tag.setId(tagId);
                    qualifiedTags.add(tag);
                }
                continue;
            } else if (tagName.equals(HEIGH180)) {
                Integer targetHeigh = targetUser.getHeight();
                iterator.remove();
                if (targetHeigh != null && targetHeigh >= 180) {
                    totalScore += addScore;
                    Tag tag = new Tag();
                    tag.setTagName(tagName);
                    tag.setId(tagId);
                    qualifiedTags.add(tag);
                }
                continue;
            } else if (tagName.equals(HEIGH158)) {
                Integer targetHeigh = targetUser.getHeight();
                iterator.remove();
                if (targetHeigh != null && targetHeigh >= 158) {
                    totalScore += addScore;
                    Tag tag = new Tag();
                    tag.setTagName(tagName);
                    tag.setId(tagId);
                    qualifiedTags.add(tag);
                }
                continue;
            } else if (tagName.equals(HEIGH162)) {
                Integer targetHeigh = targetUser.getHeight();
                iterator.remove();
                if (targetHeigh != null && targetHeigh >= 162) {
                    totalScore += addScore;
                    Tag tag = new Tag();
                    tag.setTagName(tagName);
                    tag.setId(tagId);
                    qualifiedTags.add(tag);
                }
                continue;
            } else if (tagName.equals(HEIGH168)) {
                Integer targetHeigh = targetUser.getHeight();
                iterator.remove();
                if (targetHeigh != null && targetHeigh >= 168) {
                    totalScore += addScore;
                    Tag tag = new Tag();
                    tag.setTagName(tagName);
                    tag.setId(tagId);
                    qualifiedTags.add(tag);
                }
                continue;
            } else if (tagName.equals(DEGREE_ONE)) {
                //判断对方学历是否本科以上
                Integer targetDegree = targetUser.getDegree();
                iterator.remove();
                if (targetDegree != null && (targetDegree.equals(DEGREE_ONE_CODE) ||
                        DEGREE_TWO_CODE.equals(targetDegree) || DEGREE_THREE_CODE.equals(targetDegree))) {
                    Tag tag = new Tag();
                    tag.setTagName(tagName);
                    tag.setId(tagId);
                    qualifiedTags.add(tag);
                    totalScore += addScore;
                }
                continue;
            } else if (tagName.equals(DEGREE_TWO)) {
                //判断对方学历是否研究生
                Integer targetDegree = targetUser.getDegree();
                iterator.remove();
                if (targetDegree != null && (DEGREE_TWO_CODE.equals(targetDegree)
                        || DEGREE_THREE_CODE.equals(targetDegree))) {
                    Tag tag = new Tag();
                    tag.setTagName(tagName);
                    tag.setId(tagId);
                    qualifiedTags.add(tag);
                    totalScore += addScore;
                }
                continue;
            } else if (tagName.equals(DEGREE_THREE)) {
                //判断对方学历是否博士生
                Integer targetDegree = targetUser.getDegree();
                iterator.remove();
                if (targetDegree != null && DEGREE_THREE_CODE.equals(targetDegree)) {
                    Tag tag = new Tag();
                    tag.setTagName(tagName);
                    tag.setId(tagId);
                    qualifiedTags.add(tag);
                    totalScore += addScore;
                }
                continue;
            } else if (tagName.equals(SAMEPROVINCE)) {
                String targetProvince = targetUser.getProvince();
                String selfProvince = selfUser.getProvince();
                iterator.remove();
                if (!StringUtils.isEmpty(targetProvince) && !StringUtils.isEmpty(selfProvince) &&
                        targetProvince.equals(selfProvince)) {
                    Tag tag = new Tag();
                    tag.setTagName(tagName);
                    tag.setId(tagId);
                    qualifiedTags.add(tag);
                    totalScore += addScore;
                }
            }
            //进行特殊标签遍历后，再对标签进行相似度遍历，获取相似度结果集
            getMatchTagSimilarities(requirementTag, targetConditionTags, matchTagSimilarities);
        }

        return totalScore;
    }


    /**
     * 获取用户的一个需求标签 -- 匹配对象的多个自身条件标签的相似度结果集
     *
     * @param requirementTag
     * @param targetConditionTags
     * @param matchTagSimilarities
     */
    public void getMatchTagSimilarities(RequirementTag requirementTag,
                                        List<ConditionTag> targetConditionTags,
                                        List<MatchTagSimilarity> matchTagSimilarities
    ) {
        for (ConditionTag conditionTag : targetConditionTags) {
            String requirementTagName = requirementTag.getRequireTagName();
            String conditionTagName = conditionTag.getConditionTagName();
            double similarity = 0;
            Object similarityObject = redisUtil.hget(requirementTagName, conditionTagName);
            if (similarityObject != null) {
                similarity = Double.parseDouble((String) similarityObject);
            } else if (requirementTagName.equals(conditionTagName)) {
                similarity = 1;
                redisUtil.hset(requirementTagName, conditionTagName, String.valueOf(similarity));
            } else if (("不" + requirementTagName).equals(conditionTagName) || ("不" + conditionTagName).equals(requirementTagName) ||
                    ("非" + requirementTagName).equals(conditionTagName) || ("非" + conditionTagName).equals(requirementTagName)) {
                similarity = 0;
                redisUtil.hset(requirementTagName, conditionTagName, String.valueOf(similarity));
            } else {
                try {
                    similarity = requestUtil.getSimilarity(requirementTagName,conditionTagName);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }

                if (similarity > 1) {
                    similarity = 1;
                }
                if (similarity < 0) {
                    similarity = 0;
                }
                redisUtil.hset(requirementTagName, conditionTagName, String.valueOf(similarity));
            }
            MatchTagSimilarity matchTagSimilarity = new MatchTagSimilarity(requirementTag, conditionTag, similarity);
            matchTagSimilarities.add(matchTagSimilarity);
        }
    }
}
