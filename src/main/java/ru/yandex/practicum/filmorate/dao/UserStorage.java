package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserStorage {

    Collection<User> findAllUsers();

    User createUser(User user);

    User updateUser(User user);

    void deleteById(Integer id1);

    Optional<User> findById(Integer id1);

    boolean addFriend(Integer userId, Integer idFriend);

    List<User> getUserFriends(Integer id);

    List<User> findMutualFriends(Integer userId, Integer idFriendShip);

    boolean removeFriend(Integer userId, Integer idFriend);

    boolean isExist(Integer id);
}
