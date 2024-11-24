package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.dto.UpdateBookingRequest;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingServiceImpl bookingService;
    private final String id = "/{booking-id}";
    private final String owner = "/owner";

    @GetMapping(id)
    public BookingDto findBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                  @PathVariable("booking-id") Long bookingId) {
        return bookingService.findBooking(bookingId, userId);
    }

    @GetMapping
    public Collection<BookingDto> findAllBookingsByUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                        @RequestParam(name = "state", defaultValue = "ALL") String state) {
        return bookingService.findAllBookingsByUser(userId, state);
    }

    @GetMapping(owner)
    public Collection<BookingDto> findAllBookingsByOwnerItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                              @RequestParam(name = "state", defaultValue = "ALL") String state) {
        return bookingService.findAllBookingsByOwnerItems(userId, state);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto create(@RequestHeader("X-Sharer-User-Id") Long userId,
                             @RequestBody NewBookingRequest booking) {
        return bookingService.create(userId, booking);
    }

    @PutMapping
    public BookingDto update(@RequestHeader("X-Sharer-User-Id") Long userId,
                             @RequestBody UpdateBookingRequest newBooking) {
        return bookingService.update(userId, newBooking);
    }

    @DeleteMapping(id)
    public void delete(@PathVariable("booking-id") Long bookingId) {
        bookingService.delete(bookingId);
    }

    @PatchMapping(id)
    public BookingDto approveBooking(@PathVariable("booking-id") Long bookingId,
                                     @RequestHeader("X-Sharer-User-Id") Long userId,
                                     @RequestParam(name = "approved", defaultValue = "false") Boolean approved) {
        return bookingService.approveBooking(bookingId, userId, approved);
    }
}
