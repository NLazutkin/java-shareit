package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.AdvancedItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ItemMapperTest {
    private final LocalDateTime now = LocalDateTime.now();
    private final LocalDateTime nextDay = LocalDateTime.now().plusDays(1);

    private final User user = new User(1L, "john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3));

    private final CommentDto commentDto = new CommentDto(1L, "text", 1L, "John Doe", nextDay);
    private final List<CommentDto> comments = List.of(commentDto);

    private final NewItemRequest newItem = new NewItemRequest("name", "description", Boolean.TRUE, 1L, 1L);
    private final UpdateItemRequest updItem = new UpdateItemRequest(1L, "name", "description", Boolean.TRUE, 1L, 1L);
    private final UpdateItemRequest updEmptyItem = new UpdateItemRequest(1L, "", "", null, 1L, 1L);
    private final ItemDto dto = new ItemDto(1L, "name", "description", Boolean.TRUE, 1L, 1L);

    private final AdvancedItemDto advItemDto = new AdvancedItemDto(1L, "name", "description", Boolean.TRUE, now, nextDay, comments, 1L, 1L);
    private final AdvancedItemDto advItemDtoNullDates = new AdvancedItemDto(1L, "name", "description", Boolean.TRUE, null, null, comments, 1L, 1L);

    private final Item item = new Item(1L, "name", "description", Boolean.TRUE, user, 1L);
    private final Comment comment = new Comment(1L, "text", item, user, nextDay);

    private final NewItemRequest newItemNoRequest = new NewItemRequest("name", "description", Boolean.TRUE, 1L, null);
    private final Item itemNoRequest = new Item(1L, "name", "description", Boolean.TRUE, user, null);
    private final AdvancedItemDto advItemDtoNoRequest = new AdvancedItemDto(1L, "name", "description", Boolean.TRUE, now, nextDay, comments, 1L, null);
    private final AdvancedItemDto advItemDtoNullDatesNoRequest = new AdvancedItemDto(1L, "name", "description", Boolean.TRUE, null, null, comments, 1L, null);
    private final ItemDto dtoNoRequest = new ItemDto(1L, "name", "description", Boolean.TRUE, 1L, null);

    @Test
    public void toItemDtoTest() {
        ItemDto itemDto = ItemMapper.mapToItemDto(item);
        assertThat(itemDto, equalTo(dto));
    }

    @Test
    public void toItemDtoWithOutRequestTest() {
        ItemDto itemDto = ItemMapper.mapToItemDto(itemNoRequest);
        assertThat(itemDto, equalTo(dtoNoRequest));
    }

    @Test
    public void toAdvancedItemDtoTest() {
        AdvancedItemDto advDto = ItemMapper.mapToAdvancedItemDto(item, List.of(comment));
        assertThat(advDto, equalTo(advItemDtoNullDates));
    }

    @Test
    public void toAdvancedItemDtoWithOutRequestTest() {
        AdvancedItemDto advDto = ItemMapper.mapToAdvancedItemDto(itemNoRequest, List.of(comment));
        assertThat(advDto, equalTo(advItemDtoNullDatesNoRequest));
    }

    @Test
    public void toAdvancedItemDtoWithDatesTest() {
        AdvancedItemDto advDto = ItemMapper.mapToAdvancedItemDto(item, List.of(comment), Optional.of(now), Optional.of(nextDay));
        assertThat(advDto, equalTo(advItemDto));
    }

    @Test
    public void toAdvancedItemDtoWithDatesWithOutRequestTest() {
        AdvancedItemDto advDto = ItemMapper.mapToAdvancedItemDto(itemNoRequest, List.of(comment), Optional.of(now), Optional.of(nextDay));
        assertThat(advDto, equalTo(advItemDtoNoRequest));
    }

    @Test
    public void toItemTest() {
        Item i = ItemMapper.mapToItem(user, newItem);
        assertThat(i.getName(), equalTo(item.getName()));
        assertThat(i.getDescription(), equalTo(item.getDescription()));
        assertThat(i.getAvailable(), equalTo(item.getAvailable()));
        assertThat(i.getUser(), equalTo(user));
        assertThat(i.getRequestId(), equalTo(item.getRequestId()));
    }

    @Test
    public void toItemTestWithOutRequestTest() {
        Item i = ItemMapper.mapToItem(user, newItemNoRequest);
        assertThat(i.getName(), equalTo(item.getName()));
        assertThat(i.getDescription(), equalTo(item.getDescription()));
        assertThat(i.getAvailable(), equalTo(item.getAvailable()));
        assertThat(i.getUser(), equalTo(user));
        assertThat(i.getRequestId(), equalTo(null));
    }

    @Test
    public void updateItemFieldsTest() {
        Item i = ItemMapper.updateItemFields(item, updItem);
        assertThat(i.getName(), equalTo(item.getName()));
        assertThat(i.getDescription(), equalTo(item.getDescription()));
        assertThat(i.getAvailable(), equalTo(item.getAvailable()));
        assertThat(i.getUser(), equalTo(user));
        assertThat(i.getRequestId(), equalTo(item.getRequestId()));
    }

    @Test
    public void updateItemEmptyFieldsTest() {
        Item i = ItemMapper.updateItemFields(item, updEmptyItem);
        assertThat(i.getName(), equalTo(item.getName()));
        assertThat(i.getDescription(), equalTo(item.getDescription()));
        assertThat(i.getAvailable(), equalTo(item.getAvailable()));
        assertThat(i.getUser(), equalTo(user));
        assertThat(i.getRequestId(), equalTo(item.getRequestId()));
    }
}
