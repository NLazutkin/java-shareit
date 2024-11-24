package ru.practicum.shareit.user.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewUserRequest {
    String email;
    String name;
    LocalDate birthday;
}
