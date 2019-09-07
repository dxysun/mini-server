package com.mini10.miniserver.common.utils.faceplus;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mini10.miniserver.common.utils.netease.HttpClient4Utils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author ChenShaoJie, XiaBin
 * @Date 2019-07-31 17:16
 */
public class FaceRecognition {

    public static final String API_KEY = "3v-2xdVkQWyx3QhxjjHUtvf8MTl1z14D";
    public static final String API_SECRET = "7z4dP7AKOzaiRxAeq5r5Y2Y9BUTPHefO";
    public static final String RETURN_ATTRIBUTES = "beauty,age,headpose,smiling";
    public static final String REQUEST_URL = "https://api-cn.faceplusplus.com/facepp/v3/detect";


    /**
     * 通过图片url检查人脸颜值
     *
     * @param imageUrl
     * @return
     */
    public static JSONObject checkFaceValue(String imageUrl) {
        StringBuffer result = new StringBuffer();
        try {
//            String url = "https://api-cn.faceplusplus.com/facepp/v3/detect";
            HttpPost post = new HttpPost(REQUEST_URL);

            // add header
            List<NameValuePair> urlParameters = new ArrayList<>();
            urlParameters.add(new BasicNameValuePair("api_key", API_KEY));
            urlParameters.add(new BasicNameValuePair("api_secret", API_SECRET));
            urlParameters.add(new BasicNameValuePair("image_url", imageUrl));
            urlParameters.add(new BasicNameValuePair("return_attributes", RETURN_ATTRIBUTES));
            urlParameters.add(new BasicNameValuePair("calculate_all", "1"));

            post.setEntity(new UrlEncodedFormEntity(urlParameters));

            HttpResponse response = HttpClient4Utils.httpClient.execute(post);

            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            System.out.println(result.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSON.parseObject(result.toString());
    }

    /**
     * 通过MultipartFile格式图片检测人脸
     *
     * @param multipartFile
     */
    public static JSONObject checkFaceValue(MultipartFile multipartFile) {
        StringBuffer result = new StringBuffer();
        try {
            File file = parseToFile(multipartFile);
            HttpPost post = new HttpPost(REQUEST_URL);

            // 图片参数
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            entityBuilder.addPart("image_file", new FileBody(file));
            // 字符串参数
            entityBuilder.addPart("api_key", new StringBody(API_KEY, ContentType.create("text/plain", "UTF-8")));
            entityBuilder.addPart("api_secret", new StringBody(API_SECRET, ContentType.create("text/plain", "UTF-8")));
            entityBuilder.addPart("return_attributes", new StringBody(RETURN_ATTRIBUTES, ContentType.create("text/plain", "UTF-8")));
            HttpEntity httpEntity = entityBuilder.build();
            post.setEntity(httpEntity);

            // 处理返回信息
            HttpResponse response = HttpClient4Utils.httpClient.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSON.parseObject(result.toString());
    }

    /**
     * MultipartFile格式转化为File格式
     *
     * @param multipartFile
     * @return
     */
    private static File parseToFile(MultipartFile multipartFile) {
        File file = null;
        try {
            file = File.createTempFile("tmp", null);
            multipartFile.transferTo(file);
            file.deleteOnExit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }
}

