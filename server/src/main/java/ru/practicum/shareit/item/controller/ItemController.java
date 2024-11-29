package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.NewCommentRequest;
import ru.practicum.shareit.item.dto.AdvancedItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final String id = "/{item-id}";
    private final String search = "/search";
    private final String comment = "/comment";
    private final String itemComment = id + comment;

    private final String headerUserId = "X-Sharer-User-Id";
    private final String pvItemId = "item-id";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto create(@RequestHeader(headerUserId) Long ownerId,
                          @RequestBody NewItemRequest item) {
        return itemService.create(ownerId, item);
    }

    @GetMapping(id)
    public AdvancedItemDto findItem(@RequestHeader(headerUserId) Long ownerId,
                                    @PathVariable(pvItemId) Long itemId) {
        return itemService.findItem(ownerId, itemId);
    }

    @GetMapping(search)
    public Collection<ItemDto> findItemsForTenant(@RequestHeader(headerUserId) Long ownerId,
                                                  @RequestParam(name = "text", defaultValue = "") String text) {
        return itemService.findItemsForTenant(ownerId, text);
    }

    @GetMapping
    public Collection<AdvancedItemDto> findAll(@RequestHeader(headerUserId) Long ownerId) {
        return itemService.findAll(ownerId);
    }

    @PatchMapping(id)
    public ItemDto update(@PathVariable(pvItemId) Long itemId,
                          @RequestBody UpdateItemRequest newItem,
                          @RequestHeader(headerUserId) Long ownerId) {
        return itemService.update(itemId, newItem, ownerId);
    }

    @DeleteMapping(id)
    public void delete(@RequestHeader(headerUserId) Long ownerId,
                       @PathVariable(pvItemId) Long itemId) {
        itemService.delete(ownerId, itemId);
    }

    @PostMapping(itemComment)
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addComment(@PathVariable(pvItemId) Long itemId,
                                 @RequestHeader(headerUserId) Long userId,
                                 @RequestBody NewCommentRequest comment) {
        log.info("Add comment to item with Id={}, of user with Id={} (server)", itemId, userId);
        return itemService.addComment(itemId, userId, comment);
    }
}
