package ru.practicum.shareit.booking.storage;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component("InMemoryBookingStorage")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class InMemoryBookingStorage implements BookingStorage {
    Map<Long, Booking> bookings = new HashMap<>();

    private long getNextId() {
        long currentMaxId = bookings.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @Override
    public Booking create(Booking booking) {
        booking.setId(getNextId());
        log.trace("Данные о бронировании c ID {} сохранены!", booking.getId());
        bookings.put(booking.getId(), booking);
        return booking;
    }

    @Override
    public Booking findBooking(Long bookingId) {
        return Optional.ofNullable(bookings.get(bookingId))
                .orElseThrow(() -> new NotFoundException(String.format("Бронирование c ID %d не найдено", bookingId)));
    }

    @Override
    public Collection<Booking> getBookings() {
        return bookings.values();
    }

    @Override
    public Booking update(Booking newBooking) {
        log.trace("Данные о бронировании с ID {} обновлены!", newBooking.getId());
        bookings.put(newBooking.getId(), newBooking);
        return newBooking;
    }

    @Override
    public boolean delete(Long itemId) {
        bookings.remove(itemId);
        return Optional.ofNullable(bookings.get(itemId)).isPresent();
    }
}
