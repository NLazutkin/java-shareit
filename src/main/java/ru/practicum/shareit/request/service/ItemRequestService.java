package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewRequest;
import ru.practicum.shareit.request.dto.UpdateRequest;

import java.util.Collection;

public interface ItemRequestService {
    ItemRequestDto create(NewRequest request);

    ItemRequestDto findItemRequest(Long itemRequestId);

    Collection<ItemRequestDto> findAll();

    ItemRequestDto update(UpdateRequest request);

    boolean delete(Long itemRequestId);
}
