package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final String path = "/{id}";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") Long userId,
                          @Valid @RequestBody NewItemRequest item) {
        return itemService.create(userId, item);
    }

    @GetMapping(path)
    public ItemDto findItem(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                            @PathVariable("id") Long itemId) {
        return itemService.findItem(ownerId, itemId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> findItemsForTenant(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                  @RequestParam(name = "text", defaultValue = "") String text) {
        return itemService.findItemsForTenant(ownerId, text);
    }

    @GetMapping
    public Collection<ItemDto> findAll(@RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return itemService.findAll(ownerId);
    }

    @PatchMapping(path)
    public ItemDto update(@PathVariable("id") Long itemId,
                          @Valid @RequestBody UpdateItemRequest newItem,
                          @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return itemService.update(itemId, newItem, ownerId);
    }

    @DeleteMapping(path)
    public boolean delete(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                          @PathVariable("id") Long itemId) {
        return itemService.delete(ownerId, itemId);
    }
}
