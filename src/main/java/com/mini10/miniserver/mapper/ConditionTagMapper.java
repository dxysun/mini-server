package com.mini10.miniserver.mapper;

import com.mini10.miniserver.model.ConditionTag;
import com.mini10.miniserver.model.Tag;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Component
public interface ConditionTagMapper extends Mapper<ConditionTag> {
    Integer addConditionTags(@Param("conditionTags") List<ConditionTag> conditionTagArray);

    List<Integer> getConditionTagIdsByOpenId(@Param("openId") String openId);

    List<ConditionTag> getConditionTagsByOpenId(@Param("openId") String openId);

    /**
     * 根据openId删除ConditionTag
     * @param openId
     * @return
     */
    Integer deleteConditionTagsByOpenId(@Param("openId") String openId);
}