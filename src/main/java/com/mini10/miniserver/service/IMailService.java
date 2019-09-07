package com.mini10.miniserver.service;

/**
 * @author dongxiyan
 * @date 2019-07-19 16:12
 */

import com.mini10.miniserver.common.entity.Email;

/**
 * 邮件发送服务
 *
 * @author wangdy
 * @date 2019/4/19 22:16
 */
public interface IMailService {

    /**
     * 纯文本发送
     *
     * @param mail
     */
    public void send(Email mail);

}

