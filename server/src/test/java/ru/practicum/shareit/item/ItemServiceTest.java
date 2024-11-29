package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotItemOwnerException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.comment.dto.NewCommentRequest;
import ru.practicum.shareit.item.dto.AdvancedItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ItemServiceTest {
    @Mock
    private ItemRepository itemRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testAddCommentWhenCommentIsEmpty() {
        NewCommentRequest newComment = new NewCommentRequest("comment", 1L, 1L);

        // user
        NewUserRequest newUser = new NewUserRequest("john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3));

        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(new User(1L, "john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3)));

        UserDto userDto = userService.create(newUser);
        User user = new User(1L, "john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3));

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        // item
        NewItemRequest newItem = new NewItemRequest("name", "description", Boolean.TRUE, 1L, 1L);
        Item item = new Item(1L, "name", "description", Boolean.TRUE, user, 1L);
        when(itemRepository.save(any())).thenReturn(item);

        ItemDto findItem = itemService.create(1L, newItem);

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        //check
        when(bookingRepository.existsByBookerIdAndItemIdAndEndBefore(anyLong(), anyLong(), any())).thenReturn(Boolean.FALSE);
        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            itemService.addComment(1L, 1L, newComment);
        });

        assertEquals(String.format("Пользователь %s не может оставить комментарий, " +
                "так как не пользовался вещью %s", user.getName(), item.getName()), thrown.getMessage());
    }

    @Test
    void testDeleteItemWithWrongOwnerId() {
        // user
        NewUserRequest newUser = new NewUserRequest("john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3));

        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(new User(1L, "john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3)));

        UserDto userDto = userService.create(newUser);
        User user = new User(1L, "john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3));

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        // item
        NewItemRequest newItem = new NewItemRequest("name", "description", Boolean.TRUE, 1L, 1L);
        Item item = new Item(1L, "name", "description", Boolean.TRUE, user, 1L);
        when(itemRepository.save(any())).thenReturn(item);

        ItemDto findItem = itemService.create(1L, newItem);

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        NotItemOwnerException thrown = assertThrows(NotItemOwnerException.class, () -> {
            itemService.delete(2L, 1L);
        });

        assertEquals("Удалить данные вещи может только её владелец", thrown.getMessage());
    }

    @Test
    void testUpdateItemWithWrongOwnerId() {
        // user
        NewUserRequest newUser = new NewUserRequest("john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3));

        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(new User(1L, "john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3)));

        UserDto userDto = userService.create(newUser);
        User user = new User(1L, "john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3));

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        // item
        NewItemRequest newItem = new NewItemRequest("name", "description", Boolean.TRUE, 1L, 1L);
        Item item = new Item(1L, "name", "description", Boolean.TRUE, user, 1L);
        when(itemRepository.save(any())).thenReturn(item);

        ItemDto findItem = itemService.create(1L, newItem);

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        NotItemOwnerException thrown = assertThrows(NotItemOwnerException.class, () -> {
            itemService.update(1L, null, 2L);
        });

        assertEquals("Редактировать данные вещи может только её владелец", thrown.getMessage());
    }

    @Test
    void testFindItemsForTenantWithBlankText() {
        Collection<ItemDto> items = itemService.findItemsForTenant(1L, null);

        assertEquals(items, new ArrayList<>());
    }

    @Test
    void testFindAllWithWrongOwnerId() {
        when(itemRepository.findAllByUserId(anyLong())).thenReturn(new ArrayList<>());

        Collection<AdvancedItemDto> items = itemService.findAll(999L);

        assertEquals(items, new ArrayList<>());
    }
}
