package ru.practicum.shareit.request.storage;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.entity.ItemRequest;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component("InMemoryItemRequestStorage")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class InMemoryItemRequestStorage implements ItemRequestStorage {
    Map<Long, ItemRequest> itemRequests = new HashMap<>();

    private long getNextId() {
        long currentMaxId = itemRequests.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @Override
    public ItemRequest create(ItemRequest itemRequest) {
        itemRequest.setId(getNextId());
        log.trace("Данные о запросе с ID {} сохранены!", itemRequest.getId());
        itemRequests.put(itemRequest.getId(), itemRequest);
        return itemRequest;
    }

    @Override
    public ItemRequest findItemRequest(Long itemRequestId) {
        return Optional.ofNullable(itemRequests.get(itemRequestId))
                .orElseThrow(() -> new NotFoundException(String.format("Запрос c ID %d не найден", itemRequestId)));
    }

    @Override
    public Collection<ItemRequest> getItemRequests() {
        return itemRequests.values();
    }

    @Override
    public ItemRequest update(ItemRequest newItemRequest) {
        log.trace("Запрос на вещь с ID {} обновлен!", newItemRequest.getId());
        itemRequests.put(newItemRequest.getId(), newItemRequest);
        return newItemRequest;
    }

    @Override
    public boolean delete(Long itemRequestId) {
        itemRequests.remove(itemRequestId);
        return Optional.ofNullable(itemRequests.get(itemRequestId)).isPresent();
    }
}
