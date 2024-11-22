package ru.practicum.shareit.request.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewRequest;
import ru.practicum.shareit.request.dto.UpdateRequest;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.entity.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ItemRequestServiceImpl implements ItemRequestService {
    RequestRepository repository;
    UserRepository userRepository;
    ItemRepository itemRepository;

    @Autowired
    public ItemRequestServiceImpl(RequestRepository requestRepository,
                                  UserRepository userRepository,
                                  ItemRepository itemRepository) {
        this.repository = requestRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    private ItemRequest findById(Long itemRequestId) {
        return repository.findById(itemRequestId)
                .orElseThrow(() -> new NotFoundException(String.format("Запрос c ID %d не найден", itemRequestId)));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Владелец вещи c ID %d не найден", userId)));
    }

    private List<ItemRequestDto> fillRequestsData(List<ItemRequest> requests) {

        List<Long> requestIds = requests.stream()
                .map(ItemRequest::getId)
                .toList();

        Map<Long, List<Item>> requestItems = itemRepository
                .findByRequestIdInAndAvailableTrue(requestIds)
                .stream()
                .collect(groupingBy(Item::getRequestId, toList()));

        List<ItemRequestDto> requestsList = new ArrayList<>();
        for (ItemRequest request : requests) {

            requestsList.add(ItemRequestMapper.mapToItemRequestDto(request,
                    requestItems.getOrDefault(request.getId(), Collections.emptyList()))
            );
        }

        return requestsList;
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
        log.debug("Ищем запись о запросе с ID {}", itemRequestId);
        ItemRequest itemRequest = findById(itemRequestId);

        Collection<Item> items = itemRepository.findByRequestIdAndAvailableTrue(itemRequestId);

        return ItemRequestMapper.mapToItemRequestDto(itemRequest, items);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<ItemRequestDto> findAllByRequestorId(Long requestorId) {
        log.debug("Получаем записи о всех запросах пользователя с ID {}", requestorId);

        User findUser = findUserById(requestorId);

        List<ItemRequest> requests = repository.findByRequestorId(requestorId);

        return fillRequestsData(requests)
                .stream()
                .sorted(Comparator.comparing(ItemRequestDto::getCreated).reversed())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<ItemRequestDto> findAllOfAnotherRequestors(Long requestorId) {
        log.debug("Получаем записи о всех запросах для пользователя с ID {}", requestorId);
        return repository.findByRequestorIdNotOrderByCreatedDesc(requestorId)
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
