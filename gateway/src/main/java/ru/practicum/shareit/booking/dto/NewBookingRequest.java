package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewBookingRequest {

    LocalDateTime start;

    LocalDateTime end;

    @NotNull(message = "У искомой вещи должен быть ID")
    @Positive(message = "ID вещи не может быть отрицательным числом")
    Long itemId;

    @Positive(message = "ID пользователя который хочет забронировать вещь не может быть отрицательным числом")
    Long bookerId;
}
