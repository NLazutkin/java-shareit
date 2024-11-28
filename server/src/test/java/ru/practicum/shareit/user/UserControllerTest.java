package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    UserService userService;

    @Autowired
    private MockMvc mvc;

    private UserDto makeUserDto(Long id, String email, String name, LocalDate date) {
        UserDto dto = new UserDto();
        dto.setId(id);
        dto.setEmail(email);
        dto.setName(name);
        dto.setBirthday(date);

        return dto;
    }

    @Test
    void createUserTest() throws Exception {
        UserDto userDto = makeUserDto(1L, "john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3));

        when(userService.create(any())).thenReturn(userDto);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.email", is(userDto.getEmail()), String.class))
                .andExpect(jsonPath("$.name", is(userDto.getName()), String.class))
                .andExpect(jsonPath("$.birthday", is(userDto.getBirthday()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))), String.class));
    }

    @Test
    void findUserTest() throws Exception {
        UserDto findUser = makeUserDto(1L, "ivan@email", "Ivan Ivanov", LocalDate.of(2022, 7, 3));

        when(userService.findUser(anyLong())).thenReturn(findUser);

        mvc.perform(get("/users" + "/" + findUser.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id", is(findUser.getId()), Long.class))
                .andExpect(jsonPath("$.email", is(findUser.getEmail()), String.class))
                .andExpect(jsonPath("$.name", is(findUser.getName()), String.class))
                .andExpect(jsonPath("$.birthday", is(findUser.getBirthday()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))), String.class));
    }

    @Test
    void getUsersTest() throws Exception {
        List<UserDto> newUsers = List.of(
                makeUserDto(1L, "ivan@email", "Ivan Ivanov", LocalDate.of(2022, 7, 3)),
                makeUserDto(2L, "petr@email", "Petr Petrov", LocalDate.of(2022, 8, 4)));

        when(userService.getUsers()).thenReturn(newUsers);

        mvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(is(newUsers.getFirst().getId()), Long.class))
                .andExpect(jsonPath("$[0].email").value(is(newUsers.getFirst().getEmail())))
                .andExpect(jsonPath("$[0].name").value(is(newUsers.getFirst().getName())))
                .andExpect(jsonPath("$[0].birthday", is(newUsers.getFirst().getBirthday()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))), String.class))
                .andExpect(jsonPath("$[1].id").value(is(newUsers.getLast().getId()), Long.class))
                .andExpect(jsonPath("$[1].email").value(is(newUsers.getLast().getEmail())))
                .andExpect(jsonPath("$[1].name").value(is(newUsers.getLast().getName())))
                .andExpect(jsonPath("$[1].birthday", is(newUsers.getLast().getBirthday()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))), String.class));
    }

    @Test
    void updateTest() throws Exception {
        UserDto userDto = makeUserDto(1L, "john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3));

        when(userService.update(anyLong(), any())).thenReturn(userDto);

        mvc.perform(patch("/users" + "/" + userDto.getId())
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.email", is(userDto.getEmail()), String.class))
                .andExpect(jsonPath("$.name", is(userDto.getName()), String.class))
                .andExpect(jsonPath("$.birthday", is(userDto.getBirthday()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))), String.class));
    }

    @Test
    void deleteTest() throws Exception {
        mvc.perform(delete("/users" + "/" + anyLong()))
                .andExpect(status().isOk());

        verify(userService, times(1)).delete(anyLong());
    }
}
