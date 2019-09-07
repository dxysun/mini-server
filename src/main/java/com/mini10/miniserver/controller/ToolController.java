package com.mini10.miniserver.controller;

import com.alibaba.fastjson.JSONObject;
import com.mini10.miniserver.common.Constant;
import com.mini10.miniserver.common.Result;
import com.mini10.miniserver.common.utils.AjaxObject;
import com.mini10.miniserver.common.utils.faceplus.FaceRecognition;
import com.mini10.miniserver.service.ImgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * 工具类，用于一些公用工具
 */
@RestController
@RequestMapping("/api/tool")
public class ToolController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public ImgService imgService;

    /**
     * 上传头像文件的接口
     *
     * @param upFile
     * @return Result
     */
    @PostMapping("/uploadFile")
    public Result uploadFile(@RequestParam("upFile") MultipartFile upFile) {
        if (!upFile.isEmpty()) {
            if (StringUtils.isEmpty(upFile.getOriginalFilename())) {
                return AjaxObject.error("上传的文件名为空");
            }
            String suffix = upFile.getOriginalFilename().substring(upFile.getOriginalFilename().lastIndexOf(".") + 1);
            if (!("JPG".equals(suffix) || "jpg".equals(suffix) || "PNG".equals(suffix) || "png".equals(suffix) || " jpeg".equals(suffix) || "bmp".equals(suffix))) {
                return AjaxObject.error("上传的图片格式不对，应为JPG jpg png PNG jpeg bmp中的一种");
            }
            Map<String, Object> map = new HashMap<>(10);
            try {
                String filePath = imgService.uploadPicture(upFile);
                if (filePath == null) {
                    return AjaxObject.success("你上传的图片涉及敏感信息", null);
                }
                map.put("filePath", filePath);
            } catch (Exception e) {
                logger.error(AjaxObject.getStackTrace(e));
                return AjaxObject.error(e);
            }
            return AjaxObject.success("上传文件成功", map);
        } else {
            return AjaxObject.error("文件为空，上传失败");
        }
    }

    /**
     * 上传图片文件的接口，可选择多个文件
     *
     * @param upFile
     * @return Result
     */
    @PostMapping("/uploadImgFile")
    public Result uploadImgFile(@RequestParam("upFiles") MultipartFile upFile) {
        if (upFile == null) {
            return AjaxObject.error(Constant.ResultCode.FILE_UPLOAD_FILE, "上传的文件为空");
        }
        List<JSONObject> faceInfo = new ArrayList<>();
        if (StringUtils.isEmpty(upFile.getOriginalFilename())) {
            return AjaxObject.error(Constant.ResultCode.FILE_UPLOAD_FILE, "文件名为空");
        }
        String suffix = upFile.getOriginalFilename().substring(upFile.getOriginalFilename().lastIndexOf(".") + 1);
        if (!("JPG".equals(suffix) || "jpg".equals(suffix) || "PNG".equals(suffix) || "png".equals(suffix) || " jpeg".equals(suffix) || "bmp".equals(suffix))) {
            return AjaxObject.error(Constant.ResultCode.IMG_TYPE_NOT_RIGHT, "上传的图片格式不对，应为JPG jpg png PNG jpeg bmp中的一种");
        }
        try {
            JSONObject imgInfo = new JSONObject();
            String filePath = imgService.uploadPicture(upFile);
            if (filePath == null) {
                return AjaxObject.success("你上传的图片涉及敏感信息", null);
            }
            JSONObject faceRes = FaceRecognition.checkFaceValue(filePath);
            Integer faceScore = -1;
            if (faceRes != null && faceRes.getInteger("face_num") != null) {
                if(faceRes.getInteger("face_num") > 0 && faceRes.getInteger("face_num") == 1) {
                    JSONObject face = (JSONObject) faceRes.getJSONArray("faces").get(0);
                    JSONObject faceAttributes = face.getJSONObject("attributes");
                    faceScore = (faceAttributes.getJSONObject("beauty").getInteger("female_score") + faceAttributes.getJSONObject("beauty").getInteger("male_score")) / 2;
                }
            }
            imgInfo.put("filePath", filePath);
            imgInfo.put("score", faceScore);
            faceInfo.add(imgInfo);
        } catch (Exception e) {
            logger.error(AjaxObject.getStackTrace(e));
            return AjaxObject.error(Constant.ResultCode.FILE_UPLOAD_FILE, e);
        }
        return AjaxObject.success("上传文件成功", faceInfo);
    }
}
