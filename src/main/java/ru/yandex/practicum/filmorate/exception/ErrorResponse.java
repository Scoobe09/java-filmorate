package ru.yandex.practicum.filmorate.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {
    String message;
}
