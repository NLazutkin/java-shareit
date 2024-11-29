package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.dto.UpdateBookingRequest;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;
    private final String id = "/{booking-id}";
    private final String owner = "/owner";

    private final String headerUserId = "X-Sharer-User-Id";
    private final String pvBookingId = "booking-id";


    @GetMapping(id)
    public BookingDto findBooking(@RequestHeader(headerUserId) Long userId,
                                  @PathVariable(pvBookingId) Long bookingId) {
        return bookingService.findBooking(bookingId, userId);
    }

    @GetMapping
    public Collection<BookingDto> findAllBookingsByUser(@RequestHeader(headerUserId) Long userId,
                                                        @RequestParam(name = "state", defaultValue = "ALL") String state) {
        return bookingService.findAllBookingsByUser(userId, state);
    }

    @GetMapping(owner)
    public Collection<BookingDto> findAllBookingsByOwnerItems(@RequestHeader(headerUserId) Long userId,
                                                              @RequestParam(name = "state", defaultValue = "ALL") String state) {
        return bookingService.findAllBookingsByOwnerItems(userId, state);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto create(@RequestHeader(headerUserId) Long userId,
                             @RequestBody NewBookingRequest booking) {
        return bookingService.create(userId, booking);
    }

    @PutMapping
    public BookingDto update(@RequestHeader(headerUserId) Long userId,
                             @RequestBody UpdateBookingRequest newBooking) {
        return bookingService.update(userId, newBooking);
    }

    @DeleteMapping(id)
    public void delete(@PathVariable(pvBookingId) Long bookingId) {
        bookingService.delete(bookingId);
    }

    @PatchMapping(id)
    public BookingDto approveBooking(@PathVariable(pvBookingId) Long bookingId,
                                     @RequestHeader(headerUserId) Long userId,
                                     @RequestParam(name = "approved", defaultValue = "false") Boolean approved) {
        return bookingService.approveBooking(bookingId, userId, approved);
    }
}
