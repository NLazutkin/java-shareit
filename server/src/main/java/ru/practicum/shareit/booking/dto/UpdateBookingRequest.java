package ru.practicum.shareit.booking.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.enums.Statuses;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of = {"id"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateBookingRequest {
    Long id;
    LocalDateTime start;
    LocalDateTime end;
    Long itemId;
    Statuses status;
    Long bookerId; // userId

    public boolean hasStart() {
        return this.start != null;
    }

    public boolean hasEnd() {
        return this.end != null;
    }
}
