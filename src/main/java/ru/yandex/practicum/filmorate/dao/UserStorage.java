package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    List<User> findAllUsers();

    User createUser(User user);

    User updateUser(User user);

    boolean deleteById(Integer id);

    User findById(Integer id);

    boolean isExist(Integer id);

    List<User> getUserFriends(Integer id);

    List<User> findMutualFriends(Integer userId, Integer idFriend);

    boolean addFriend(Integer userId, Integer idFriend);

    boolean removeFriend(Integer userId, Integer idFriend);
}
