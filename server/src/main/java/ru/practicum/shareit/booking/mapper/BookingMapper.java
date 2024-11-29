package ru.practicum.shareit.booking.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.dto.UpdateBookingRequest;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.enums.Statuses;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.mapper.UserMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingMapper {

    public static BookingDto mapToBookingDto(Booking booking) {
        BookingDto dto = new BookingDto();
        dto.setId(booking.getId());
        dto.setItem(ItemMapper.mapToItemDto(booking.getItem()));
        dto.setStart(booking.getStart());
        dto.setEnd(booking.getEnd());
        dto.setStatus(booking.getStatus());
        dto.setBooker(UserMapper.mapToUserDto(booking.getBooker()));

        return dto;
    }

    public static Booking mapToBooking(NewBookingRequest request, User booker, Item item) {
        Booking booking = new Booking();
        booking.setItem(item);
        booking.setStart(request.getStart());
        booking.setEnd(request.getEnd());
        booking.setStatus(Statuses.WAITING);
        booking.setBooker(booker);

        return booking;
    }

    public static Booking updateBookingFields(Booking booking, UpdateBookingRequest request) {
        if (request.hasStart()) {
            booking.setStart(request.getStart());
        }

        if (request.hasEnd()) {
            booking.setEnd(request.getEnd());
        }

        booking.setStatus(request.getStatus());

        return booking;
    }
}
