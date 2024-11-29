package ru.practicum.shareit.request;

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
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewRequest;
import ru.practicum.shareit.request.dto.UpdateRequest;
import ru.practicum.shareit.request.entity.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(
        properties = "spring.datasource.username=shareit",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ItemRequestServiceIntegrationTest {
    private final EntityManager em;
    private final ItemRequestService itemRequestService;

    private void createUserInDb() {
        Query userQuery = em.createNativeQuery("INSERT INTO Users (id, name, email, birthday) " +
                "VALUES (:id , :name , :email , :birthday);");
        userQuery.setParameter("id", "1");
        userQuery.setParameter("name", "Ivan Ivanov");
        userQuery.setParameter("email", "ivan@email");
        userQuery.setParameter("birthday", LocalDate.of(2021, 7, 1));
        userQuery.executeUpdate();
    }

    private void createRequestInDb() {
        Query requestQuery = em.createNativeQuery("INSERT INTO Requests (id, description, requestor_id, created) " +
                "VALUES (:id , :description , :requestor_id , :created);");
        requestQuery.setParameter("id", "1");
        requestQuery.setParameter("description", "description");
        requestQuery.setParameter("requestor_id", "1");
        requestQuery.setParameter("created", LocalDateTime.of(2022, 7, 3, 19, 30, 1));
        requestQuery.executeUpdate();
    }

    @Test
    void createItemRequestTest() {
        createUserInDb();

        NewRequest newRequest = new NewRequest("description", 1L);

        ItemRequestDto findItemRequest = itemRequestService.create(1L, newRequest);

        MatcherAssert.assertThat(findItemRequest.getId(), CoreMatchers.notNullValue());
        MatcherAssert.assertThat(findItemRequest.getDescription(), Matchers.equalTo(newRequest.getDescription()));
        MatcherAssert.assertThat(findItemRequest.getRequestorId(), CoreMatchers.notNullValue());
    }

    @Test
    void getItemRequestTest() {
        createUserInDb();
        createRequestInDb();

        ItemRequestDto loadRequest = itemRequestService.findItemRequest(1L);

        MatcherAssert.assertThat(loadRequest.getId(), CoreMatchers.notNullValue());
        MatcherAssert.assertThat(loadRequest.getDescription(), Matchers.equalTo("description"));
        MatcherAssert.assertThat(loadRequest.getRequestorId(), CoreMatchers.notNullValue());
        MatcherAssert.assertThat(loadRequest.getCreated(), Matchers.equalTo(LocalDateTime.of(2022, 7, 3, 19, 30, 1)));
    }

    @Test
    void testFindAllByRequestorId() {
        createUserInDb();

        List<NewRequest> itemRequests = List.of(
                makeNewItemRequest("description1", 1L),
                makeNewItemRequest("description2", 1L),
                makeNewItemRequest("description3", 1L)
        );

        for (NewRequest itemRequest : itemRequests) {
            itemRequestService.create(1L, itemRequest);
        }

        Collection<ItemRequestDto> loadRequests = itemRequestService.findAllByRequestorId(1L);

        assertThat(loadRequests, hasSize(itemRequests.size()));
        for (NewRequest itemRequest : itemRequests) {
            assertThat(loadRequests, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("description", equalTo(itemRequest.getDescription())),
                    hasProperty("requestorId", equalTo(itemRequest.getRequestorId())),
                    hasProperty("created", notNullValue()),
                    hasProperty("items", CoreMatchers.equalTo(new ArrayList<>()))
            )));
        }
    }

    @Test
    void testFindAllOfAnotherRequestors() {
        createUserInDb();

        List<NewRequest> itemRequests = List.of(
                makeNewItemRequest("description1", 1L),
                makeNewItemRequest("description2", 1L),
                makeNewItemRequest("description3", 1L)
        );

        for (NewRequest itemRequest : itemRequests) {
            itemRequestService.create(1L, itemRequest);
        }

        Collection<ItemRequestDto> loadRequests = itemRequestService.findAllOfAnotherRequestors(2L);

        assertThat(loadRequests, hasSize(itemRequests.size()));
        for (NewRequest itemRequest : itemRequests) {
            assertThat(loadRequests, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("description", equalTo(itemRequest.getDescription())),
                    hasProperty("requestorId", equalTo(itemRequest.getRequestorId())),
                    hasProperty("created", notNullValue()),
                    hasProperty("items", CoreMatchers.equalTo(new ArrayList<>()))
            )));
        }
    }

    @Test
    void updateItemRequestTest() {
        createUserInDb();
        createRequestInDb();

        UpdateRequest updItemRequest = new UpdateRequest(1L, "description1", 1L,
                LocalDateTime.of(2023, 7, 3, 19, 30, 1));
        ItemRequestDto findItemRequest = itemRequestService.update(1L, updItemRequest);

        MatcherAssert.assertThat(findItemRequest.getId(), CoreMatchers.notNullValue());
        MatcherAssert.assertThat(findItemRequest.getDescription(), Matchers.equalTo(updItemRequest.getDescription()));
        MatcherAssert.assertThat(findItemRequest.getRequestorId(), CoreMatchers.notNullValue());
    }

    @Test
    void deleteItemRequestTest() {
        createUserInDb();
        createRequestInDb();

        itemRequestService.delete(1L);

        TypedQuery<ItemRequest> selectQuery = em.createQuery("Select r from ItemRequest r where r.description like :description", ItemRequest.class);
        List<ItemRequest> users = selectQuery.setParameter("description", "description").getResultList();

        MatcherAssert.assertThat(users, CoreMatchers.equalTo(new ArrayList<>()));
    }

    private NewRequest makeNewItemRequest(String description, Long requestorId) {
        NewRequest dto = new NewRequest();
        dto.setDescription(description);
        dto.setRequestorId(requestorId);

        return dto;
    }
}
