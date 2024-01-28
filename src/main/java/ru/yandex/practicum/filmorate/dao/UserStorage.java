package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    List<User> findAllUsers();

    User createUser(User user);

    Optional<User> updateUser(User user);

    void deleteById(Integer id);

    User findById(Integer id);

    boolean isExist(Integer id);
}
