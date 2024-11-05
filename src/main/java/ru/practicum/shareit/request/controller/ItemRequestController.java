package ru.practicum.shareit.request.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.NewRequest;
import ru.practicum.shareit.request.dto.UpdateRequest;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Collection;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestServiceImpl itemRequestService;
    private final String path = "/{id}";

    @GetMapping(path)
    public ItemRequestDto findItemRequest(@PathVariable("id") Long requestId) {
        return itemRequestService.findItemRequest(requestId);
    }

    @GetMapping
    public Collection<ItemRequestDto> findAll() {
        return itemRequestService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDto create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @Valid @RequestBody NewRequest itemRequest) {
        return itemRequestService.create(userId, itemRequest);
    }

    @PutMapping
    public ItemRequestDto update(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @Valid @RequestBody UpdateRequest newItemRequest) {
        return itemRequestService.update(userId, newItemRequest);
    }

    @DeleteMapping(path)
    public void delete(@PathVariable("id") Long requestId) {
        itemRequestService.delete(requestId);
    }
}
