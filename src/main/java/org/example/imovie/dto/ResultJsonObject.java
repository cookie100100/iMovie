package org.example.imovie.dto;

import lombok.Getter;

@Getter
public class ResultJsonObject {

    private final int code;
    private final String message;
    private final Object data;

    private ResultJsonObject(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static ResultJsonObject success(Object data) {
        return new ResultJsonObject(RespCode.SUCCESS.getCode(), RespCode.SUCCESS.getMessage(), data);
    }

    public static ResultJsonObject error(RespCode respCode) {
        return new ResultJsonObject(respCode.getCode(), respCode.getMessage(), null);
    }

    public static ResultJsonObject error(RespCode respCode, Object data) {
        return new ResultJsonObject(respCode.getCode(), respCode.getMessage(), data);
    }

    public static ResultJsonObject error(int code, String message) {
        return new ResultJsonObject(code, message, null);
    }

    public static ResultJsonObject error(int code, String message, Object data) {
        return new ResultJsonObject(code, message, data);
    }
}
