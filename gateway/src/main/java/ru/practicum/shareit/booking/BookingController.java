package ru.practicum.shareit.booking;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.dto.UpdateBookingRequest;
import ru.practicum.shareit.enums.States;

@Controller
@Validated
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingClient bookingClient;
    private final String id = "/{booking-id}";
    private final String owner = "/owner";

    private final String headerUserId = "X-Sharer-User-Id";
    private final String pvBookingId = "booking-id";

    @GetMapping(id)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> findBooking(@RequestHeader(headerUserId) Long userId,
                                              @PathVariable(pvBookingId) Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @PostMapping
    public ResponseEntity<Object> bookItem(@RequestHeader(headerUserId) Long userId,
                                           @Valid @RequestBody NewBookingRequest requestDto) {
        log.info("Creating booking {}, userId={}", requestDto, userId);
        return bookingClient.bookItem(userId, requestDto);
    }

    @GetMapping
    public ResponseEntity<Object> findAllBookingsByUser(@RequestHeader(headerUserId) Long userId,
                                                        @RequestParam(name = "state", defaultValue = "ALL") String stateParam) {
        log.info("Get bookings of user with Id={}", userId);
        States state = States.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        return bookingClient.getBookings(null, userId, state);
    }

    @GetMapping(owner)
    public ResponseEntity<Object> findAllBookingsByOwnerItems(@RequestHeader(headerUserId) Long userId,
                                                              @RequestParam(name = "state", defaultValue = "ALL") String stateParam) {
        log.info("Get items bookings of owner with Id={}", userId);
        States state = States.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        return bookingClient.getBookings(owner, userId, state);
    }

    @PutMapping
    public ResponseEntity<Object> updateBooking(@RequestHeader(headerUserId) Long userId,
                                                @RequestBody UpdateBookingRequest newBooking) {
        log.info("Update booking of user with Id={}", userId);
        return bookingClient.updateBooking(userId, newBooking);
    }

    @DeleteMapping(id)
    public ResponseEntity<Object> deleteBooking(@PathVariable(pvBookingId) Long bookingId) {
        log.info("Delete booking with Id={}", bookingId);
        return bookingClient.deleteBooking(bookingId);
    }

    @PatchMapping(id)
    public ResponseEntity<Object> approveBooking(@PathVariable(pvBookingId) Long bookingId,
                                                 @RequestHeader(headerUserId) Long userId,
                                                 @RequestParam(name = "approved", defaultValue = "false") Boolean approved) {
        log.info("Set \"approve\" to booking with Id={}, of user with Id={}", bookingId, userId);
        return bookingClient.approveBooking(bookingId, userId, approved);
    }
}