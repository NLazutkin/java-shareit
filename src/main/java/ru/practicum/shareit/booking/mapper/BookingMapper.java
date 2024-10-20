package ru.practicum.shareit.booking.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.dto.UpdateBookingRequest;
import ru.practicum.shareit.booking.model.Booking;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingMapper {

    public static BookingDto mapToBookingDto(Booking booking) {
        BookingDto dto = new BookingDto();
        dto.setId(booking.getId());
        dto.setItemId(booking.getItemId());
        dto.setStart(booking.getStart());
        dto.setEnd(booking.getEnd());
        dto.setStatus(booking.getStatus());
        dto.setBookerId(booking.getBookerId());

        return dto;
    }

    public static Booking mapToBooking(NewBookingRequest request) {
        Booking booking = new Booking();
        booking.setItemId(request.getItemId());
        booking.setStart(request.getStart());
        booking.setEnd(request.getEnd());
        booking.setStatus(request.getStatus());
        booking.setBookerId(request.getBookerId());

        return booking;
    }

    public static Booking updateBookingFields(Booking booking, UpdateBookingRequest request) {
        booking.setItemId(request.getItemId());

        if (request.hasStart()) {
            booking.setStart(request.getStart());
        }

        if (request.hasEnd()) {
            booking.setEnd(request.getEnd());
        }

        booking.setStatus(request.getStatus());
        booking.setBookerId(request.getBookerId());

        return booking;
    }
}
