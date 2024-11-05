package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.entity.User;

import java.util.Collection;

public interface UserStorage {
    User create(User user);

    User findUser(Long userId);

    Collection<User> getUsers();

    User update(User newUser);

    Boolean delete(Long id);

    Boolean isUserWithEmailExist(String eMail);
}
