package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRequestDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long id;
    String description;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long requestorId; // userId
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    LocalDateTime created;
}
