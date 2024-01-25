package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {

    List<User> findAllUsers();

    User createUser(User user);

    User updateUser(User user);


    void deleteById(Integer id);

    User findById(Integer id);

    void addFriend(Integer userId, Integer idFriend);

    List<User> getUserFriends(Integer id);

    List<User> findMutualFriends(Integer userId, Integer idFriendShip);

    void removeFriend(Integer userId, Integer idFriend);
}
