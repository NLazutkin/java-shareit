package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.enums.Statuses;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of = {"id"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewBookingRequest {
    Long id;
    LocalDateTime start;
    LocalDateTime end;
    @NotNull(message = "У искомой вещи должен быть ID")
    @Positive(message = "ID вещи не может быть отрицательным числом")
    Long itemId;
    @NotNull(message = "Статус бронирования вещи не может быть пустым. Укажите одно из: " +
            "\"WAITING\", \"APPROVED\", \"REJECTED\", \"CANCELED\".")
    Statuses status;
    @NotNull(message = "ID пользователя который хочет забронировать вещь не может быть пустым")
    @Positive(message = "ID пользователя который хочет забронировать вещь не может быть отрицательным числом")
    Long bookerId; // userId
}
