package ru.practicum.shareit.request.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = {"id"})
public class ItemRequest {
    Long id;
    @NotBlank(message = "Запрос не должен быть пустым")
    String description;
    @NotNull(message = "ID пользователя составившего заявку не может не может быть пустым")
    @Positive(message = "ID пользователя составившего заявку не может не может быть отрицательным числом")
    Long requestorId; // userId
    LocalDateTime created;
}
