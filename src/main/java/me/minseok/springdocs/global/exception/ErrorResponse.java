package me.minseok.springdocs.global.exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;

@Data
@Builder
public class ErrorResponse {

    private int status;
    private String message;
    private String code;

    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(
                        ErrorResponse.builder()
                                .status(errorCode.getHttpStatus().value())
                                .message(errorCode.getMessage())
                                .code(errorCode.getCode())
                                .build()
                );
    }
}
