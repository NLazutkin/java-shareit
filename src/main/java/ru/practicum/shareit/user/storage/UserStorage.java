package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserStorage {
    User create(User user);

    User findUser(Long userId);

    Collection<User> getUsers();

    User update(User newUser);

    boolean delete(Long id);

    boolean isUserWithEmailExist(String eMail);
}
