package ru.practicum.shareit.request.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateRequest {
    Long id;
    String description;
    Long requestorId; // userId
    LocalDateTime created;
}
