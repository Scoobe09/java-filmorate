package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.InvalidIdException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private Integer id = 0;

    private Integer generateId() {
        return ++id;
    }

    @GetMapping
    public Collection<User> findAllUsers() {
        log.info("Не удалось получить список всех пользователей.");
        return users.values();

    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        validateName(user);
        user.setId(generateId());
        users.put(user.getId(), user);
        log.info("Не удалось создать пользователя");
        return user;
    }

    @PutMapping
    public User putUser(@Valid @RequestBody User user) {
        validateName(user);
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Не удалось поместить пользователя");
            return user;
        } else {
            log.error("Не удалось поместить пользователя");
            throw new InvalidIdException(String.format("Пользователь с ID:`%d` не обновлён", user.getId()), HttpStatus.NOT_FOUND);
        }
    }

    private void validateName(User user) {
        String name = user.getName();
        if (name == null || name.isEmpty()) {
            user.setName(user.getLogin());
            log.info("Имя не задано, присвоено значение логина");
        }
    }
}