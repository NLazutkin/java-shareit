package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.NewCommentRequest;
import ru.practicum.shareit.item.dto.AdvancedItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;

import java.util.Collection;

public interface ItemService {
    ItemDto create(Long ownerId, NewItemRequest request);

    AdvancedItemDto findItem(Long ownerId, Long itemId);

    Collection<ItemDto> findItemsForTenant(Long ownerId, String text);

    Collection<AdvancedItemDto> findAll(Long ownerId);

    ItemDto update(Long itemId, UpdateItemRequest request, Long ownerId);

    void delete(Long ownerId, Long itemId);

    CommentDto addComment(Long itemId, Long userId, NewCommentRequest comment);
}
