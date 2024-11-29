package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewCommentRequest {
    @NotBlank(message = "Комментарий не может быть пустым")
    String text;

    @Positive(message = "ID предмета на который пишут комментарий не может быть отрицательным числом")
    Long itemId;

    @Positive(message = "ID пользователя который который пишет комментарий не может быть отрицательным числом")
    Long authorId;
}
