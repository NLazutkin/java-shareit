package ru.practicum.shareit.item.dto;

import io.micrometer.common.util.StringUtils;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = {"id"})
public class UpdateItemRequest {
    Long id;
    String name;
    String description;
    Boolean available;
    @Positive(message = "ID владельца вещи не может быть отрицательным числом")
    Long ownerId;
    @Positive(message = "ID запроса на создание вещи не может быть отрицательным числом")
    Long requestId;

    public boolean hasName() {
        return !StringUtils.isBlank(this.name);
    }

    public boolean hasDescription() {
        return !StringUtils.isBlank(this.description);
    }

    public boolean hasAvailable() {
        return this.available != null;
    }
}
