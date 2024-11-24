package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long id;
    String email;
    String name;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    LocalDate birthday;
}
