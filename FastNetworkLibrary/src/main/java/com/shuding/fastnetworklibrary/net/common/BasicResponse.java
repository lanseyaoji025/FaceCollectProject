package com.shuding.fastnetworklibrary.net.common;

/**
 * 统一的处理结果类
 */
public class BasicResponse<T> {

    private int code;
    private String msg;
    private T content;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }
}
