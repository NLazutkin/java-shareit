package ru.practicum.shareit.booking.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.dto.UpdateBookingRequest;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.ValidationException;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BookingServiceImpl implements BookingService {
    BookingStorage bookingStorage;

    @Autowired
    public BookingServiceImpl(@Qualifier(/*"BookingDbStorage"*/"InMemoryBookingStorage") BookingStorage bookingStorage) {
        this.bookingStorage = bookingStorage;
    }

    @Override
    public BookingDto create(NewBookingRequest request) {
        log.debug("Создаем запись о бронировании");

        Booking booking = BookingMapper.mapToBooking(request);
        booking = bookingStorage.create(booking);

        return BookingMapper.mapToBookingDto(booking);
    }

    @Override
    public BookingDto findBooking(Long bookingId) {
        log.debug("Ищем бронирование с ID {}", bookingId);
        return BookingMapper.mapToBookingDto(bookingStorage.findBooking(bookingId));
    }

    @Override
    public Collection<BookingDto> findAll() {
        log.debug("Получаем записи о всех бронированиях");
        return bookingStorage.getBookings().stream().map(BookingMapper::mapToBookingDto).collect(Collectors.toList());
    }

    @Override
    public BookingDto update(UpdateBookingRequest request) {
        log.debug("Обновляем данные о бронировании");

        if (request.getId() == null) {
            throw new ValidationException("ID бронирования должен быть указан");
        }

        Booking updatedItem = BookingMapper.updateBookingFields(bookingStorage.findBooking(request.getId()), request);
        updatedItem = bookingStorage.update(updatedItem);

        return BookingMapper.mapToBookingDto(updatedItem);
    }

    @Override
    public boolean delete(Long bookingId) {
        Booking booking = bookingStorage.findBooking(bookingId);
        log.debug("Удаляем данные о бронировании с ID {}", booking.getId());
        return bookingStorage.delete(bookingId);
    }
}
