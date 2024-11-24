package ru.practicum.shareit.request.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of = {"id"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateRequest {
    Long id;
    String description;
    Long requestorId; // userId
    LocalDateTime created;
}
