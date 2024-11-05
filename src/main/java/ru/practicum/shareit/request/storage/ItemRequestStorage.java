package ru.practicum.shareit.request.storage;

import ru.practicum.shareit.request.entity.ItemRequest;

import java.util.Collection;

public interface ItemRequestStorage {
    ItemRequest create(ItemRequest request);

    ItemRequest findItemRequest(Long itemRequestId);

    Collection<ItemRequest> getItemRequests();

    ItemRequest update(ItemRequest request);

    boolean delete(Long itemRequestId);
}
