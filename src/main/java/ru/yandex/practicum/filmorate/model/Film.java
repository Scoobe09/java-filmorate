package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotations.MinimumDate;
import ru.yandex.practicum.filmorate.constant.FilmConstants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Film.
 */

@Data
@Builder
public class Film {
    private Integer id;
    @NotBlank(message = "Название фильма не может быть пустым")
    private String name;
    @Size(min = 1, max = 200, message = "Максимальное количество символов в описании 200, а минимальное 1")
    private String description;
    @MinimumDate(value = FilmConstants.RELEASE_DATE, message = "Дата не может быть раньше 28 декабря 1895 года")
    private LocalDate releaseDate;
    @Positive(message = "Число должно быть положительное")
    private int duration;

    private final Set<Integer> likes = new HashSet<>();
}
