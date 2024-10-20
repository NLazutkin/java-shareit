package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.dto.UpdateBookingRequest;

import java.util.Collection;

public interface BookingService {
    BookingDto create(NewBookingRequest request);

    BookingDto findBooking(Long bookingId);

    Collection<BookingDto> findAll();

    BookingDto update(UpdateBookingRequest request);

    boolean delete(Long bookingId);
}
