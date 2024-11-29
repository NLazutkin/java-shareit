package ru.practicum.shareit.user.dto;

import io.micrometer.common.util.StringUtils;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(of = {"id"})
@AllArgsConstructor
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateUserRequest {
    Long id;
    String email;
    String name;
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
