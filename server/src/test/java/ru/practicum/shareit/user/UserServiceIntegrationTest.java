package ru.practicum.shareit;

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
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDate;
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
class UserServiceIntegrationTest {
    private final EntityManager em;
    private final UserService userService;

    private void createUserInDb() {
        Query userQuery = em.createNativeQuery("INSERT INTO Users (id, name, email, birthday) " +
                "VALUES (:id , :name , :email , :birthday);");
        userQuery.setParameter("id", "1");
        userQuery.setParameter("name", "Ivan Ivanov");
        userQuery.setParameter("email", "ivan@email");
        userQuery.setParameter("birthday", LocalDate.of(2021, 7, 1));
        userQuery.executeUpdate();
    }

    @Test
    void createTest() {
        NewUserRequest newUser = new NewUserRequest("john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3));

        userService.create(newUser);

        TypedQuery<User> query = em.createQuery("Select u from User u where u.name like :nameUser", User.class);
        User user = query.setParameter("nameUser", newUser.getName()).getSingleResult();

        MatcherAssert.assertThat(user.getId(), CoreMatchers.notNullValue());
        MatcherAssert.assertThat(user.getName(), Matchers.equalTo(newUser.getName()));
        MatcherAssert.assertThat(user.getEmail(), Matchers.equalTo(newUser.getEmail()));
        MatcherAssert.assertThat(user.getBirthday(), Matchers.equalTo(newUser.getBirthday()));
    }

    @Test
    void getUserTest() {
        createUserInDb();

        UserDto loadUsers = userService.findUser(1L);

        MatcherAssert.assertThat(loadUsers.getId(), CoreMatchers.notNullValue());
        MatcherAssert.assertThat(loadUsers.getName(), Matchers.equalTo("Ivan Ivanov"));
        MatcherAssert.assertThat(loadUsers.getEmail(), Matchers.equalTo("ivan@email"));
        MatcherAssert.assertThat(loadUsers.getBirthday(), Matchers.equalTo(LocalDate.of(2021, 7, 1)));
    }

    @Test
    void testGetAllUsers() {
        List<NewUserRequest> newUsers = List.of(
                makeNewUser("ivan@email", "Ivan Ivanov", LocalDate.of(2021, 7, 1)),
                makeNewUser("petr@email", "Pet Petrovr", LocalDate.of(2022, 8, 2)),
                makeNewUser("vasilii@email", "Vasilii Vasiliev", LocalDate.of(2023, 9, 3)));

        for (NewUserRequest user : newUsers) {
            userService.create(user);
        }

        Collection<UserDto> loadUsers = userService.getUsers();

        assertThat(loadUsers, hasSize(newUsers.size()));
        for (NewUserRequest user : newUsers) {
            assertThat(loadUsers, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("name", equalTo(user.getName())),
                    hasProperty("email", equalTo(user.getEmail())),
                    hasProperty("birthday", equalTo(user.getBirthday()))
            )));
        }
    }

    @Test
    void updateUserTest() {
        createUserInDb();

        UpdateUserRequest updUser = new UpdateUserRequest(1L, "john.doe1@mail.com", "John Doe", LocalDate.of(2023, 7, 3));
        UserDto findUser = userService.update(1L, updUser);

        MatcherAssert.assertThat(findUser.getId(), CoreMatchers.notNullValue());
        MatcherAssert.assertThat(findUser.getName(), Matchers.equalTo(updUser.getName()));
        MatcherAssert.assertThat(findUser.getEmail(), Matchers.equalTo(updUser.getEmail()));
        MatcherAssert.assertThat(findUser.getBirthday(), Matchers.equalTo(updUser.getBirthday()));
    }

    @Test
    void deleteUserTest() {
        createUserInDb();

        userService.delete(1L);

        TypedQuery<User> selectQuery = em.createQuery("Select u from User u where u.name like :nameUser", User.class);
        List<User> users = selectQuery.setParameter("nameUser", "Ivan Ivanov").getResultList();

        MatcherAssert.assertThat(users, CoreMatchers.equalTo(new ArrayList<>()));
    }

    private NewUserRequest makeNewUser(String email, String name, LocalDate birthday) {
        NewUserRequest dto = new NewUserRequest();
        dto.setName(name);
        dto.setEmail(email);
        dto.setBirthday(birthday);

        return dto;
    }
}
