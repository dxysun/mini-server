package com.mini10.miniserver.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mini10.miniserver.common.requestwrapper.NewRequestWrapper;
import com.mini10.miniserver.common.requestwrapper.RequestParameterWrapper;
import com.mini10.miniserver.common.utils.AjaxObject;
import com.mini10.miniserver.common.utils.RedisUtil;
import com.mini10.miniserver.common.utils.RequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@WebFilter(filterName = "RequestFilter",urlPatterns = "/api/*")
public class RequestFilter implements Filter {

    private static Logger log = LoggerFactory.getLogger(RequestFilter.class);

    @Autowired
    private RequestUtil requestUtil;

    @Override
    public void init(FilterConfig filterConfig){
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {

        HttpServletRequest httpServletRequest =(HttpServletRequest)request;
        if(httpServletRequest.getMethod().equals(RequestMethod.GET.name()))
        {
            try {
                String openId = requestUtil.getOpenId(httpServletRequest);
                if(openId != null){
                    Map<String, Object> extraParams = new HashMap<>();
                    extraParams.put("openId", openId);
                    RequestParameterWrapper requestParameterWrapper = new RequestParameterWrapper(httpServletRequest);
                    requestParameterWrapper.addParameters(extraParams);
                    chain.doFilter(requestParameterWrapper,response);
                }
                else {
                    setResponse(response, JSON.toJSONString(AjaxObject.sessionIdError()));
                    return;
                }

            } catch (Exception e) {
                e.printStackTrace();
                log.error(AjaxObject.getStackTrace(e));
                setResponse(response, JSON.toJSONString(AjaxObject.error(e)));
            }
            return;
        }

        String openId = requestUtil.getOpenId(httpServletRequest);
        if(openId == null){
            setResponse(response, JSON.toJSONString(AjaxObject.sessionIdError()));
            return;
        }

        if (httpServletRequest.getMethod().equalsIgnoreCase(RequestMethod.POST.name())) {
            //构造请求封装类
            NewRequestWrapper newRequestWrapper = new NewRequestWrapper(httpServletRequest);
            JSONObject requestBody = null;
            try {
                requestBody = JSON.parseObject(newRequestWrapper.getNewRequestWrapperBody(httpServletRequest));
            } catch (Exception e) {
                e.printStackTrace();
                log.error(AjaxObject.getStackTrace(e));
                setResponse(response, "过滤器报错\n" + JSON.toJSONString(AjaxObject.error(e)));
                return;
            }
            if(requestBody != null && !StringUtils.isEmpty(openId)){
                requestBody.put("openId", openId);
                if("/api/group/addGroupInfo".equals(httpServletRequest.getServletPath())){
                    requestBody.put("sessionKey", requestUtil.getSessionKey(httpServletRequest));
                }
                httpServletRequest = (HttpServletRequest) newRequestWrapper.getNewRequestWrapper(httpServletRequest, requestBody.toJSONString());
            }

            try {
                chain.doFilter(httpServletRequest, response);
            }
            catch (Exception e){
                e.printStackTrace();
                log.error(AjaxObject.getStackTrace(e));
                setResponse(response, "过滤器报错\n" + JSON.toJSONString(AjaxObject.error(e)));
            }
        }

    }

    private void setResponse(ServletResponse response,String msg) {

        response.setContentType("application/json; charset=utf-8");
        response.setCharacterEncoding("UTF-8");

        OutputStream out = null;
        try {
            out = response.getOutputStream();
            out.write(msg.getBytes("UTF-8"));
            out.flush();
            out.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void destroy() {
    }
}
