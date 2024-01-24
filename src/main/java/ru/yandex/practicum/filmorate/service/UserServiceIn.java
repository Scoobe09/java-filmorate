package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserServiceIn {

     Collection<User> findAllUsers();

     User createUser(User user1);

     User updateUser(User user1);


     void deleteById(Integer id);

     User findById(Integer id1);

     void addFriend(Integer userId, Integer idFriend);

     List<User> getUserFriends(Integer id);

     List<User> findMutualFriends(Integer userId, Integer idFriendShip);

     boolean removeFriend(Integer userId, Integer idFriend);
}
