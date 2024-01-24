package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ValidateException extends RuntimeException {
    private final HttpStatus httpStatus;

    public ValidateException(String message) {
        super(message);
        httpStatus = HttpStatus.BAD_REQUEST;
    }
}
