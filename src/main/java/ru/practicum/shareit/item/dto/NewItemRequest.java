package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = {"id"})
public class NewItemRequest {
    Long id;
    @NotBlank(message = "Название вещи не должно быть пустым")
    String name;
    @NotBlank(message = "Описание вещи не должно быть пустым")
    String description;
    @NotNull(message = "Статус вещи не может быть пустым. Укажите занята вещь или свободна")
    Boolean available;
    @Positive(message = "ID владельца вещи не может быть отрицательным числом")
    Long ownerId;
    @Positive(message = "ID запроса на создание вещи не может быть отрицательным числом")
    Long requestId;
}
