package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long id;

    String name;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long ownerId;
}
