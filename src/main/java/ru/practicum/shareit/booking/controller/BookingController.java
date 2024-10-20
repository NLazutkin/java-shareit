package ru.practicum.shareit.booking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.dto.UpdateBookingRequest;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.Collection;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingServiceImpl bookingService;

    @GetMapping("/{id}")
    public BookingDto findItem(@PathVariable("id") Long itemId) {
        return bookingService.findItem(itemId);
    }

    @GetMapping
    public Collection<BookingDto> findAll() {
        return bookingService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto create(@Valid @RequestBody NewBookingRequest item) {
        return bookingService.create(item);
    }

    @PutMapping
    public BookingDto update(@Valid @RequestBody UpdateBookingRequest newItem) {
        return bookingService.update(newItem);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable("id") Long itemId) {
        return bookingService.delete(itemId);
    }
}
