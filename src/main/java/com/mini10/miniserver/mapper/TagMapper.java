package com.mini10.miniserver.mapper;

import com.mini10.miniserver.model.Tag;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Component
public interface TagMapper extends Mapper<Tag> {

     List<Tag> queryTagByTagType(@Param("tagType") Integer tagType);
     String queryTagNameById(Integer id);
     Integer queryTagClassifyById(Integer id);
     List<Tag> queryTagByIds(@Param("list") List<Integer> ids);

     void bathAddTags(List<Tag> tagList);
}