package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
public class User {

    private Integer id;
    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Некорректный формат данных email")
    private String email;
    @NotBlank(message = "Логин не может быть пустым")
    @Pattern(regexp = "\\S*", message = "В логине не может быть пробелов")
    private String login;
    private String name;
    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;
}
