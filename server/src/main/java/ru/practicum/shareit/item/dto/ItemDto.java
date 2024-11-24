package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long id;
    String name;
    String description;
    Boolean available;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long ownerId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long requestId;
}
