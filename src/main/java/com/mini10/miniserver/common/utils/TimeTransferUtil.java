package com.mini10.miniserver.common.utils;

import org.springframework.stereotype.Component;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
/**
 * 此类用于常见的时间转换支持
 * @author Xiang Jiangnan
 * @date 20190723
 */
public class TimeTransferUtil {

    public static final String TIMEFORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 此方法实现将时间字符串转换成Date
     * @param createTime
     * @return
     */
    public Date string2Date(String createTime){
        Date date = null;
        DateFormat sdf = new SimpleDateFormat(TIMEFORMAT);
        try {
            date = sdf.parse(createTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 此方法实现将Date转换成字符串
     * @param createTime
     * @return 返回时间字符串
     */
    public String date2String(Date createTime){
        //Date time =new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(TIMEFORMAT);
        return sdf.format(createTime);
    }

    public String getYMD(String createTime){
        String ymd = createTime.split(" ")[0];
        return ymd;
    }

    public String getMonth(String createTime){
        String ymd = createTime.split(" ")[0];
        String month = ymd.split("-")[1];
        if(month.charAt(0) == '0'){
            return String.valueOf(month.charAt(1));
        }
        return month;
    }

    public String getDay(String createTime){
        String ymd = createTime.split(" ")[0];
        String day = ymd.split("-")[2];
//        if(day.charAt(0) == '0'){
//            return String.valueOf(day.charAt(1));
//        }
        return day;
    }
}
