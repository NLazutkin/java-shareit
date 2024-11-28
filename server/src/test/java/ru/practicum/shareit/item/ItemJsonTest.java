package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.AdvancedItemDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemJsonTest {
    @Autowired
    private JacksonTester<AdvancedItemDto> advancedJson;

    @Autowired
    private JacksonTester<ItemDto> itemJson;

    @Test
    void testItemDto() throws Exception {
        final ItemDto itemDto = new ItemDto(1L, "name", "description", Boolean.TRUE, 2L, 2L);

        JsonContent<ItemDto> result = itemJson.write(itemDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(itemDto.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(itemDto.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(itemDto.getAvailable());
        assertThat(result).extractingJsonPathNumberValue("$.ownerId").isEqualTo(2);
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(2);
    }

    @Test
    void testCommentsDto() throws Exception {
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime nextDay = LocalDateTime.now().plusDays(1);

        final CommentDto commentDto = new CommentDto(1L, "text", 1L, "John Doe", nextDay);
        final List<CommentDto> comments = List.of(commentDto);

        final AdvancedItemDto responseDto = new AdvancedItemDto(1L, "name", "description", Boolean.TRUE, now, nextDay, comments, 1L, 1L);

        JsonContent<AdvancedItemDto> result = advancedJson.write(responseDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(responseDto.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(responseDto.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(responseDto.getAvailable());
        assertThat(result).extractingJsonPathStringValue("$.lastBooking").isNotBlank();
        assertThat(result).extractingJsonPathStringValue("$.nextBooking").isNotBlank();
        assertThat(result).extractingJsonPathNumberValue("$.ownerId").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
        assertThat(result).extractingJsonPathValue("$.comments[0].id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.comments[0].text").isEqualTo(commentDto.getText());
    }
}
