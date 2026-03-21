package org.example.imovie.dto;

import lombok.Getter;

@Getter
public enum RespCode {

    // Success
    SUCCESS(200, "success"),

    // Client errors
    BAD_REQUEST(400, "Bad request"),
    UNAUTHORIZED(401, "未登录，请先登录"),
    FORBIDDEN(403, "没有权限访问该资源"),
    VALIDATION_FAILED(400, "Validation failed"),

    // Business errors
    USER_NOT_FOUND(404, "User not found"),
    SCHEDULE_NOT_FOUND(404, "Schedule not found"),
    STATISTICS_NOT_FOUND(404, "Statistics not found"),
    ACCOUNT_ALREADY_EXISTS(409, "Account already exists"),
    INVALID_PASSWORD(401, "Invalid password"),
    INSUFFICIENT_TICKETS(400, "Not enough tickets available"),
    CONCURRENT_UPDATE(409, "System is busy, please try again");

    private final int code;
    private final String message;

    RespCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
