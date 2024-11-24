package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;

@Controller
@Validated
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/users")
public class UserController {
    private final UserClient userClient;
    private final String id = "/{user-id}";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> addUser(@Valid @RequestBody NewUserRequest newUser) {
        return userClient.addUser(newUser);
    }

    @GetMapping(id)
    public ResponseEntity<Object> findUser(@PathVariable("user-id") Long userId) {
        return userClient.getUser(userId);
    }

    @GetMapping
    public ResponseEntity<Object> getUsers() {
        return userClient.getUsers();
    }

    @PatchMapping(id)
    public ResponseEntity<Object> updateUser(@PathVariable("user-id") Long userId,
                                             @Valid @RequestBody UpdateUserRequest newUser) {
        return userClient.updateUser(userId, newUser);
    }

    @DeleteMapping(id)
    public ResponseEntity<Object> deleteUser(@PathVariable("user-id") Long userId) {
        return userClient.deleteUser(userId);
    }
}

