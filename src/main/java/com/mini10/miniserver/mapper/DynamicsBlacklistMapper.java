package com.mini10.miniserver.mapper;

import com.mini10.miniserver.model.DynamicsBlacklist;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Component
public interface DynamicsBlacklistMapper extends Mapper<DynamicsBlacklist> {

    Integer addBlackList(@Param("openId")String openId,@Param("dynamicId")Integer dynamicId);

    List<DynamicsBlacklist> selectBlacklistByOpenId(String openId);
}