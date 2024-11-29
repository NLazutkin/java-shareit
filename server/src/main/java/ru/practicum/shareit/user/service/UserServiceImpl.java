package ru.practicum.shareit.user.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserServiceImpl implements UserService {
    UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    private User findById(Long userId) {
        return repository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь c ID %d не найден", userId)));
    }

    @Override
    @Transactional
    public UserDto create(NewUserRequest request) {
        log.debug("Создаем запись пользователя");

        Optional<User> findUser = repository.findByEmail(request.getEmail());
        if (findUser.isPresent()) {
            throw new DuplicatedDataException(String.format("Этот E-mail \"%s\" уже используется", request.getEmail()));
        }

        User user = UserMapper.mapToUser(request);
        user = repository.save(user);

        return UserMapper.mapToUserDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto findUser(Long userId) {
        return UserMapper.mapToUserDto(findById(userId));
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<UserDto> getUsers() {
        log.debug("Получаем записи всех пользователей");
        return repository.findAll()
                .stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto update(Long userId, UpdateUserRequest request) {
        log.debug("Обновляем данные пользователя");

        if (userId == null) {
            throw new ValidationException("ID пользователя должен быть указан");
        }

        Optional<User> findUser = repository.findByEmail(request.getEmail());
        if (findUser.isPresent()) {
            throw new DuplicatedDataException(String.format("Этот E-mail \"%s\" уже используется", request.getEmail()));
        }

        User updatedUser = UserMapper.updateUserFields(findById(userId), request);
        updatedUser = repository.save(updatedUser);

        return UserMapper.mapToUserDto(updatedUser);
    }

    @Override
    @Transactional
    public void delete(Long userId) {
        User findUser = findById(userId);
        log.debug("Удаляем данные пользователя {}", findUser.getName());
        repository.delete(findUser);
    }
}
