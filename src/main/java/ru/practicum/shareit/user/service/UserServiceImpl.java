package ru.practicum.shareit.user.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserServiceImpl implements UserService {
    UserStorage userStorage;

    @Autowired
    public UserServiceImpl(@Qualifier(/*"UserDbStorage"*/"InMemoryUserStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public UserDto create(NewUserRequest request) {
        log.debug("Создаем запись пользователя");

        if (userStorage.isUserWithEmailExist(request.getEmail())) {
            throw new DuplicatedDataException(String.format("Этот E-mail \"%s\" уже используется", request.getEmail()));
        }

        User user = UserMapper.mapToUser(request);
        user = userStorage.create(user);

        return UserMapper.mapToUserDto(user);
    }

    @Override
    public UserDto findUser(Long userId) {
        return UserMapper.mapToUserDto(userStorage.findUser(userId));
    }

    @Override
    public Collection<UserDto> getUsers() {
        log.debug("Получаем записи всех пользователей");
        return userStorage.getUsers().stream().map(UserMapper::mapToUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDto update(Long userId, UpdateUserRequest request) {
        log.debug("Обновляем данные пользователя");

        if (userId == null) {
            throw new ValidationException("Id пользователя должен быть указан");
        }

        if (userStorage.isUserWithEmailExist(request.getEmail())) {
            throw new DuplicatedDataException(String.format("Этот E-mail \"%s\" уже используется", request.getEmail()));
        }

        User updatedUser = UserMapper.updateUserFields(userStorage.findUser(userId), request);
        updatedUser = userStorage.update(updatedUser);

        return UserMapper.mapToUserDto(updatedUser);
    }

    @Override
    public boolean delete(Long userId) {
        User user = userStorage.findUser(userId);
        log.debug("Удаляем данные пользователя {}", user.getName());
        return userStorage.delete(userId);
    }
}
