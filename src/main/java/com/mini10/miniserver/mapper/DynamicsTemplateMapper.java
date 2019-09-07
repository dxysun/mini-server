package com.mini10.miniserver.mapper;

import com.mini10.miniserver.model.DynamicsTemplate;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DynamicsTemplateMapper {
    /**
     * 此方法用于从模板库里面获取一个日记数据
     * @return
     */
    DynamicsTemplate queryTemplate();

    List<DynamicsTemplate> queryTemplateByNum(@Param("num") Integer num);

}
