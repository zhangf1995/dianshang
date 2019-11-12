package com.dianshang.common.en;

public enum StateCode {
    SUCCESS(200,"success"),
    FAIL(500,"fail"),
    NOTAUTH(400,"notAuth");

    private Integer code;
    private String msg;

    StateCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode(){
        return code;
    }

    public String getMsg(){
        return msg;
    }
}
