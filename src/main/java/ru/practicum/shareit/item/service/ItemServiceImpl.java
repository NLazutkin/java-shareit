package ru.practicum.shareit.item.service;

import io.micrometer.common.util.StringUtils;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotItemOwnerException;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ItemServiceImpl implements ItemService {
    ItemStorage itemStorage;
    UserStorage userStorage;

    @Autowired
    public ItemServiceImpl(@Qualifier(/*"ItemDbStorage"*/"InMemoryItemStorage") ItemStorage itemStorage,
                           @Qualifier(/*"UserDbStorage"*/"InMemoryUserStorage") UserStorage userStorage) {
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
    }

    @Override
    public ItemDto create(Long ownerId, NewItemRequest request) {
        log.debug("Создаем запись о вещи");

        User user = userStorage.findUser(ownerId);

        Item item = ItemMapper.mapToItem(ownerId, request);
        item = itemStorage.create(item);

        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public ItemDto findItem(Long ownerId, Long itemId) {
        return ItemMapper.mapToItemDto(itemStorage.findItem(itemId));
    }

    @Override
    public Collection<ItemDto> findItemsForTenant(Long ownerId, String text) {
        log.debug("Получаем записи о всех вещах которые ищет арендатор по ключевым символа {}", text);
        if (StringUtils.isBlank(text)) {
            return new ArrayList<>();
        }

        return itemStorage.findItemsForTenant(text.toLowerCase()).stream()
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ItemDto> findAll(Long ownerId) {
        log.debug("Получаем записи о всех вещах");
        return itemStorage.getItems(ownerId).stream().map(ItemMapper::mapToItemDto).collect(Collectors.toList());
    }

    @Override
    public ItemDto update(Long itemId, UpdateItemRequest request, Long ownerId) {
        log.debug("Обновляем данные о вещи");

        Item item = itemStorage.findItem(itemId);

        if (!item.getOwnerId().equals(ownerId)) {
            throw new NotItemOwnerException("Редактировать данные вещи может только её владелец");
        }

        Item updatedItem = itemStorage.update(ItemMapper.updateItemFields(item, request));

        return ItemMapper.mapToItemDto(updatedItem);
    }

    @Override
    public boolean delete(Long ownerId, Long itemId) {
        Item item = itemStorage.findItem(itemId);
        log.debug("Удаляем данные о вещи с ID {}", item.getName());
        return itemStorage.delete(itemId);
    }
}
