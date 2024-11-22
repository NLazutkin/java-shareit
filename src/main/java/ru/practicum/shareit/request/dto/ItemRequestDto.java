package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRequestDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long id;

    String description;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long requestorId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    LocalDateTime created;

    List<ResponseDto> items;
}
