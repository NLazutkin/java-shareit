package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long id;

    String name;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long ownerId;
}
