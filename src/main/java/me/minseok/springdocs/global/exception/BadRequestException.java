package me.minseok.springdocs.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BadRequestException extends RuntimeException{

    private ErrorCode errorCode;
}
