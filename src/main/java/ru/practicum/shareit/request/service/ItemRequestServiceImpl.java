package ru.practicum.shareit.request.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewRequest;
import ru.practicum.shareit.request.dto.UpdateRequest;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ItemRequestServiceImpl implements ItemRequestService {
    ItemRequestStorage itemRequestStorage;

    @Autowired
    public ItemRequestServiceImpl(@Qualifier(/*"ItemRequestDbStorage"*/"InMemoryItemRequestStorage") ItemRequestStorage itemRequestStorage) {
        this.itemRequestStorage = itemRequestStorage;
    }

    @Override
    public ItemRequestDto create(NewRequest request) {
        log.debug("Создаем запись о запросе");

        ItemRequest itemRequest = ItemRequestMapper.mapToItemRequest(request);
        itemRequest = itemRequestStorage.create(itemRequest);

        return ItemRequestMapper.mapToItemRequestDto(itemRequest);
    }

    @Override
    public ItemRequestDto findItemRequest(Long itemRequestId) {
        return ItemRequestMapper.mapToItemRequestDto(itemRequestStorage.findItemRequest(itemRequestId));
    }

    @Override
    public Collection<ItemRequestDto> findAll() {
        log.debug("Получаем записи о всех запросах");
        return itemRequestStorage.getItemRequests()
                .stream()
                .map(ItemRequestMapper::mapToItemRequestDto).collect(Collectors.toList());
    }

    @Override
    public ItemRequestDto update(UpdateRequest request) {
        log.debug("Обновляем данные запроса");

        if (request.getId() == null) {
            throw new ValidationException("ID запроса должен быть указан");
        }

        ItemRequest updatedItem = ItemRequestMapper.updateItemFields(itemRequestStorage.findItemRequest(request.getId()), request);
        updatedItem = itemRequestStorage.update(updatedItem);

        return ItemRequestMapper.mapToItemRequestDto(updatedItem);
    }

    @Override
    public boolean delete(Long itemRequestId) {
        ItemRequest itemRequest = itemRequestStorage.findItemRequest(itemRequestId);
        log.debug("Удаляем данные запроса с ID {}", itemRequest.getId());
        return itemRequestStorage.delete(itemRequestId);
    }
}
