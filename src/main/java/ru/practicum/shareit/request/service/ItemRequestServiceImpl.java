package ru.practicum.shareit.request.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewRequest;
import ru.practicum.shareit.request.dto.UpdateRequest;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.entity.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ItemRequestServiceImpl implements ItemRequestService {
    RequestRepository repository;
    UserRepository userRepository;

    @Autowired
    public ItemRequestServiceImpl(RequestRepository requestRepository, UserRepository userRepository) {
        this.repository = requestRepository;
        this.userRepository = userRepository;
    }

    private ItemRequest findById(Long itemRequestId) {
        return repository.findById(itemRequestId)
                .orElseThrow(() -> new NotFoundException(String.format("Запрос c ID %d не найден", itemRequestId)));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Владелец вещи c ID %d не найден", userId)));
    }

    @Override
    @Transactional
    public ItemRequestDto create(Long userId, NewRequest request) {
        log.debug("Создаем запись о запросе");

        User findUser = findUserById(userId);

        ItemRequest itemRequest = ItemRequestMapper.mapToItemRequest(request, findUser);
        itemRequest = repository.save(itemRequest);

        return ItemRequestMapper.mapToItemRequestDto(itemRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemRequestDto findItemRequest(Long itemRequestId) {
        return ItemRequestMapper.mapToItemRequestDto(findById(itemRequestId));
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<ItemRequestDto> findAll() {
        log.debug("Получаем записи о всех запросах");
        return repository.findAll()
                .stream()
                .map(ItemRequestMapper::mapToItemRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ItemRequestDto update(Long requestId, Long userId, UpdateRequest request) {
        log.debug("Обновляем данные запроса");

        User findUser = findUserById(userId);

        if (requestId == null) {
            throw new ValidationException("ID запроса должен быть указан");
        }

        ItemRequest updatedItem = ItemRequestMapper.updateItemFields(findById(requestId), request, findUser);
        updatedItem = repository.save(updatedItem);

        return ItemRequestMapper.mapToItemRequestDto(updatedItem);
    }

    @Override
    @Transactional
    public void delete(Long itemRequestId) {
        ItemRequest itemRequest = findById(itemRequestId);
        log.debug("Удаляем данные запроса с ID {}", itemRequest.getId());
        repository.delete(itemRequest);
    }
}
