package com.mini10.miniserver.mapper;

import com.mini10.miniserver.model.SpecialRequirement;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
@Component
public interface SpecialRequirementMapper extends Mapper<SpecialRequirement> {

    /**
     * 根据openId获取该用户的特殊需求
     * @param openId
     * @return
     */
    SpecialRequirement getRequireByOpenId(@Param("openId") String openId);

    void addSpecialRequirement(SpecialRequirement specialRequirement);

    void updateSpecialRequirementByOpenId(SpecialRequirement specialRequirement);
}