package com.mini10.miniserver.common.requestwrapper;



import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class NewRequestWrapper extends HttpServletRequestWrapper {

    private String body = null;

    public NewRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    /**
     * 构造一个带有请求参数的request请求
     *
     * @param request
     * @param body
     */
    public NewRequestWrapper(HttpServletRequest request, String body) {
        super(request);
        this.body = body;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        ServletInputStream inputStream = null;
        if (!StringUtils.isEmpty(body)) {
            inputStream = new NewServletInputStream(body);
        }
        return inputStream;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        String enc = getCharacterEncoding();
        if (enc == null) {
            enc = "UTF-8";
        }
        return new BufferedReader(new InputStreamReader(getInputStream(), enc));
    }

    public String getNewRequestWrapperBody(HttpServletRequest request) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        if (request.getInputStream() == null) {
            return "";
        }
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(request.getInputStream()))){
            char[] charBuffer = new char[128];
            int bytesRead = -1;
            while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                stringBuilder.append(charBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw e;
        }
        return stringBuilder.toString();
    }

    public ServletRequest getNewRequestWrapper(ServletRequest request, String body) {
        String enctype = request.getContentType();
        String contentType = "application/json";
        if (!StringUtils.isEmpty(enctype) && enctype.contains(contentType)) {
            return new NewRequestWrapper((HttpServletRequest) request, body);
        }
        return request;
    }

    private class NewServletInputStream extends ServletInputStream {

        private InputStream inputStream;
        /**
         * 解析json之后的文本
         */
        private String body;

        public NewServletInputStream(String body) throws IOException {
            this.body = body;
            inputStream = null;
        }

        private InputStream acquireInputStream() throws IOException {
            if (inputStream == null) {
                //通过解析之后传入的文本生成inputStream以便后面control调用
                inputStream = new ByteArrayInputStream(body.getBytes());
            }
            return inputStream;
        }

        @Override
        public void close() throws IOException {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                throw e;
            } finally {
                inputStream = null;
            }
        }

        @Override
        public int read() throws IOException {
            return acquireInputStream().read();
        }

        @Override
        public boolean markSupported() {
            return false;
        }

        @Override
        public synchronized void mark(int i) {
            throw new UnsupportedOperationException("mark not supported");
        }

        @Override
        public synchronized void reset() throws IOException {
            throw new IOException(new UnsupportedOperationException("reset not supported"));
        }

        @Override
        public boolean isFinished() {
            return false;
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setReadListener(ReadListener readListener) {

        }
    }
}

