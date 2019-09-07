package com.mini10.miniserver.common.utils;

import com.mini10.miniserver.common.Constant;
import com.mini10.miniserver.common.Result;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;

/**
 * 返回数据
 */
public class AjaxObject extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;


    public static Result<Object> error() {
        Result<Object> result = new Result<>();
        result.setCode(Constant.ResultCode.ERROR_CODE);
        result.setMessage(Constant.ResultInfo.ERROR_INFO);
        return result;
    }

    public static Result<Object> error(Exception e) {
        Result<Object> result = new Result<>();
        result.setCode(Constant.ResultCode.ERROR_CODE);
        result.setMessage(getStackTrace(e));
        return result;
    }

    /**
     * 异常信息前加自定义的出错信息
     * @param msg
     * @param e
     * @return
     */
    public static Result<Object> error(String msg,Exception e) {
        Result<Object> result = new Result<>();
        result.setCode(Constant.ResultCode.ERROR_CODE);
        result.setMessage(msg + "\n" + getStackTrace(e));
        return result;
    }


    public static Result<Object> sessionIdError() {
        Result<Object> result = new Result<>();
        result.setCode(Constant.ResultCode.SESSION_CODE);
        result.setMessage(Constant.ResultInfo.SESSION_ERROR_INFO);
        return result;
    }
    public static Result<Object> error(String msg) {
        Result<Object> result = new Result<>();
        result.setCode(Constant.ResultCode.ERROR_CODE);
        result.setMessage(msg);
        return result;
    }
    public static Result<Object> error(Integer code,String msg) {
        Result<Object> result = new Result<>();
        result.setCode(code);
        result.setMessage(msg);
        return result;
    }
    public static Result<Object> incivilization(String msg) {
        Result<Object> result = new Result<>();
        result.setCode(Constant.ResultCode.UQUALIFIED_CODE);
        result.setMessage(msg);
        return result;
    }

    public static Result<Object> error(Integer code,Exception e) {
        Result<Object> result = new Result<>();
        result.setCode(code);
        result.setMessage(getStackTrace(e));
        return result;
    }


    public static Result<Object> success(Object object){
        Result<Object> result = new Result<>();
        result.setCode(Constant.ResultCode.SUCCESS_CODE);
        result.setMessage(Constant.ResultInfo.SUCCESS_INFO);
        result.setData(object);
        return result;
    }
    public static Result<Object> success(String msg, Object object) {
        Result<Object> result = new Result<>();
        result.setCode(Constant.ResultCode.SUCCESS_CODE);
        result.setMessage(msg);
        result.setData(object);
        return result;
    }

    /**
     * 获取错误的堆栈信息
     * @param throwable
     * @return
     */
    public static String getStackTrace(Throwable throwable){
        StringWriter stringWriter=new StringWriter();
        PrintWriter printWriter=new PrintWriter(stringWriter);
        try {
            throwable.printStackTrace(printWriter);
            return stringWriter.toString();
        }finally {
            printWriter.close();
        }
    }
}
