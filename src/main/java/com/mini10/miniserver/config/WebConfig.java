package com.mini10.miniserver.config;

import com.mini10.miniserver.common.Constant;
import com.mini10.miniserver.interceptor.ExternalServiceInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.net.InetAddress;
import java.net.UnknownHostException;


@Configuration
public class WebConfig implements WebMvcConfigurer {
    private static Logger logger = LoggerFactory.getLogger(WebConfig.class);

    @Bean
    public ExternalServiceInterceptor externalServiceInterceptor() {
        return new ExternalServiceInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InetAddress ia = null;
        try {
            ia = InetAddress.getLocalHost();
            Constant.HOST_NAME = ia.getHostName();
        } catch (UnknownHostException e1) {
            logger.error(e1.getMessage(), e1);
        }
        // 添加拦截器
        registry.addInterceptor(externalServiceInterceptor()).addPathPatterns("/**").excludePathPatterns("/user/check");
    }
}
