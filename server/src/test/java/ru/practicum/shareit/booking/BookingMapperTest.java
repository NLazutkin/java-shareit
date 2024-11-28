package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.dto.UpdateBookingRequest;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.enums.Statuses;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class BookingMapperTest {
    private final LocalDateTime now = LocalDateTime.now();
    private final LocalDateTime nextDay = LocalDateTime.now().plusDays(1);

    private final User user = new User(1L, "john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3));
    private final Item item = new Item(1L, "name", "description", Boolean.TRUE, user, 1L);

    private final UserDto userDto = new UserDto(1L, "john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3));
    private final ItemDto itemDto = new ItemDto(1L, "name", "description", Boolean.TRUE, 1L, 1L);

    private final NewBookingRequest newBooking = new NewBookingRequest(now, nextDay, 1L, 1L);
    private final UpdateBookingRequest updBooking = new UpdateBookingRequest(1L, now, nextDay, 1L, Statuses.WAITING, 1L);
    private final UpdateBookingRequest updEmptyBooking = new UpdateBookingRequest(1L, null, null, 1L, Statuses.WAITING, 1L);
    private final BookingDto dto = new BookingDto(1L, now, nextDay, itemDto, Statuses.WAITING, userDto);
    private final Booking booking = new Booking(1L, now, nextDay, item, Statuses.WAITING, user);

    @Test
    public void toBookingDtoTest() {
        BookingDto bookingDto = BookingMapper.mapToBookingDto(booking);
        assertThat(bookingDto, equalTo(dto));
    }

    @Test
    public void toBookingTest() {
        Booking b = BookingMapper.mapToBooking(newBooking, user, item);
        assertThat(b.getStart(), equalTo(booking.getStart()));
        assertThat(b.getEnd(), equalTo(booking.getEnd()));
        assertThat(b.getStatus(), equalTo(booking.getStatus()));
        assertThat(b.getItem(), equalTo(item));
        assertThat(b.getBooker(), equalTo(user));
    }

    @Test
    public void updateBookingFieldsTest() {
        Booking b = BookingMapper.updateBookingFields(booking, updBooking);
        assertThat(b.getId(), equalTo(booking.getId()));
        assertThat(b.getStart(), equalTo(booking.getStart()));
        assertThat(b.getEnd(), equalTo(booking.getEnd()));
        assertThat(b.getStatus(), equalTo(booking.getStatus()));
    }

    @Test
    public void updateBookingEmptyFieldsTest() {
        Booking b = BookingMapper.updateBookingFields(booking, updEmptyBooking);
        assertThat(b.getId(), equalTo(booking.getId()));
        assertThat(b.getStart(), equalTo(booking.getStart()));
        assertThat(b.getEnd(), equalTo(booking.getEnd()));
        assertThat(b.getStatus(), equalTo(booking.getStatus()));
    }
}
