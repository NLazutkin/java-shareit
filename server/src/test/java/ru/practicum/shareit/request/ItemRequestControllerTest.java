package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemRequestService itemRequestService;

    @Autowired
    private MockMvc mvc;

    private final String urlTemplate = "/requests";
    private final String headerUserId = "X-Sharer-User-Id";

    private ItemRequestDto makeItemRequestDto(Long id, String description, Long requestorId, LocalDateTime date,
                                              List<ResponseDto> items) {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(id);
        dto.setDescription(description);
        dto.setRequestorId(requestorId);
        dto.setCreated(date);
        dto.setItems(items);

        return dto;
    }

    private ResponseDto makeResponseDto(Long id, String name, Long ownerId) {
        ResponseDto dto = new ResponseDto();
        dto.setId(id);
        dto.setName(name);
        dto.setOwnerId(ownerId);

        return dto;
    }

    @Test
    void createTest() throws Exception {
        ResponseDto item = makeResponseDto(3L, "name", 4L);
        ItemRequestDto requestDto = makeItemRequestDto(1L, "description", 2L,
                LocalDateTime.of(2022, 7, 3, 19, 30, 1), List.of(item));

        when(itemRequestService.create(anyLong(), any())).thenReturn(requestDto);

        mvc.perform(post(urlTemplate)
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(headerUserId, 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").exists())
                .andExpect(content().json(mapper.writeValueAsString(requestDto)))
                .andExpect(jsonPath("$.items").exists())
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items[0].id").value(is(item.getId()), Long.class))
                .andExpect(jsonPath("$.items[0].name").value(is(item.getName()), String.class))
                .andExpect(jsonPath("$.items[0].ownerId").value(is(item.getOwnerId()), Long.class));
    }

    @Test
    void findRequestTest() throws Exception {
        ResponseDto item = makeResponseDto(3L, "name", 4L);
        ItemRequestDto requestDto = makeItemRequestDto(1L, "description", 2L,
                LocalDateTime.of(2022, 7, 3, 19, 30, 1), List.of(item));

        when(itemRequestService.findItemRequest(anyLong())).thenReturn(requestDto);

        mvc.perform(get(urlTemplate + "/" + requestDto.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(requestDto)));
    }

    @Test
    void findAllByRequestorIdTest() throws Exception {
        ResponseDto item = makeResponseDto(3L, "name", 4L);
        ItemRequestDto requestDto1 = makeItemRequestDto(1L, "description", 1L,
                LocalDateTime.of(2022, 7, 3, 19, 30, 1), List.of(item));
        ItemRequestDto requestDto2 = makeItemRequestDto(2L, "description", 2L,
                LocalDateTime.of(2023, 7, 3, 19, 30, 1), List.of(item));

        List<ItemRequestDto> newRequests = List.of(requestDto1, requestDto2);

        when(itemRequestService.findAllByRequestorId(anyLong())).thenReturn(newRequests);

        mvc.perform(get(urlTemplate)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(headerUserId, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(is(newRequests.getFirst().getId()), Long.class))
                .andExpect(jsonPath("$[1].id").value(is(newRequests.getLast().getId()), Long.class))
                .andExpect(content().json(mapper.writeValueAsString(newRequests)));
    }

    @Test
    void findAllOfAnotherRequestorsTest() throws Exception {
        ResponseDto item = makeResponseDto(3L, "name", 4L);
        ItemRequestDto requestDto1 = makeItemRequestDto(1L, "description", 1L,
                LocalDateTime.of(2022, 7, 3, 19, 30, 1), List.of(item));
        ItemRequestDto requestDto2 = makeItemRequestDto(2L, "description", 2L,
                LocalDateTime.of(2023, 7, 3, 19, 30, 1), List.of(item));

        List<ItemRequestDto> newRequests = List.of(requestDto1, requestDto2);

        when(itemRequestService.findAllOfAnotherRequestors(anyLong())).thenReturn(newRequests);

        mvc.perform(get(urlTemplate + "/all")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(headerUserId, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(is(newRequests.getFirst().getId()), Long.class))
                .andExpect(jsonPath("$[1].id").value(is(newRequests.getLast().getId()), Long.class))
                .andExpect(content().json(mapper.writeValueAsString(newRequests)));
    }

    @Test
    void updateTest() throws Exception {
        ResponseDto item = makeResponseDto(3L, "name", 4L);
        ItemRequestDto requestDto = makeItemRequestDto(1L, "description", 2L,
                LocalDateTime.of(2022, 7, 3, 19, 30, 1), List.of(item));

        when(itemRequestService.update(anyLong(), any())).thenReturn(requestDto);

        mvc.perform(put(urlTemplate)
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(headerUserId, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(content().json(mapper.writeValueAsString(requestDto)));
    }

    @Test
    void deleteTest() throws Exception {
        mvc.perform(delete(urlTemplate + "/" + anyLong()))
                .andExpect(status().isOk());

        verify(itemRequestService, times(1)).delete(anyLong());
    }

}
