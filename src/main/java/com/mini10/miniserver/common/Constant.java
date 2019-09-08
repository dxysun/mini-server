package com.mini10.miniserver.common;

import com.hankcs.hanlp.mining.word2vec.DocVectorModel;
import com.hankcs.hanlp.mining.word2vec.WordVectorModel;

import java.util.HashMap;

/**
 * 公用数据类
 */
public class Constant {
//    public static WordVectorModel wordVectorModel;
//    public static DocVectorModel docVectorModel;
    public static String ACCESS_TOKEN = "";
    public static String HOST_NAME = null;

    public class ResultCode {
        public static final int SUCCESS_CODE = 200;    // 调用成功
        public static final int ERROR_CODE = 500;      // 调用失败
        public static final int SESSION_CODE = 401;      // 调用失败，未授权，sessionId过期，需重login
        public static final int UQUALIFIED_CODE = 406;  // 文本不符合规范
        public static final int IMG_TYPE_NOT_RIGHT = 415;  //图片格式不对的错误码
        public static final int FILE_UPLOAD_FILE = 413;  //文件上传错误错误码
        public static final int LOGIN_TIME_OUT = 408;   //login登录超时
    }

    public class ResultInfo {
        public static final String SUCCESS_INFO = "success";    // 调用成功信息
        public static final String ERROR_INFO = "failed";      // 调用失败信息
        public static final String SESSION_ERROR_INFO = "sessionid error";      // 调用失败信息
    }

    public static final String APPID = "wx0360dea9e9f61add";
    public static final String APPSECRET = "5427a119c8b389f73eb1676480260278";
    public static final String JSCODE = "";
    public static final String WX_OPENID_URL = "https://api.weixin.qq.com/sns/jscode2session?appid=" +
            APPID + "&secret=" + APPSECRET + "&grant_type=authorization_code&js_code=" + JSCODE;
    public static final String BAIDU_ACCESS_TOKEN = "24.c54ad14d8aab85b78b3ede1e299a65e8.2592000.1569203840.282335-17075251";

    public static class SpecialCode {
        public static final String HEIGH170 = "170cm+";
        public static final String HEIGH175 = "175cm+";
        public static final String HEIGH180 = "180cm+";
        public static final String HEIGH158 = "158cm+";
        public static final String HEIGH162 = "162cm++";
        public static final String HEIGH168 = "168cm+";
        public static final String SAMEPROVINCE = "同省份";
        public static final String DEGREE_ONE = "本科以上";
        public static final String DEGREE_TWO = "硕士";
        public static final String DEGREE_THREE = "博士";
        public static final Integer DEGREE_ZERO_CODE = 0;
        public static final Integer DEGREE_ONE_CODE = 1;
        public static final Integer DEGREE_TWO_CODE = 2;
        public static final Integer DEGREE_THREE_CODE = 3;
        public static final Integer DEGREE_FOUR_CODE = 4;


    }

    public class MatchingCoefficient {
        public static final double MToF = 0.3;
        public static final double FToM = 0.7;
    }

    public class GenderCode {
        public static final int MALE = 1;
        public static final int FEMAL = 0;
    }

    public class NeteaseYun {
        public static final String ACCESS_KEY = "d8792c08101842e4b737b6f924fc19a5";
        public static final String SECRET_KEY = "73c480324a3a45b7b708c08fec1de9b6";
        public static final String ENDPOINT = "nos-eastchina1.126.net";
        public static final String YUN_URL = "https://mini10.nos-eastchina1.126.net/";
    }

    public class Amap {
        public static final String AMAP_KEY = "86f607a98bd63683fca4a0e249feb24d";
    }


    public static final Integer SESSION_EXPIRED_TIME = 7200;
    public static final Integer SHARE_CODE_EXPIRED_TIME = 21600;

    public static final String USERKEY = "AllUSERS";

    public static final String REDIS_GROUP_RESULT = "groupResult";
    public static final String REDIS_DETAIL_GROUP_RESULT = "detailGroupResult";
    public static final String REDIS_VIRTUAL_GROUP_RESULT = "virtualGroupResult";
    public static final String REDIS_DETAIL_VIRTUAL_GROUP_RESULT = "detailvirtualGroupResult";
    public static final String REDIS_SESSION_KEY = "sessionKey";
    public static final String REDIS_SHARE_CODE_URL = "shareCodeUrl";

    /**
     * 默认的模板用户id
     */
    public static final String DEFAULTOPENID = "DEFAULTTEMPLATEOPENID";
    /**
     * 默认的模板群组id
     */
    public static final String DEFAULTGROUPID = "DEFAULTGROUPID";
    /**
     * 默认的模板nickname
     */
    public static final String DEFAULTNICKNAME = "DEFAULTNICKNAME";


}
