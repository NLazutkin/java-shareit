package ru.practicum.shareit.item.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.dto.AdvancedItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {
    public static ItemDto mapToItemDto(Item item) {
        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        dto.setOwnerId(item.getUser().getId());
        if (item.getRequest() != null) {
            dto.setRequestId(item.getRequest().getId());
        }

        return dto;
    }

    public static AdvancedItemDto mapToAdvancedItemDto(Item item,
                                                       List<Comment> comments,
                                                       Optional<LocalDateTime> lastBooking,
                                                       Optional<LocalDateTime> nextBooking) {
        AdvancedItemDto dto = new AdvancedItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        dto.setOwnerId(item.getUser().getId());
        lastBooking.ifPresent(dto::setLastBooking);
        nextBooking.ifPresent(dto::setNextBooking);
        dto.setComments(comments.stream().map(CommentMapper::mapToCommentDto).toList());

        return dto;
    }

    public static AdvancedItemDto mapToAdvancedItemDto(Item item, List<Comment> comments) {
        AdvancedItemDto dto = new AdvancedItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        dto.setOwnerId(item.getUser().getId());
        dto.setComments(comments.stream().map(CommentMapper::mapToCommentDto).toList());
        if (item.getRequest() != null) {
            dto.setRequestId(item.getRequest().getId());
        }

        return dto;
    }

    public static Item mapToItem(User owner, NewItemRequest request) {
        Item item = new Item();
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setAvailable(request.getAvailable());
        item.setUser(owner);

        return item;
    }

    public static Item updateItemFields(Item item, UpdateItemRequest request) {
        if (request.hasName()) {
            item.setName(request.getName());
        }

        if (request.hasDescription()) {
            item.setDescription(request.getDescription());
        }

        if (request.hasAvailable()) {
            item.setAvailable(request.getAvailable());
        }

        return item;
    }
}
