package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.enums.Statuses;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long id;
    LocalDateTime start;
    LocalDateTime end;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long itemId;
    Statuses status;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long bookerId; // userId
}
