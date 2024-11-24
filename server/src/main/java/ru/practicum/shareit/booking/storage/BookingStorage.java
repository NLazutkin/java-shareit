package ru.practicum.shareit.booking.storage;

import ru.practicum.shareit.booking.entity.Booking;

import java.util.Collection;

public interface BookingStorage {
    Booking create(Booking request);

    Booking findBooking(Long bookingId);

    Collection<Booking> getBookings();

    Booking update(Booking request);

    boolean delete(Long bookingId);
}
