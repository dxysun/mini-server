package com.mini10.miniserver.common.utils;

import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author XiaBin
 * @date 2019-08-12 09:49
 * 封装的list工具
 */
public class ListUtils {

    /**
     * 截取list片段
     * @param list
     * @param fromIndex
     * @param toIndex
     * @param <T>
     * @return
     */
    public static <T> List<T> getSubList(List<T> list, int fromIndex, int toIndex) {
        if (toIndex > list.size()) {
        }
        List listClone = list;
        List sub = listClone.subList(fromIndex, toIndex);
        List two = new ArrayList(sub);
        sub.clear();
        return two;
    }


}
