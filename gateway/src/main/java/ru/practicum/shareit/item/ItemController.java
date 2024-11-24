package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.NewCommentRequest;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;

@Controller
@Validated
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/items")
public class ItemController {
    private final ItemClient itemClient;
    private final String id = "/{item-id}";
    private final String search = "/search";
    private final String comment = "/comment";
    private final String itemComment = id + comment;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> addItem(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                          @Valid @RequestBody NewItemRequest item) {
        return itemClient.addItem(ownerId, item);
    }

    @GetMapping(id)
    public ResponseEntity<Object> findItem(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                           @PathVariable("item-id") Long itemId) {
        return itemClient.getItem(ownerId, itemId);
    }

    @GetMapping(search)
    public ResponseEntity<Object> findItemsForTenant(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                     @RequestParam(name = "text", defaultValue = "") String text) {
        return itemClient.getItems(search, ownerId, text);
    }

    @GetMapping
    public ResponseEntity<Object> findAll(@RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return itemClient.getItems(null, ownerId, null);
    }

    @PatchMapping(id)
    public ResponseEntity<Object> updateItem(@PathVariable("item-id") Long itemId,
                                             @Valid @RequestBody UpdateItemRequest newItem,
                                             @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return itemClient.updateItem(itemId, ownerId, newItem);
    }

    @DeleteMapping(id)
    public ResponseEntity<Object> deleteItem(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                             @PathVariable("item-id") Long itemId) {
        return itemClient.deleteItem(itemId, ownerId);
    }

    @PostMapping(itemComment)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> addComment(@PathVariable("item-id") Long itemId,
                                             @RequestHeader("X-Sharer-User-Id") Long userId,
                                             @Valid @RequestBody NewCommentRequest comment) {
        return itemClient.addComment("/" + itemId + this.comment, userId, comment);
    }
}
