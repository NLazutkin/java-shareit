package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.dto.UpdateBookingRequest;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.enums.Statuses;
import ru.practicum.shareit.exception.NotItemOwnerException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.exception.WrongBookingStatusException;
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
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class BookingServiceTest {
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

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    void testCreateBookingWhenItemNotAvailable() {
        NewBookingRequest newBooking = new NewBookingRequest(LocalDateTime.of(2024, 7, 1, 19, 30, 15),
                LocalDateTime.of(2024, 7, 2, 19, 30, 15), 1L, 2L);

        // user
        NewUserRequest newUser = new NewUserRequest("john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3));

        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(new User(1L, "john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3)));

        UserDto userDto = userService.create(newUser);
        User user = new User(1L, "john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3));

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        // item
        NewItemRequest newItem = new NewItemRequest("name", "description", Boolean.FALSE, 1L, 1L);
        Item item = new Item(1L, "name", "description", Boolean.FALSE, user, 1L);
        when(itemRepository.save(any())).thenReturn(item);

        ItemDto findItem = itemService.create(1L, newItem);

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            bookingService.create(1L, newBooking);
        });

        assertEquals("Вещь не доступна для бронирования!", thrown.getMessage());
    }

    @Test
    void testCreateBookingWhenItemUserIsBooker() {
        NewBookingRequest newBooking = new NewBookingRequest(LocalDateTime.of(2024, 7, 1, 19, 30, 15),
                LocalDateTime.of(2024, 7, 2, 19, 30, 15), 1L, 2L);

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

        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            bookingService.create(1L, newBooking);
        });

        assertEquals("Нельзя бронировать собственную вещь", thrown.getMessage());
    }

    @Test
    void testFindBookingWhenUserNotItemOwnerOrBooker() {
        // user 1
        NewUserRequest newUser1 = new NewUserRequest("john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3));

        when(userRepository.findByEmail(newUser1.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(new User(1L, "john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3)));

        userService.create(newUser1);
        User user1 = new User(1L, "john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3));

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));

        // user 2
        NewUserRequest newUser2 = new NewUserRequest("john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3));

        when(userRepository.findByEmail(newUser2.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(new User(2L, "john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3)));

        userService.create(newUser2);
        User user2 = new User(2L, "john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3));

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user2));

        // item
        NewItemRequest newItem = new NewItemRequest("name", "description", Boolean.TRUE, 1L, 1L);
        Item item = new Item(1L, "name", "description", Boolean.TRUE, user1, 1L);
        when(itemRepository.save(any())).thenReturn(item);

        ItemDto findItem = itemService.create(1L, newItem);

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        // booking
        NewBookingRequest newBooking = new NewBookingRequest(LocalDateTime.of(2024, 7, 1, 19, 30, 15),
                LocalDateTime.of(2024, 7, 2, 19, 30, 15), 1L, 2L);
        Booking booking = new Booking(1L, LocalDateTime.of(2024, 7, 1, 19, 30, 15),
                LocalDateTime.of(2024, 7, 2, 19, 30, 15), item, Statuses.WAITING, user2);
        when(bookingRepository.save(any())).thenReturn(booking);

        BookingDto bookingItem = bookingService.create(2L, newBooking);

        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            bookingService.findBooking(1L, 999L);
        });

        assertEquals("Только владелец вещи и создатель брони могут просматривать данные о бронировании", thrown.getMessage());
    }

    @Test
    void testUpdateBookingWithWrongId() {
        UpdateBookingRequest updBooking = new UpdateBookingRequest(1L, LocalDateTime.of(2026, 7, 1, 19, 30, 15),
                LocalDateTime.of(2026, 7, 2, 19, 30, 15), 1L, Statuses.REJECTED, 1L);
        updBooking.setId(null);

        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            bookingService.update(1L, updBooking);
        });

        assertEquals("ID бронирования должен быть указан", thrown.getMessage());
    }

    @Test
    void testUpdateBookingWithWrongBookerOrOwner() {
        // user 1
        NewUserRequest newUser1 = new NewUserRequest("john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3));

        when(userRepository.findByEmail(newUser1.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(new User(1L, "john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3)));

        userService.create(newUser1);
        User user1 = new User(1L, "john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3));

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));

        // user 2
        NewUserRequest newUser2 = new NewUserRequest("john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3));

        when(userRepository.findByEmail(newUser2.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(new User(2L, "john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3)));

        userService.create(newUser2);
        User user2 = new User(2L, "john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3));

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user2));

        // item
        NewItemRequest newItem = new NewItemRequest("name", "description", Boolean.TRUE, 1L, 1L);
        Item item = new Item(1L, "name", "description", Boolean.TRUE, user1, 1L);
        when(itemRepository.save(any())).thenReturn(item);

        ItemDto findItem = itemService.create(1L, newItem);

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        // booking
        NewBookingRequest newBooking = new NewBookingRequest(LocalDateTime.of(2024, 7, 1, 19, 30, 15),
                LocalDateTime.of(2024, 7, 2, 19, 30, 15), 1L, 2L);
        Booking booking = new Booking(1L, LocalDateTime.of(2024, 7, 1, 19, 30, 15),
                LocalDateTime.of(2024, 7, 2, 19, 30, 15), item, Statuses.WAITING, user2);
        when(bookingRepository.save(any())).thenReturn(booking);

        BookingDto bookingItem = bookingService.create(2L, newBooking);

        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        UpdateBookingRequest updBooking = new UpdateBookingRequest(1L, LocalDateTime.of(2026, 7, 1, 19, 30, 15),
                LocalDateTime.of(2026, 7, 2, 19, 30, 15), 1L, Statuses.REJECTED, 1L);

        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            bookingService.update(1L, updBooking);
        });

        assertEquals("Только владелец вещи и создатель брони могут редактировать данные о бронировании", thrown.getMessage());
    }

    @Test
    void testApproveBookingWithWrongBookerOrOwner() {
        // user 1
        NewUserRequest newUser1 = new NewUserRequest("john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3));

        when(userRepository.findByEmail(newUser1.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(new User(1L, "john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3)));

        userService.create(newUser1);
        User user1 = new User(1L, "john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3));

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));

        // user 2
        NewUserRequest newUser2 = new NewUserRequest("john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3));

        when(userRepository.findByEmail(newUser2.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(new User(2L, "john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3)));

        userService.create(newUser2);
        User user2 = new User(2L, "john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3));

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user2));

        // item
        NewItemRequest newItem = new NewItemRequest("name", "description", Boolean.TRUE, 1L, 1L);
        Item item = new Item(1L, "name", "description", Boolean.TRUE, user1, 1L);
        when(itemRepository.save(any())).thenReturn(item);

        ItemDto findItem = itemService.create(1L, newItem);

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        // booking
        NewBookingRequest newBooking = new NewBookingRequest(LocalDateTime.of(2024, 7, 1, 19, 30, 15),
                LocalDateTime.of(2024, 7, 2, 19, 30, 15), 1L, 2L);
        Booking booking = new Booking(1L, LocalDateTime.of(2024, 7, 1, 19, 30, 15),
                LocalDateTime.of(2024, 7, 2, 19, 30, 15), item, Statuses.WAITING, user2);
        when(bookingRepository.save(any())).thenReturn(booking);

        BookingDto bookingItem = bookingService.create(2L, newBooking);

        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        NotItemOwnerException thrown = assertThrows(NotItemOwnerException.class, () -> {
            bookingService.approveBooking(1L, 2L, Boolean.TRUE);
        });

        assertEquals("Менять статус вещи может только её владелец", thrown.getMessage());
    }

    @Test
    void testApproveBookingWhenStatusNotWaiting() {
        // user 1
        NewUserRequest newUser1 = new NewUserRequest("john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3));

        when(userRepository.findByEmail(newUser1.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(new User(1L, "john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3)));

        userService.create(newUser1);
        User user1 = new User(1L, "john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3));

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));

        // user 2
        NewUserRequest newUser2 = new NewUserRequest("john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3));

        when(userRepository.findByEmail(newUser2.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(new User(2L, "john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3)));

        userService.create(newUser2);
        User user2 = new User(2L, "john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3));

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user2));

        // item
        NewItemRequest newItem = new NewItemRequest("name", "description", Boolean.TRUE, 1L, 1L);
        Item item = new Item(1L, "name", "description", Boolean.TRUE, user1, 1L);
        when(itemRepository.save(any())).thenReturn(item);

        ItemDto findItem = itemService.create(1L, newItem);

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        // booking
        NewBookingRequest newBooking = new NewBookingRequest(LocalDateTime.of(2024, 7, 1, 19, 30, 15),
                LocalDateTime.of(2024, 7, 2, 19, 30, 15), 1L, 2L);
        Booking booking = new Booking(1L, LocalDateTime.of(2024, 7, 1, 19, 30, 15),
                LocalDateTime.of(2024, 7, 2, 19, 30, 15), item, Statuses.APPROVED, user2);
        when(bookingRepository.save(any())).thenReturn(booking);

        BookingDto bookingItem = bookingService.create(2L, newBooking);

        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        WrongBookingStatusException thrown = assertThrows(WrongBookingStatusException.class, () -> {
            bookingService.approveBooking(1L, 1L, Boolean.FALSE);
        });

        assertEquals("Вещь уже занята!", thrown.getMessage());
    }
}
