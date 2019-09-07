package com.mini10.miniserver.common.entity;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author dongxiyan
 * @date 2019-07-19 16:12
 */
public class Email implements Serializable {

    /**
     *  接收方邮件数组
      */
    private String[] email;
    /**
     *主题,邮件标题
     */
    private String subject;
    /**
     * 邮件内容文本
     */
    private String content;
    /**
     * 邮件模板
     */
    private String template;
    /**
     *自定义参数，用于模板发送时显示内容
     */
    private HashMap<String, Object> kvMap;

    public Email() {
        super();
    }

    public Email(String[] email, String subject, String content, String template, HashMap<String, Object> kvMap) {
        super();
        this.email = email;
        this.subject = subject;
        this.content = content;
        this.template = template;
        this.kvMap = kvMap;
    }

    public String[] getEmail() {
        return email;
    }

    public void setEmail(String[] email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public HashMap<String, Object> getKvMap() {
        return kvMap;
    }

    public void setKvMap(HashMap<String, Object> kvMap) {
        this.kvMap = kvMap;
    }
}
