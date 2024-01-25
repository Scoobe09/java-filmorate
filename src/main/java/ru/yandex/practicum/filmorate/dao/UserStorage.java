package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    List<User> findAllUsers();

    User createUser(User user);

    Optional<User> updateUser(User user);

    void deleteById(Integer id);

    Optional<User> findById(Integer id);

    boolean addFriend(Integer userId, Integer idFriend);

    List<User> getUserFriends(Integer id);

    List<User> findMutualFriends(Integer userId, Integer idFriendShip);

    boolean removeFriend(Integer userId, Integer idFriend);

    boolean isExist(Integer id);
}
