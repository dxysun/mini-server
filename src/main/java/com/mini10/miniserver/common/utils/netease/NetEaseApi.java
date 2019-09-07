package com.mini10.miniserver.common.utils.netease;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.http.Consts;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

/**
 * @author XiaBin
 * @date 2019-07-30 14:37
 * 文本鉴黄方法checkApi，参数有dataId:数据的唯一id我们这边设置例如：ebfcad1c-dba1-490c-b4de-e784c2691768；content:需要鉴定的内容
 * 图片鉴黄方法checkImgApiByUrl，两个参数，当flag为1的时候表示输入图片地址，当flag=2时表示输入图片Base64
 * 都是静态方法直接调用没有托管到bean，codewei200表示调用接口正常，不过鉴别状态要看action，action的0表示通过，1表示模糊不清我们也是通过，2表示不通过
 * 测试类com.mini10.miniserver.NeteaseApiTest有demo
 */
public class NetEaseApi {
    private final static Logger logger = LoggerFactory.getLogger(NetEaseApi.class);

    /**
     * 响应成功code
     */
    public final static Integer SUCCESS_CODE = 200;

    /**
     * 涉及敏感信息的标识
     */
    public final static Integer SENSITIVE = 2;
    /**
     * 产品密钥ID，产品标识
     */
    private final static String SECRETID = "a261f2d3b64f642a5b518c2e4c4f3862";
    /**
     * 产品私有密钥，服务端生成签名信息使用，请严格保管，避免泄露
     */
    private final static String SECRETKEY = "83824e39211f7e78073174efce879074";
    /**
     * 业务ID，易盾根据产品业务特点分配
     */
    private final static String BUSINESSID_TXT = "35d31eb11bac151b8589f2959cc9f210";
    /**
     * 业务ID，易盾根据产品业务特点分配
     */
    private final static String BUSINESSID_IMG = "7e138cca75f390cc7c74eeda8a302890";
    /**
     * 易盾反垃圾云服务文本在线检测接口地址
     */
    private final static String API_URL_TXT = "https://as.dun.163yun.com/v3/text/check";
    /**
     * 易盾反垃圾云服务图片在线检测接口地址
     */
    private final static String API_URL_IMG = "https://as.dun.163yun.com/v4/image/check";


    /**
     * 网易云内容安全检测私有方法封装
     *
     * @param jObjectRequest
     * @return
     * @throws Exception
     */
    private static JSONObject checkApi(JSONObject jObjectRequest) throws Exception {
        JSONObject objectResponse = new JSONObject();
        if (jObjectRequest == null) {
            return objectResponse;
        }
        // flag为0表示文本检测，flag为1表示图片检测,flag为使用base64图片检测
        int flag = jObjectRequest.getInteger("flag");
        Map<String, Object> params = new HashMap<>();
        // 1.设置公共参数
        params.put("secretId", SECRETID);
        if (flag == 0) {
            params.put("businessId", BUSINESSID_TXT);
            params.put("version", "v3.1");
        } else {
            params.put("businessId", BUSINESSID_IMG);
            params.put("version", "v4");
        }
        params.put("timestamp", String.valueOf(System.currentTimeMillis()));
        params.put("nonce", String.valueOf(new Random().nextInt()));

        // 2.私有参数
        switch (flag) {
            case 0:
                params.put("dataId", jObjectRequest.get("dataId").toString());
                params.put("content", jObjectRequest.get("content").toString());
                break;
            case 1:
                JsonArray jsonArrayUrl = new JsonArray();
                // 传图片url进行检测，name结构产品自行设计，用于唯一定位该图片数据
                JsonObject image1 = new JsonObject();
                image1.addProperty("name", jObjectRequest.get("imgUrl").toString());
                image1.addProperty("type", 1);
                image1.addProperty("data", jObjectRequest.get("imgUrl").toString());
                jsonArrayUrl.add(image1);
                params.put("images", jsonArrayUrl.toString());
                break;
            case 2:
                JsonArray jsonArrayBase64 = new JsonArray();
                // 传图片base64编码进行检测，name结构产品自行设计，用于唯一定位该图片数据
                JsonObject image2 = new JsonObject();
                image2.addProperty("name", "{\"imageId\": 33451123, \"contentId\": 78978}");
                image2.addProperty("type", 2);
                image2.addProperty("data",jObjectRequest.getString("data"));
                jsonArrayBase64.add(image2);
                params.put("images", jsonArrayBase64.toString());
                break;
            default:
                break;
        }
        // 3.生成签名信息
        String signature = SignatureUtils.genSignature(SECRETKEY, params);
        params.put("signature", signature);
        // 4.发送HTTP请求，这里使用的是HttpClient工具包，产品可自行选择自己熟悉的工具包发送请求
        String response;
        if (flag == 0) {
            response = HttpClient4Utils.sendPost(API_URL_TXT, params, Consts.UTF_8);
        } else {
            response = HttpClient4Utils.sendPost(API_URL_IMG, params, Consts.UTF_8);
        }
        logger.info("返回的response为=" + response);
        return JSON.parseObject(response);
    }

    /**
     * 利用网易云API检测文本是否涉黄涉爆
     * @param dataId
     * @param content
     * @return
     */
    public static JSONObject checkTxtApi(String dataId, String content) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("dataId", dataId);
        jsonObject.put("content", content);
        jsonObject.put("flag", 0);
        JSONObject response = new JSONObject();
        try {
            response = checkApi(jsonObject);
            if (response == null) {
                return null;
            }
            int code = response.getInteger("code");
            String msg = response.getString("msg");
            if (code == 200) {
                JSONObject resultObject = response.getJSONObject("result");
                int action = resultObject.getInteger("action");
                String taskId = resultObject.getString("taskId");
                JSONArray labelArray = resultObject.getJSONArray("labels");
                if (action == 0) {
                    logger.info(String.format("taskId=%s，文本机器检测结果：通过", taskId));
                } else if (action == 1) {
                    logger.info(String.format("taskId=%s，文本机器检测结果：嫌疑，需人工复审，分类信息如下：%s", taskId, labelArray.toString()));
                } else if (action == 2) {
                    logger.info(String.format("taskId=%s，文本机器检测结果：不通过，分类信息如下：%s", taskId, labelArray.toString()));
                }
            } else {
                logger.info(String.format("ERROR: code=%s, msg=%s", code, msg));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 通过网易云接口检查图片是否涉黄涉爆
     * flag为1表示通过url查询，flag为2表示通过图片base64查询
     * @param imgUrlOrData
     * @param flag 1 or 2
     * @return
     */
    public static JSONObject checkImgApiByUrlOrData(String imgUrlOrData,int flag) {
        JSONObject jsonObject = new JSONObject();
        if (flag == 1){
            // 监测地址
            jsonObject.put("imgUrl", imgUrlOrData);
            jsonObject.put("flag", flag);
        }else{
            // base64 数据
            jsonObject.put("data", imgUrlOrData);
            jsonObject.put("flag", flag);
        }
        JSONObject response = new JSONObject();
        try {
            response = checkApi(jsonObject);
            if (response == null) {
                return null;
            }
            int code = response.getInteger("code");
            String msg = response.getString("msg");
            if (code == 200) {
                // 图片反垃圾结果
                JSONArray antispamArray = response.getJSONArray("antispam");
                for (Iterator iterator = antispamArray.iterator(); iterator.hasNext(); ) {
                    JSONObject job = (JSONObject) iterator.next();
                    String name = job.getString("name");
                    int status = job.getInteger("status");
                    String taskId = job.getString("taskId");
                    // 图片维度结果
                    int action = job.getInteger("action");
                    JSONArray labelArray = job.getJSONArray("labels");
                    logger.info(String.format("taskId=%s，status=%s，name=%s，action=%s", taskId, status, name, action));
                    // 产品需根据自身需求，自行解析处理，本示例只是简单判断分类级别
                    for (Iterator iteratorDetail = labelArray.iterator(); iterator.hasNext(); ) {
                        JSONObject jobDetail = (JSONObject) iteratorDetail.next();
                        int label = jobDetail.getInteger("label");
                        int level = jobDetail.getInteger("level");
                        double rate = jobDetail.getInteger("rate");
                        JSONArray subLabels = jobDetail.getJSONArray("subLabels");
                        logger.info(String.format("label:%s, level=%s, rate=%s, subLabels=%s", label, level, rate,
                                subLabels.toString()));
                    }
                    switch (action) {
                        case 0:
                            logger.info("#图片机器检测结果：最高等级为\"正常\"\n");
                            break;
                        case 1:
                            logger.info("#图片机器检测结果：最高等级为\"嫌疑\"\n");
                            break;
                        case 2:
                            logger.info("#图片机器检测结果：最高等级为\"不通过\"\n");
                            break;
                        default:
                            break;
                    }
                }
                // 图片OCR结果
                JSONArray ocrArray = response.getJSONArray("ocr");
                for (Iterator iterator = ocrArray.iterator(); iterator.hasNext(); ) {
                    JSONObject job = (JSONObject) iterator.next();
                    String name = job.getString("name");
                    String taskId = job.getString("taskId");
                    JSONArray details = job.getJSONArray("details");
                    // 产品需根据自身需求，自行解析处理，本示例只是简单判断分类级别
                    for (Iterator iteratorDetail = details.iterator(); iterator.hasNext(); ) {
                        JSONObject jobDetail = (JSONObject) iteratorDetail.next();
                        String content = jobDetail.getString("content");
                        JSONArray lineContents = jobDetail.getJSONArray("lineContents");
                        logger.info(String.format("识别ocr文本内容:%s, ocr片段及坐标信息:%s", content, lineContents.toString()));
                    }
                }
            } else {
                logger.info(String.format("ERROR: code=%s, msg=%s", code, msg));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
