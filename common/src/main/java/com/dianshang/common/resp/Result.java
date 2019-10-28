package com.dianshang.common.resp;

import lombok.Data;

/**
 * @BelongsProject: dianshang
 * @Author: zf
 * @CreateTime: 2019-10-27 17:26
 * @Description: 统一返回结果
 */
@Data
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    public static Result me() {
        return new Result();
    }

    public Result response(Integer code, String message) {
        this.code = code;
        this.message = message;
        return this;
    }

    public Result response(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        return this;
    }
}