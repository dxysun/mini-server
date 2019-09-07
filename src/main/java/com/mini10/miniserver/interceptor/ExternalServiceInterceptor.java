package com.mini10.miniserver.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;

/**
 * 自定义拦截器，用于配置拦截业务请求
 * @author dongxiyan
 */
public class ExternalServiceInterceptor implements HandlerInterceptor {
    private static Logger logger = LoggerFactory.getLogger(ExternalServiceInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        logger.info("sessionId:" + request.getRequestedSessionId() +",访问url:" + request.getRequestURI());
        return true;
   }
}
