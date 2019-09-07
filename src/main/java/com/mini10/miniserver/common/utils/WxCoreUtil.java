package com.mini10.miniserver.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.mini10.miniserver.common.Constant;
import com.mini10.miniserver.common.entity.AES;
import com.mini10.miniserver.common.entity.WxPKCS7Encoder;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;


/**
 * 封装对外访问方法
 * @author dongxiyan
 * @date 2019-07-19 16:12
 */
public class WxCoreUtil {
    private static final Logger logger = LoggerFactory.getLogger(WxCoreUtil.class);
    private static final String WATERMARK = "watermark";
    private static final String APPID = "appid";

    /**
     * 解密数据
     * @return JSONObject
     */
    public static JSONObject decrypt(String encryptedData, String iv,String sessionKey) throws Exception{
        AES aes = new AES();
        if(sessionKey == null){
            logger.error("Constant.SESSION_KEY == null");
            return null;
        }
        byte[] resultByte = aes.decrypt(Base64.decodeBase64(encryptedData), Base64.decodeBase64(sessionKey), Base64.decodeBase64(iv));
        if(null != resultByte && resultByte.length > 0){
            String result = new String(WxPKCS7Encoder.decode(resultByte));
            JSONObject jsonObject = JSONObject.parseObject(result);
            String decryptAppId = jsonObject.getJSONObject(WATERMARK).getString(APPID);
            if(!Constant.APPID.equals(decryptAppId)){
                return null;
            }
            else {
                return jsonObject;
            }
        }
        else {
            return null;
        }
    }

    /*public static void main(String[] args) {
        String encryptedData = "NiwRPjK4bdDi/qpwKaDCkaQjOp8Iif/Y3YL3Vx5UtS29h/nLu4GAKzs6G36A7UdX1jt87Ev0doH6GUr8k+9lQd8ni3GvgVTclmJlMk2oy0fTCXYMoixtO6gZE4PNfj8xEaHz9wDzbtLOyGjdjghnhQ==";
        String iv = "AGPH33bDWaK3Ne6LXyYIcg==";
        String sessionKey = "4GK0xdAdg8R60kU5aa/t/Q==";
        JSONObject jsonObject = null;
        try {
            jsonObject = decrypt(encryptedData,iv,sessionKey);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("解密出错");
        }
        System.out.println(jsonObject.toJSONString());
    }*/
}
