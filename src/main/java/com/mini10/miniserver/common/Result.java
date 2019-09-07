package com.mini10.miniserver.common;

import java.io.Serializable;


/**
 * 共用返回接口
 * @param <T>
 */
public class Result<T> implements Serializable {
    private Integer code;
    private String message;
    private T data;

    public Integer getCode()
    {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Result() {
    }

    public Result(Integer code, String message) {
        this.code = code;
        this.message = message;
        this.data = null;
    }

    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
