package com.mini10.miniserver;


import com.mini10.miniserver.service.ImgService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author dongxiyan
 * @date 2019-07-19 16:12
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ImgServiceTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ImgService imgService;

    @Test
    public void uploadPictureTest(){
        try {
            FileInputStream fileInputStream = new FileInputStream("D:\\myFiles\\minin项目\\nohup.log");
            MultipartFile multipartFile = new MockMultipartFile("upFiles",fileInputStream);
            String filePath = imgService.uploadPicture(multipartFile);
            logger.info("filePath:" + filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
