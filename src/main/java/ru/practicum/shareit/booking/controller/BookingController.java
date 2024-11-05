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
    private final String path = "/{id}";

    @GetMapping(path)
    public BookingDto findBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                  @PathVariable("id") Long bookingId) {
        return bookingService.findBooking(bookingId, userId);
    }

    @GetMapping
    public Collection<BookingDto> findAllBookingsByUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                        @RequestParam(name = "state", defaultValue = "ALL") String state) {
        return bookingService.findAllBookingsByUser(userId, state);
    }

    @GetMapping("/owner")
    public Collection<BookingDto> findAllBookingsByOwnerItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                              @RequestParam(name = "state", defaultValue = "ALL") String state) {
        return bookingService.findAllBookingsByOwnerItems(userId, state);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto create(@RequestHeader("X-Sharer-User-Id") Long userId,
                             @Valid @RequestBody NewBookingRequest booking) {
        return bookingService.create(userId, booking);
    }

    @PutMapping
    public BookingDto update(@Valid @RequestBody UpdateBookingRequest newBooking) {
        return bookingService.update(newBooking);
    }

    @DeleteMapping(path)
    public void delete(@PathVariable("id") Long bookingId) {
        bookingService.delete(bookingId);
    }

    @PatchMapping(path)
    public BookingDto approveBooking(@PathVariable("id") Long bookingId,
                                     @RequestHeader("X-Sharer-User-Id") Long userId,
                                     @RequestParam(name = "approved", defaultValue = "false") Boolean approved) {
        return bookingService.approveBooking(bookingId, userId, approved);
    }
}
