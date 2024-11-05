package ru.practicum.shareit.user.storage;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.entity.User;

import java.util.*;

@Slf4j
@Component("InMemoryUserStorage")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class InMemoryUserStorage implements UserStorage {
    Map<Long, User> users = new HashMap<>();

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @Override
    public User create(User user) {
        user.setId(getNextId());
        log.trace("Данные пользователя c ID {} сохранены!", user.getName());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User findUser(Long userId) {
        return Optional.ofNullable(users.get(userId))
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь c ID %d не найден", userId)));
    }

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public User update(User newUser) {
        log.trace("Данные пользователя {} обновлены!", newUser.getName());
        users.put(newUser.getId(), newUser);
        return newUser;
    }

    @Override
    public Boolean delete(Long userId) {
        users.remove(userId);
        return Optional.ofNullable(users.get(userId)).isPresent();
    }

    @Override
    public Boolean isUserWithEmailExist(String eMail) {
        return users.values().stream().anyMatch(userFromMemory -> userFromMemory.getEmail().equals(eMail));
    }
}
