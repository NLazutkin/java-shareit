package ru.practicum.shareit.item.comment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long id;
    String text;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long itemId;
    String authorName;
    LocalDateTime created;
}
