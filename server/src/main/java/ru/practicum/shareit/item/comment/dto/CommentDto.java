package ru.practicum.shareit.item.comment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
