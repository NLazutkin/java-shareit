package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.enums.Statuses;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    BookingService bookingService;

    @Autowired
    private MockMvc mvc;

    private final String urlTemplate = "/bookings";
    private final String headerUserId = "X-Sharer-User-Id";

    private ItemDto makeItemDto(Long id, String name, String description, Boolean available, Long ownerId, Long requestId) {
        ItemDto dto = new ItemDto();
        dto.setId(id);
        dto.setName(name);
        dto.setDescription(description);
        dto.setAvailable(available);
        dto.setOwnerId(ownerId);
        dto.setRequestId(requestId);

        return dto;
    }

    private UserDto makeUserDto(Long id, String email, String name, LocalDate date) {
        UserDto dto = new UserDto();
        dto.setId(id);
        dto.setEmail(email);
        dto.setName(name);
        dto.setBirthday(date);

        return dto;
    }

    private BookingDto makeBookingDto(Long id, LocalDateTime start, LocalDateTime end, Statuses status, UserDto booker, ItemDto item) {
        BookingDto dto = new BookingDto();
        dto.setId(id);
        dto.setStart(start);
        dto.setEnd(end);
        dto.setStatus(status);
        dto.setBooker(booker);
        dto.setItem(item);

        return dto;
    }

    @Test
    void createTest() throws Exception {
        ItemDto itemDto = makeItemDto(1L, "name", "description", Boolean.TRUE, 1L, 1L);
        UserDto userDto = makeUserDto(1L, "john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3));
        BookingDto requestDto = makeBookingDto(1L, LocalDateTime.of(2022, 7, 3, 19, 30, 1),
                LocalDateTime.of(2022, 7, 3, 19, 30, 1), Statuses.APPROVED, userDto, itemDto);

        when(bookingService.create(anyLong(), any())).thenReturn(requestDto);

        mvc.perform(post(urlTemplate)
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(headerUserId, 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").exists())
                .andExpect(content().json(mapper.writeValueAsString(requestDto)));
    }

    @Test
    void findBookingTest() throws Exception {
        ItemDto itemDto = makeItemDto(1L, "name", "description", Boolean.TRUE, 1L, 1L);
        UserDto userDto = makeUserDto(1L, "john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3));
        BookingDto requestDto = makeBookingDto(1L, LocalDateTime.of(2022, 7, 3, 19, 30, 1),
                LocalDateTime.of(2022, 7, 3, 19, 30, 1), Statuses.APPROVED, userDto, itemDto);

        when(bookingService.findBooking(anyLong(), anyLong())).thenReturn(requestDto);

        mvc.perform(get(urlTemplate + "/" + requestDto.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(headerUserId, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(requestDto)))
                .andExpect(jsonPath("$.item").exists())
                .andExpect(jsonPath("$.item.id").value(is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.item.name").value(is(itemDto.getName()), String.class))
                .andExpect(jsonPath("$.item.description").value(is(itemDto.getDescription()), String.class))
                .andExpect(jsonPath("$.booker").exists())
                .andExpect(jsonPath("$.booker.id").value(is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.booker.email").value(is(userDto.getEmail()), String.class))
                .andExpect(jsonPath("$.booker.name").value(is(userDto.getName()), String.class));
    }

    @Test
    void findAllBookingsByUserTest() throws Exception {
        ItemDto itemDto = makeItemDto(1L, "name", "description", Boolean.TRUE, 1L, 1L);
        UserDto userDto = makeUserDto(1L, "john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3));
        BookingDto requestDto1 = makeBookingDto(1L, LocalDateTime.of(2022, 7, 3, 19, 30, 1),
                LocalDateTime.of(2022, 7, 3, 19, 30, 1), Statuses.APPROVED, userDto, itemDto);
        BookingDto requestDto2 = makeBookingDto(2L, LocalDateTime.of(2022, 7, 3, 19, 30, 1),
                LocalDateTime.of(2022, 7, 3, 19, 30, 1), Statuses.REJECTED, userDto, itemDto);

        List<BookingDto> newRequests = List.of(requestDto1, requestDto2);

        when(bookingService.findAllBookingsByUser(anyLong(), any())).thenReturn(newRequests);

        mvc.perform(get(urlTemplate)
                        .characterEncoding(StandardCharsets.UTF_8)
                        //
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
    void findAllBookingsByOwnerItemsTest() throws Exception {
        ItemDto itemDto = makeItemDto(1L, "name", "description", Boolean.TRUE, 1L, 1L);
        UserDto userDto = makeUserDto(1L, "john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3));
        BookingDto requestDto1 = makeBookingDto(1L, LocalDateTime.of(2022, 7, 3, 19, 30, 1),
                LocalDateTime.of(2022, 7, 3, 19, 30, 1), Statuses.APPROVED, userDto, itemDto);
        BookingDto requestDto2 = makeBookingDto(2L, LocalDateTime.of(2022, 7, 3, 19, 30, 1),
                LocalDateTime.of(2022, 7, 3, 19, 30, 1), Statuses.REJECTED, userDto, itemDto);

        List<BookingDto> newRequests = List.of(requestDto1, requestDto2);

        when(bookingService.findAllBookingsByOwnerItems(anyLong(), anyString())).thenReturn(newRequests);

        mvc.perform(get(urlTemplate + "/owner")
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
        ItemDto itemDto = makeItemDto(1L, "name", "description", Boolean.TRUE, 1L, 1L);
        UserDto userDto = makeUserDto(1L, "john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3));
        BookingDto requestDto = makeBookingDto(1L, LocalDateTime.of(2022, 7, 3, 19, 30, 1),
                LocalDateTime.of(2022, 7, 3, 19, 30, 1), Statuses.APPROVED, userDto, itemDto);

        when(bookingService.update(anyLong(), any())).thenReturn(requestDto);

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

        verify(bookingService, times(1)).delete(anyLong());
    }

    @Test
    void approveBookingTest() throws Exception {
        ItemDto itemDto = makeItemDto(1L, "name", "description", Boolean.TRUE, 1L, 1L);
        UserDto userDto = makeUserDto(1L, "john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3));
        BookingDto requestDto = makeBookingDto(1L, LocalDateTime.of(2022, 7, 3, 19, 30, 1),
                LocalDateTime.of(2022, 7, 3, 19, 30, 1), Statuses.APPROVED, userDto, itemDto);

        when(bookingService.approveBooking(anyLong(), anyLong(), any())).thenReturn(requestDto);

        mvc.perform(patch(urlTemplate + "/" + requestDto.getId())
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(headerUserId, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(content().json(mapper.writeValueAsString(requestDto)));
    }

}
