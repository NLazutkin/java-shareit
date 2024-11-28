package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ResponseDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemRequestJsonTest {
    @Autowired
    private JacksonTester<ItemRequestDto> requestJson;

    @Autowired
    private JacksonTester<ResponseDto> responseJson;

    @Test
    void testItemRequestDto() throws Exception {
        final LocalDateTime now = LocalDateTime.now();

        final ResponseDto responseDto = new ResponseDto(1L, "name", 1L);
        final List<ResponseDto> items = List.of(responseDto);
        final ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "description", 1L, now, items);

        JsonContent<ItemRequestDto> result = requestJson.write(itemRequestDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(itemRequestDto.getDescription());
        assertThat(result).extractingJsonPathNumberValue("$.requestorId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.created").isNotBlank();
        assertThat(result).extractingJsonPathValue("$.items[0].id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.items[0].name").isEqualTo(responseDto.getName());
    }

    @Test
    void testResponseDto() throws Exception {
        final ResponseDto responseDto = new ResponseDto(1L, "name", 1L);

        JsonContent<ResponseDto> result = responseJson.write(responseDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(responseDto.getName());
        assertThat(result).extractingJsonPathNumberValue("$.ownerId").isEqualTo(1);
    }
}
