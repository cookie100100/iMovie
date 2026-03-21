package org.example.imovie.exception;

import lombok.Getter;
import org.example.imovie.dto.RespCode;

@Getter
public class BusinessException extends RuntimeException {

    private final RespCode respCode;

    public BusinessException(RespCode respCode) {
        super(respCode.getMessage());
        this.respCode = respCode;
    }
}
