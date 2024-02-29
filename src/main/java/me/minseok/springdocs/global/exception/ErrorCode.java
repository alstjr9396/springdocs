package me.minseok.springdocs.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "1000", "잘못된 요청입니다."),
    INVALID_MEMBER_ID(HttpStatus.BAD_REQUEST, "1001", "사용자의 ID가 잘못되었습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
