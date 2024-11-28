package ru.practicum.shareit.item.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewCommentRequest {
    @NotBlank(message = "Комментарий не может быть пустым")
    String text;

    @Positive(message = "ID предмета на который пишут комментарий не может быть отрицательным числом")
    Long itemId;

    @Positive(message = "ID пользователя который который пишет комментарий не может быть отрицательным числом")
    Long authorId;
}
