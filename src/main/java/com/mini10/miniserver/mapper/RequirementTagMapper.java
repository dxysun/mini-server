package com.mini10.miniserver.mapper;

import com.mini10.miniserver.model.RequirementTag;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Component
public interface RequirementTagMapper extends Mapper<RequirementTag> {

    /**
     * 设置对方的非必要标签
     * @param tags
     */
     void setRequirementTagMapper(@Param("requireTags") List<RequirementTag> tags);

    /**
     * 设置对方的必要标签
     * @param tags
     */
     void setNecessaryTagMapper(@Param("requireTags") List<RequirementTag> tags);

    /**
     * 删除RequirementTag，根据openId和necessary
     * @param requirementTag
     * @return
     */
     Integer deleteRequirementTag(RequirementTag requirementTag);

     /**
      * 获取用户对他人的非必要需求Id
      * @param openId
      * @return
      */
     List<Integer> getRequirementTagIdsByOpenId(@Param("openId") String openId);

     /**
      * 获取用户对他人的必要需求Id
      * @param openId
      * @return
      */
     List<Integer> getNecessaryTagIdsByOpenId(@Param("openId") String openId);

     /**
      * 获取用户对他人的非必要需求
      * @param openId
      * @return
      */
     List<RequirementTag> getRequirementTagsByOpenId(@Param("openId") String openId);

     /**
      * 获取用户对他人的必要需求
      * @param openId
      * @return
      */
     List<RequirementTag> getNecessaryTagsByOpenId(@Param("openId") String openId);

     /**
      * 获取用户对他人的所有需求（包括必须和非必需）
      * @param openId
      * @return
      */
     List<RequirementTag> getAllRequireTagsByOpenId(@Param("openId") String openId);
}