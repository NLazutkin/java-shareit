package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.entity.Item;

import java.util.Collection;

public interface ItemStorage {
    Item create(Item request);

    Item findItem(Long itemId);

    Collection<Item> findItemsForTenant(String text);

    Collection<Item> getItems(Long ownerId);

    Item update(Item request);

    boolean delete(Long itemId);
}
