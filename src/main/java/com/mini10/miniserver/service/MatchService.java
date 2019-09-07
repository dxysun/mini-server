package com.mini10.miniserver.service;

import com.mini10.miniserver.model.User;
import com.mini10.miniserver.model.param.MatchGroupResult;
import com.mini10.miniserver.model.param.MatchingResult;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;

/**
 * @Author ChenShaoJie
 * @Date 2019-07-22 09:15
 */
public interface MatchService {

    /**
     * 获取实际群的结果
     *
     * @param openId
     * @return
     */
    List<MatchGroupResult> getMatchResult(String openId);

    /**
     * 获取虚拟群的匹配结果(基于地理位置的分群)
     *
     * @param openId
     * @return
     */
    List<MatchGroupResult> getVirtualMatchResult(String openId, String longitude, String latitude);

    MatchingResult matchUser(User selfUser, User targetUser);


}
