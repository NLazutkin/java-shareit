package ru.practicum.shareit.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.service.UserServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;
    private final String id = "/{user-id}";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@Valid @RequestBody NewUserRequest user) {
        return userService.create(user);
    }

    @GetMapping(id)
    public UserDto findUser(@PathVariable("user-id") Long userId) {
        return userService.findUser(userId);
    }

    @GetMapping
    public Collection<UserDto> getUsers() {
        return userService.getUsers();
    }

    @PatchMapping(id)
    public UserDto update(@PathVariable("user-id") Long userId,
                          @Valid @RequestBody UpdateUserRequest newUser) {
        return userService.update(userId, newUser);
    }

    @DeleteMapping(id)
    public void delete(@PathVariable("user-id") Long userId) {
        userService.delete(userId);
    }
}

