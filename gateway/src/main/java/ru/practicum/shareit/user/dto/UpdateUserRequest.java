package ru.practicum.shareit.user.dto;

import io.micrometer.common.util.StringUtils;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(of = {"id"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateUserRequest {
    Long id;
    String email;
    String name;
    @PastOrPresent(message = "Дата рождения не может быть больше текущего дня")
    LocalDate birthday;

    public boolean hasEmail() {
        return !StringUtils.isBlank(this.email);
    }

    public boolean hasName() {
        return !StringUtils.isBlank(this.name);
    }

    public boolean hasBirthday() {
        return this.birthday != null;
    }
}
