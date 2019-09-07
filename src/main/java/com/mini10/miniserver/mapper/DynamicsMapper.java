package com.mini10.miniserver.mapper;


import com.github.pagehelper.Page;
import com.mini10.miniserver.model.Dynamics;
import org.springframework.stereotype.Component;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public interface DynamicsMapper {

    Integer insertDynamics(Dynamics dynamics);

    Page<Dynamics> selectAllDynamics(String openId);

    /**
     *按ID删除日记
     * @param id
     * @return 返回删除的记录数
     */
    Integer deleteDiary(Integer id);

    /**
     *按openId查找最新日记
     * @param openId
     * @return 返回最新日记，没有则为null
     */
    Dynamics queryNewestByOpenId(@Param("openId") String openId);


    List<Dynamics> selectByOpenId(@Param("openId") String openId);

    Page<Dynamics> selectMatchedUserDiary(@Param("targetOpenId") String targetOpenId);
}
