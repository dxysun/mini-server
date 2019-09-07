package com.mini10.miniserver.config;

import com.mini10.miniserver.common.Constant;
import com.netease.cloud.ClientConfiguration;
import com.netease.cloud.Protocol;
import com.netease.cloud.auth.BasicCredentials;
import com.netease.cloud.auth.Credentials;
import com.netease.cloud.services.nos.NosClient;
import com.netease.cloud.services.nos.transfer.TransferManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NosConfig {

    @Bean
    public TransferManager nosClientBean(){
        Credentials credentials = new BasicCredentials(Constant.NeteaseYun.ACCESS_KEY, Constant.NeteaseYun.SECRET_KEY);
        ClientConfiguration conf = new ClientConfiguration();
        // 设置 NosClient 使用的最大连接数
        conf.setMaxConnections(200);
        // 设置 socket 超时时间
        conf.setSocketTimeout(10000);
        // 设置失败请求重试次数
        conf.setMaxErrorRetry(2);
        // 如果要用 https 协议，请加上下面语句
        conf.setProtocol(Protocol.HTTPS);
        NosClient nosClient = new NosClient(credentials,conf);
        nosClient.setEndpoint(Constant.NeteaseYun.ENDPOINT);
        //然后通过nosClient对象来初始化TransferManager
        return new TransferManager(nosClient);
    }
}
