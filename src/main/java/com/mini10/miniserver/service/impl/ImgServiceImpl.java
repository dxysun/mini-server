package com.mini10.miniserver.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mini10.miniserver.common.Constant;
import com.mini10.miniserver.common.utils.faceplus.FaceRecognition;
import com.mini10.miniserver.common.utils.netease.NetEaseApi;
import com.mini10.miniserver.service.ImgService;
import com.netease.cloud.services.nos.transfer.TransferManager;
import com.netease.cloud.services.nos.transfer.Upload;
import com.netease.cloud.services.nos.transfer.model.UploadResult;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.UUID;

/**
 * @author dongxiyan
 * @date 2019-07-19 16:12
 */
@Service
public class ImgServiceImpl implements ImgService {

    @Autowired
    public TransferManager transferManager;

    /**
     * 上传文件至网易云nos
     * @param multipartfile
     * @return
     * @throws Exception
     */
    @Override
    public String uploadPicture(MultipartFile multipartfile) throws Exception {
        if (multipartfile == null) {
            throw new Exception("上传的图片为空");
        }
        //设置统一图片后缀名
        String suffixName;
        //获取图片文件名(不带扩展名的文件名)
        String prefixName = getFileNameWithoutEx(multipartfile.getOriginalFilename());
        //获取图片后缀名,判断如果是png的话就不进行格式转换,因为Thumbnails存在转png->jpg图片变红bug
        String suffixNameOrigin = getExtensionName(multipartfile.getOriginalFilename());
        if ("png".equals(suffixNameOrigin)) {
            suffixName = "png";
        } else {
            suffixName = "jpg";
        }
        if(prefixName == null){
            prefixName = "oriFile";
        }else{
            prefixName += "oriFile";
        }
        File upFile = File.createTempFile(prefixName, "." + suffixNameOrigin);
        FileUtils.copyInputStreamToFile(multipartfile.getInputStream(), upFile);
        File resFile = upFile;
        if (multipartfile.getSize() > 1024 * 1024) {
            BufferedImage image = ImageIO.read(upFile);
            resFile = compressImg(upFile,image,prefixName,suffixName);
        }

        Upload upload = transferManager.upload("mini10", "avatar/" + UUID.randomUUID().toString() + "." + suffixName,resFile);
        UploadResult result = upload.waitForUploadResult();
        String filePath = Constant.NeteaseYun.YUN_URL + result.getKey();
        JSONObject jsonObject = NetEaseApi.checkImgApiByUrlOrData(filePath,1);
        if(jsonObject != null){
            if(jsonObject.getInteger("code").equals(NetEaseApi.SUCCESS_CODE)){
                if(jsonObject.getJSONArray("antispam").getJSONObject(0).getInteger("action").equals(NetEaseApi.SENSITIVE)){
                    transferManager.getNosClient().deleteObject("mini10",result.getKey());
                    return null;
                }
            }
        }
        resFile.deleteOnExit();
        upFile.deleteOnExit();
        return filePath;
    }

    /**
     * 压缩图片
     * @param upFile
     * @param image
     * @param prefixName
     * @param suffixName
     * @return
     * @throws Exception
     */
    public File compressImg(File upFile,BufferedImage image,String prefixName,String suffixName) throws Exception{

        File resFile = File.createTempFile(prefixName + "_res", "." + suffixName);
        if (image.getHeight() > 1080 || image.getWidth() > 1920) {
            if (!"png".equals(suffixName)) {
                Thumbnails.of(upFile.getAbsolutePath()).size(1366, 768).outputQuality(1f).outputFormat("jpg").toFile(resFile.getAbsolutePath());
            } else {
                Thumbnails.of(upFile.getAbsolutePath()).size(1366, 768).outputQuality(1f).toFile(resFile.getAbsolutePath());
            }
        } else {
            if (!"png".equals(suffixName)) {
                Thumbnails.of(upFile.getAbsolutePath()).outputQuality(1f).scale(1f).outputFormat("jpg").toFile(resFile.getAbsolutePath());
            } else {
                Thumbnails.of(upFile.getAbsolutePath()).outputQuality(1f).scale(0.5f).toFile(resFile.getAbsolutePath());
            }
        }
        return resFile;
    }

    /**
     * 获取文件扩展名
     *
     * @param filename 文件名
     * @return
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    /**
     * 获取不带扩展名的文件名
     *
     * @param filename 文件
     * @return
     */
    private static String getFileNameWithoutEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }
}
