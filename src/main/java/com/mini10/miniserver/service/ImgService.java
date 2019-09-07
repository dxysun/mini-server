package com.mini10.miniserver.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @author dongxiyan
 * @date 2019-07-19 16:12
 */
public interface ImgService {
    public String uploadPicture(MultipartFile multipartfile) throws Exception;
}
