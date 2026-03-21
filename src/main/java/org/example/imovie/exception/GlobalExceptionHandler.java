package org.example.imovie.exception;

import org.example.imovie.dto.RespCode;
import org.example.imovie.dto.ResultJsonObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResultJsonObject> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.badRequest()
                .body(ResultJsonObject.error(RespCode.VALIDATION_FAILED, fieldErrors));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ResultJsonObject> handleBusinessException(BusinessException ex) {
        return ResponseEntity.status(ex.getRespCode().getCode())
                .body(ResultJsonObject.error(ex.getRespCode()));
    }

    @ExceptionHandler(jakarta.persistence.OptimisticLockException.class)
    public ResponseEntity<ResultJsonObject> handleOptimisticLockException(jakarta.persistence.OptimisticLockException ex) {
        return ResponseEntity.status(RespCode.CONCURRENT_UPDATE.getCode())
                .body(ResultJsonObject.error(RespCode.CONCURRENT_UPDATE));
    }

    @ExceptionHandler(org.springframework.orm.ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ResultJsonObject> handleObjectOptimisticLockingFailureException(
            org.springframework.orm.ObjectOptimisticLockingFailureException ex) {
        return ResponseEntity.status(RespCode.CONCURRENT_UPDATE.getCode())
                .body(ResultJsonObject.error(RespCode.CONCURRENT_UPDATE));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResultJsonObject> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResultJsonObject.error(500, ex.getMessage()));
    }
}
