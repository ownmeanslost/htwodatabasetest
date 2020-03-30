package com.example.htwodatabasetest.excel.model;

/**
 * @ClassName: BaseAjaxVO
 * @Author: shaofan.li
 * @Description:
 * @Date: 2019/11/23 14:55
 */
public class BaseAjaxVO {
    private int code;
    private String text;
    public Object result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
