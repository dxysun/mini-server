package com.mini10.miniserver;

import com.hankcs.hanlp.mining.word2vec.DocVectorModel;
import com.hankcs.hanlp.mining.word2vec.WordVectorModel;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;


import static com.mini10.miniserver.common.Constant.MODEL_FILE_NAME;
import static com.mini10.miniserver.common.Constant.wordVectorModel;
import static com.mini10.miniserver.common.Constant.docVectorModel;
import java.io.IOException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableCaching
@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
@ServletComponentScan(basePackages = "com.mini10.miniserver.filter")
@MapperScan({"com.mini10.miniserver.service.impl", "com.mini10.miniserver.mapper", "com.mini10.miniserver.config"})
public class MiniServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiniServerApplication.class, args);
        try {
            wordVectorModel = new WordVectorModel(MODEL_FILE_NAME);
            docVectorModel = new DocVectorModel(wordVectorModel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
