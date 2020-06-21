package com.bay.common.exception;

/**
 * @ClassName ExCodeEnum
 * @Description 系统错误状态码
 * @Author baymux
 * @Date 2020/5/30 15:14
 * @Vsrsion 1.0
 **/
public enum ExCodeEnum {

    UNKNOW_EXCEPTION(10000, "系统未知异常"),
    VALID_EXCEPTION(10001, "参数校验不通过");

    private int code;
    private String msg;

    ExCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
