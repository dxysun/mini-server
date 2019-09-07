package com.mini10.miniserver.common.utils;

import com.mini10.miniserver.model.param.DynamicDto;
import com.mini10.miniserver.model.param.MatchingResult;

import java.util.Comparator;

public class MyComparator implements Comparator<DynamicDto> {
    @Override
    public int compare(DynamicDto o1, DynamicDto o2) {
        if(o1.getMatchResult()>o2.getMatchResult())
        {
            return -1;
        }
        else if(o1.getMatchResult()< o2.getMatchResult())
        {
            return 1;
        }
        else
        {   // 按照OpenId排序。
            return o1.getOpenId().compareTo(o2.getOpenId());
        }
    }
}
