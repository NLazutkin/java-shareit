package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.NewRequest;
import ru.practicum.shareit.request.dto.UpdateRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Collection;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;
    private final String id = "/{request-id}";
    private final String all = "/all";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDto create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @RequestBody NewRequest itemRequest) {
        return itemRequestService.create(userId, itemRequest);
    }

    @GetMapping(id)
    public ItemRequestDto findItemRequest(@PathVariable("request-id") Long requestId) {
        return itemRequestService.findItemRequest(requestId);
    }

    @GetMapping
    public Collection<ItemRequestDto> findAllByRequestorId(@RequestHeader("X-Sharer-User-Id") Long requestorId) {
        return itemRequestService.findAllByRequestorId(requestorId);
    }

    @GetMapping(all)
    public Collection<ItemRequestDto> findAllOfAnotherRequestors(@RequestHeader("X-Sharer-User-Id") Long requestorId) {
        return itemRequestService.findAllOfAnotherRequestors(requestorId);
    }

    @PutMapping
    public ItemRequestDto update(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @RequestBody UpdateRequest newItemRequest) {
        return itemRequestService.update(userId, newItemRequest);
    }

    @DeleteMapping(id)
    public void delete(@PathVariable("request-id") Long requestId) {
        itemRequestService.delete(requestId);
    }
}
