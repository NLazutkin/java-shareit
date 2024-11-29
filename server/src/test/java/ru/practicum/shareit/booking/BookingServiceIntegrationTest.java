package ru.practicum.shareit.booking;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.dto.UpdateBookingRequest;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.enums.States;
import ru.practicum.shareit.enums.Statuses;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(
        properties = "spring.datasource.username=shareit",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
class BookingServiceIntegrationTest {
    private final EntityManager em;
    private final BookingService bookingService;

    private void createItemInDb() {
        Query itemQuery = em.createNativeQuery("INSERT INTO Items (id, name, description, available, owner_id, request_id) " +
                "VALUES (:id , :name , :description , :available , :owner_id , :request_id);");
        itemQuery.setParameter("id", "1");
        itemQuery.setParameter("name", "name");
        itemQuery.setParameter("description", "description");
        itemQuery.setParameter("available", Boolean.TRUE);
        itemQuery.setParameter("owner_id", "1");
        itemQuery.setParameter("request_id", "1");
        itemQuery.executeUpdate();
    }

    private void createUser1InDb() {
        Query user1Query = em.createNativeQuery("INSERT INTO Users (id, name, email, birthday) " +
                "VALUES (:id , :name , :email , :birthday);");
        user1Query.setParameter("id", "1");
        user1Query.setParameter("name", "Ivan Ivanov");
        user1Query.setParameter("email", "ivan@email");
        user1Query.setParameter("birthday", LocalDate.of(2021, 7, 1));
        user1Query.executeUpdate();
    }

    private void createUser2InDb() {
        Query user2Query = em.createNativeQuery("INSERT INTO Users (id, name, email, birthday) " +
                "VALUES (:id , :name , :email , :birthday);");
        user2Query.setParameter("id", "2");
        user2Query.setParameter("name", "Petr Petrov");
        user2Query.setParameter("email", "petr@email");
        user2Query.setParameter("birthday", LocalDate.of(2021, 7, 1));
        user2Query.executeUpdate();
    }

    private void createBookingInDb() {
        Query bookingQuery = em.createNativeQuery("INSERT INTO Bookings (id, start_date, end_date, item_id, status, booker_id) " +
                "VALUES (:id , :startDate , :eneDate , :itemId , :status , :bookerId);");
        bookingQuery.setParameter("id", "1");
        bookingQuery.setParameter("startDate", LocalDateTime.of(2024, 7, 1, 19, 30, 15));
        bookingQuery.setParameter("eneDate", LocalDateTime.of(2024, 7, 2, 19, 30, 15));
        bookingQuery.setParameter("itemId", 1L);
        bookingQuery.setParameter("status", Statuses.APPROVED);
        bookingQuery.setParameter("bookerId", 1L);
        bookingQuery.executeUpdate();
    }

    private void createCurrentBookingInDb() {
        Query currentBookingQuery = em.createNativeQuery("INSERT INTO Bookings (id, start_date, end_date, item_id, status, booker_id) " +
                "VALUES (:id , :startDate , :eneDate , :itemId , :status , :bookerId);");
        currentBookingQuery.setParameter("id", "1");
        currentBookingQuery.setParameter("startDate", LocalDateTime.of(2022, 7, 1, 19, 30, 15));
        currentBookingQuery.setParameter("eneDate", LocalDateTime.of(2026, 7, 2, 19, 30, 15));
        currentBookingQuery.setParameter("itemId", 1L);
        currentBookingQuery.setParameter("status", Statuses.APPROVED);
        currentBookingQuery.setParameter("bookerId", 1L);
        currentBookingQuery.executeUpdate();
    }

    private void createLastBookingInDb() {
        Query lastBookingQuery = em.createNativeQuery("INSERT INTO Bookings (id, start_date, end_date, item_id, status, booker_id) " +
                "VALUES (:id , :startDate , :eneDate , :itemId , :status , :bookerId);");
        lastBookingQuery.setParameter("id", "2");
        lastBookingQuery.setParameter("startDate", LocalDateTime.of(2024, 7, 1, 19, 30, 15));
        lastBookingQuery.setParameter("eneDate", LocalDateTime.of(2024, 7, 2, 19, 30, 15));
        lastBookingQuery.setParameter("itemId", 1L);
        lastBookingQuery.setParameter("status", Statuses.APPROVED);
        lastBookingQuery.setParameter("bookerId", 1L);
        lastBookingQuery.executeUpdate();
    }

    private void createNextBookingInDb() {
        Query nextBookingQuery = em.createNativeQuery("INSERT INTO Bookings (id, start_date, end_date, item_id, status, booker_id) " +
                "VALUES (:id , :startDate , :eneDate , :itemId , :status , :bookerId);");
        nextBookingQuery.setParameter("id", "3");
        nextBookingQuery.setParameter("startDate", LocalDateTime.of(2024, 12, 1, 19, 30, 15));
        nextBookingQuery.setParameter("eneDate", LocalDateTime.of(2024, 12, 2, 19, 30, 15));
        nextBookingQuery.setParameter("itemId", 1L);
        nextBookingQuery.setParameter("status", Statuses.APPROVED);
        nextBookingQuery.setParameter("bookerId", 1L);
        nextBookingQuery.executeUpdate();
    }

    @Test
    void createBookingTest() {
        createUser1InDb();
        createUser2InDb();
        createItemInDb();

        NewBookingRequest newRequest = new NewBookingRequest(LocalDateTime.of(2024, 7, 1, 19, 30, 15),
                LocalDateTime.of(2024, 7, 2, 19, 30, 15), 1L, 2L);

        BookingDto booking = bookingService.create(2L, newRequest);

        assertThat(booking.getId(), CoreMatchers.notNullValue());
        assertThat(booking.getStart(), Matchers.equalTo(newRequest.getStart()));
        assertThat(booking.getEnd(), Matchers.equalTo(newRequest.getEnd()));
        assertThat(booking.getItem(), CoreMatchers.notNullValue());
        assertThat(booking.getItem().getClass(), CoreMatchers.equalTo(ItemDto.class));
        assertThat(booking.getStatus(), Matchers.equalTo(Statuses.WAITING));
        assertThat(booking.getBooker(), CoreMatchers.notNullValue());
        assertThat(booking.getBooker().getClass(), CoreMatchers.equalTo(UserDto.class));
    }

    @Test
    void getBookingTest() {
        createUser1InDb();
        createUser2InDb();
        createItemInDb();
        createBookingInDb();

        BookingDto loadItem = bookingService.findBooking(1L, 1L);

        assertThat(loadItem.getId(), CoreMatchers.notNullValue());
        assertThat(loadItem.getStart(), Matchers.equalTo(LocalDateTime.of(2024, 7, 1, 19, 30, 15)));
        assertThat(loadItem.getEnd(), Matchers.equalTo(LocalDateTime.of(2024, 7, 2, 19, 30, 15)));
        assertThat(loadItem.getItem(), CoreMatchers.notNullValue());
        assertThat(loadItem.getItem().getClass(), CoreMatchers.equalTo(ItemDto.class));
        assertThat(loadItem.getStatus(), Matchers.equalTo(Statuses.APPROVED));
        assertThat(loadItem.getBooker(), CoreMatchers.notNullValue());
        assertThat(loadItem.getBooker().getClass(), CoreMatchers.equalTo(UserDto.class));
    }

    @Test
    void findAllBookingsByUserAndStateAllTest() {
        createUser1InDb();
        createUser2InDb();
        createItemInDb();
        createBookingInDb();

        Collection<BookingDto> loadBookings = bookingService.findAllBookingsByUser(1L, String.valueOf(States.ALL));

        assertThat(loadBookings, hasSize(1));
        for (BookingDto booking : loadBookings) {
            assertThat(loadBookings, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("start", equalTo(booking.getStart())),
                    hasProperty("end", equalTo(booking.getEnd())),
                    hasProperty("item", notNullValue()),
                    hasProperty("status", equalTo(Statuses.APPROVED)),
                    hasProperty("booker", notNullValue())
            )));
        }
    }

    @Test
    void findAllBookingsByUserAndStateCurrentTest() {
        createUser1InDb();
        createUser2InDb();
        createItemInDb();
        createCurrentBookingInDb();

        Collection<BookingDto> loadBookings = bookingService.findAllBookingsByUser(1L, String.valueOf(States.CURRENT));

        assertThat(loadBookings, hasSize(1));
        for (BookingDto booking : loadBookings) {
            assertThat(loadBookings, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("start", equalTo(booking.getStart())),
                    hasProperty("end", equalTo(booking.getEnd())),
                    hasProperty("item", notNullValue()),
                    hasProperty("status", equalTo(Statuses.APPROVED)),
                    hasProperty("booker", notNullValue())
            )));
        }
    }

    @Test
    void findAllBookingsByUserAndStatePastTest() {
        createUser1InDb();
        createUser2InDb();
        createItemInDb();
        createLastBookingInDb();

        Collection<BookingDto> loadBookings = bookingService.findAllBookingsByUser(1L, String.valueOf(States.PAST));

        assertThat(loadBookings, hasSize(1));
        for (BookingDto booking : loadBookings) {
            assertThat(loadBookings, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("start", equalTo(booking.getStart())),
                    hasProperty("end", equalTo(booking.getEnd())),
                    hasProperty("item", notNullValue()),
                    hasProperty("status", equalTo(Statuses.APPROVED)),
                    hasProperty("booker", notNullValue())
            )));
        }
    }

    @Test
    void findAllBookingsByUserAndStateFutureTest() {
        createUser1InDb();
        createUser2InDb();
        createItemInDb();
        createNextBookingInDb();

        Collection<BookingDto> loadBookings = bookingService.findAllBookingsByUser(1L, String.valueOf(States.FUTURE));

        assertThat(loadBookings, hasSize(1));
        for (BookingDto booking : loadBookings) {
            assertThat(loadBookings, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("start", equalTo(booking.getStart())),
                    hasProperty("end", equalTo(booking.getEnd())),
                    hasProperty("item", notNullValue()),
                    hasProperty("status", equalTo(Statuses.APPROVED)),
                    hasProperty("booker", notNullValue())
            )));
        }
    }

    @Test
    void findAllBookingsByUserAndStateWaitingTest() {
        createUser1InDb();
        createUser2InDb();
        createItemInDb();

        NewBookingRequest newRequest = new NewBookingRequest(LocalDateTime.of(2024, 7, 1, 19, 30, 15),
                LocalDateTime.of(2024, 7, 2, 19, 30, 15), 1L, 2L);

        BookingDto findBooking = bookingService.create(2L, newRequest);

        Collection<BookingDto> loadBookings = bookingService.findAllBookingsByUser(2L, String.valueOf(States.WAITING));

        assertThat(loadBookings, hasSize(1));
        for (BookingDto booking : loadBookings) {
            assertThat(loadBookings, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("start", equalTo(booking.getStart())),
                    hasProperty("end", equalTo(booking.getEnd())),
                    hasProperty("item", notNullValue()),
                    hasProperty("status", equalTo(Statuses.WAITING)),
                    hasProperty("booker", notNullValue())
            )));
        }
    }

    @Test
    void findAllBookingsByUserAndStateRejectedTest() {
        createUser1InDb();
        createUser2InDb();
        createItemInDb();

        NewBookingRequest newRequest = new NewBookingRequest(LocalDateTime.of(2024, 7, 1, 19, 30, 15),
                LocalDateTime.of(2024, 7, 2, 19, 30, 15), 1L, 2L);
        BookingDto newBooking = bookingService.create(2L, newRequest);

        bookingService.approveBooking(newBooking.getId(), newBooking.getItem().getOwnerId(), Boolean.FALSE);

        Collection<BookingDto> loadBookings = bookingService.findAllBookingsByUser(2L, String.valueOf(States.REJECTED));

        assertThat(loadBookings, hasSize(1));
        for (BookingDto booking : loadBookings) {
            assertThat(loadBookings, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("start", equalTo(booking.getStart())),
                    hasProperty("end", equalTo(booking.getEnd())),
                    hasProperty("item", notNullValue()),
                    hasProperty("status", equalTo(Statuses.REJECTED)),
                    hasProperty("booker", notNullValue())
            )));
        }
    }

    @Test
    void findAllBookingsByItemOwnerIdAndStateAllTest() {
        createUser1InDb();
        createUser2InDb();
        createItemInDb();
        createBookingInDb();

        Collection<BookingDto> loadBookings = bookingService.findAllBookingsByOwnerItems(1L, String.valueOf(States.ALL));

        assertThat(loadBookings, hasSize(1));
        for (BookingDto booking : loadBookings) {
            assertThat(loadBookings, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("start", equalTo(booking.getStart())),
                    hasProperty("end", equalTo(booking.getEnd())),
                    hasProperty("item", notNullValue()),
                    hasProperty("status", equalTo(Statuses.APPROVED)),
                    hasProperty("booker", notNullValue())
            )));
        }
    }

    @Test
    void findAllBookingsByItemOwnerIdAndStateCurrentTest() {
        createUser1InDb();
        createUser2InDb();
        createItemInDb();
        createCurrentBookingInDb();

        Collection<BookingDto> loadBookings = bookingService.findAllBookingsByOwnerItems(1L, String.valueOf(States.CURRENT));

        assertThat(loadBookings, hasSize(1));
        for (BookingDto booking : loadBookings) {
            assertThat(loadBookings, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("start", equalTo(booking.getStart())),
                    hasProperty("end", equalTo(booking.getEnd())),
                    hasProperty("item", notNullValue()),
                    hasProperty("status", equalTo(Statuses.APPROVED)),
                    hasProperty("booker", notNullValue())
            )));
        }
    }

    @Test
    void findAllBookingsByItemOwnerIdAndStatePastTest() {
        createUser1InDb();
        createUser2InDb();
        createItemInDb();
        createLastBookingInDb();

        Collection<BookingDto> loadBookings = bookingService.findAllBookingsByOwnerItems(1L, String.valueOf(States.PAST));

        assertThat(loadBookings, hasSize(1));
        for (BookingDto booking : loadBookings) {
            assertThat(loadBookings, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("start", equalTo(booking.getStart())),
                    hasProperty("end", equalTo(booking.getEnd())),
                    hasProperty("item", notNullValue()),
                    hasProperty("status", equalTo(Statuses.APPROVED)),
                    hasProperty("booker", notNullValue())
            )));
        }
    }

    @Test
    void findAllBookingsByItemOwnerIdAndStateFutureTest() {
        createUser1InDb();
        createUser2InDb();
        createItemInDb();
        createNextBookingInDb();

        Collection<BookingDto> loadBookings = bookingService.findAllBookingsByOwnerItems(1L, String.valueOf(States.FUTURE));

        assertThat(loadBookings, hasSize(1));
        for (BookingDto booking : loadBookings) {
            assertThat(loadBookings, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("start", equalTo(booking.getStart())),
                    hasProperty("end", equalTo(booking.getEnd())),
                    hasProperty("item", notNullValue()),
                    hasProperty("status", equalTo(Statuses.APPROVED)),
                    hasProperty("booker", notNullValue())
            )));
        }
    }

    @Test
    void findAllBookingsByItemOwnerIdAndStateWaitingTest() {
        createUser1InDb();
        createUser2InDb();
        createItemInDb();

        NewBookingRequest newRequest = new NewBookingRequest(LocalDateTime.of(2024, 7, 1, 19, 30, 15),
                LocalDateTime.of(2024, 7, 2, 19, 30, 15), 1L, 2L);

        BookingDto findBooking = bookingService.create(2L, newRequest);

        Collection<BookingDto> loadBookings = bookingService.findAllBookingsByOwnerItems(1L, String.valueOf(States.WAITING));

        assertThat(loadBookings, hasSize(1));
        for (BookingDto booking : loadBookings) {
            assertThat(loadBookings, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("start", equalTo(booking.getStart())),
                    hasProperty("end", equalTo(booking.getEnd())),
                    hasProperty("item", notNullValue()),
                    hasProperty("status", equalTo(Statuses.WAITING)),
                    hasProperty("booker", notNullValue())
            )));
        }
    }

    @Test
    void findAllBookingsByItemOwnerIdAndStateRejectedTest() {
        createUser1InDb();
        createUser2InDb();
        createItemInDb();

        NewBookingRequest newRequest = new NewBookingRequest(LocalDateTime.of(2024, 7, 1, 19, 30, 15),
                LocalDateTime.of(2024, 7, 2, 19, 30, 15), 1L, 2L);
        BookingDto newBooking = bookingService.create(2L, newRequest);

        bookingService.approveBooking(newBooking.getId(), newBooking.getItem().getOwnerId(), Boolean.FALSE);

        Collection<BookingDto> loadBookings = bookingService.findAllBookingsByOwnerItems(1L, String.valueOf(States.REJECTED));

        assertThat(loadBookings, hasSize(1));
        for (BookingDto booking : loadBookings) {
            assertThat(loadBookings, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("start", equalTo(booking.getStart())),
                    hasProperty("end", equalTo(booking.getEnd())),
                    hasProperty("item", notNullValue()),
                    hasProperty("status", equalTo(Statuses.REJECTED)),
                    hasProperty("booker", notNullValue())
            )));
        }
    }

    @Test
    void updateBookingTest() {
        createUser1InDb();
        createUser2InDb();
        createItemInDb();
        createBookingInDb();

        UpdateBookingRequest newBooking = new UpdateBookingRequest(1L, LocalDateTime.of(2026, 7, 1, 19, 30, 15),
                LocalDateTime.of(2026, 7, 2, 19, 30, 15), 1L, Statuses.REJECTED, 1L);
        BookingDto updBooking = bookingService.update(1L, newBooking);

        MatcherAssert.assertThat(newBooking.getId(), CoreMatchers.equalTo(updBooking.getId()));
        MatcherAssert.assertThat(newBooking.getStart(), Matchers.equalTo(updBooking.getStart()));
        MatcherAssert.assertThat(newBooking.getEnd(), Matchers.equalTo(updBooking.getEnd()));
        MatcherAssert.assertThat(newBooking.getItemId(), Matchers.equalTo(updBooking.getItem().getId()));
        MatcherAssert.assertThat(newBooking.getStatus(), Matchers.equalTo(updBooking.getStatus()));
        MatcherAssert.assertThat(newBooking.getBookerId(), Matchers.equalTo(updBooking.getBooker().getId()));
    }

    @Test
    void deleteItemTest() {
        createUser1InDb();
        createUser2InDb();
        createItemInDb();
        createBookingInDb();

        bookingService.delete(1L);

        TypedQuery<Booking> selectQuery = em.createQuery("Select b from Booking b where b.id = :number", Booking.class);
        List<Booking> users = selectQuery.setParameter("number", "1").getResultList();

        MatcherAssert.assertThat(users, CoreMatchers.equalTo(new ArrayList<>()));
    }

    @Test
    void approveBookingTest() {
        createUser1InDb();
        createUser2InDb();
        createItemInDb();

        NewBookingRequest newRequest = new NewBookingRequest(LocalDateTime.of(2024, 7, 1, 19, 30, 15),
                LocalDateTime.of(2024, 7, 2, 19, 30, 15), 1L, 2L);
        BookingDto newBooking = bookingService.create(2L, newRequest);

        BookingDto approvedBooking = bookingService.approveBooking(newBooking.getId(), newBooking.getItem().getOwnerId(), Boolean.TRUE);

        assertThat(approvedBooking.getId(), Matchers.equalTo(newBooking.getId()));
        assertThat(approvedBooking.getStart(), Matchers.equalTo(approvedBooking.getStart()));
        assertThat(approvedBooking.getEnd(), Matchers.equalTo(approvedBooking.getEnd()));
        assertThat(approvedBooking.getItem(), CoreMatchers.notNullValue());
        assertThat(approvedBooking.getItem().getClass(), CoreMatchers.equalTo(ItemDto.class));
        assertThat(approvedBooking.getStatus(), Matchers.equalTo(Statuses.APPROVED));
        assertThat(approvedBooking.getBooker(), CoreMatchers.notNullValue());
        assertThat(approvedBooking.getBooker().getClass(), CoreMatchers.equalTo(UserDto.class));
    }

    @Test
    void notApproveBookingTest() {
        createUser1InDb();
        createUser2InDb();
        createItemInDb();

        NewBookingRequest newRequest = new NewBookingRequest(LocalDateTime.of(2024, 7, 1, 19, 30, 15),
                LocalDateTime.of(2024, 7, 2, 19, 30, 15), 1L, 2L);
        BookingDto newBooking = bookingService.create(2L, newRequest);

        BookingDto approvedBooking = bookingService.approveBooking(newBooking.getId(), newBooking.getItem().getOwnerId(), Boolean.FALSE);

        assertThat(approvedBooking.getId(), Matchers.equalTo(newBooking.getId()));
        assertThat(approvedBooking.getStart(), Matchers.equalTo(approvedBooking.getStart()));
        assertThat(approvedBooking.getEnd(), Matchers.equalTo(approvedBooking.getEnd()));
        assertThat(approvedBooking.getItem(), CoreMatchers.notNullValue());
        assertThat(approvedBooking.getItem().getClass(), CoreMatchers.equalTo(ItemDto.class));
        assertThat(approvedBooking.getStatus(), Matchers.equalTo(Statuses.REJECTED));
        assertThat(approvedBooking.getBooker(), CoreMatchers.notNullValue());
        assertThat(approvedBooking.getBooker().getClass(), CoreMatchers.equalTo(UserDto.class));
    }
}
