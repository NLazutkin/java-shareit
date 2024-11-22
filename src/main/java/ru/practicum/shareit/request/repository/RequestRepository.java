package ru.practicum.shareit.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.entity.ItemRequest;

import java.util.Collection;
import java.util.List;

public interface RequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findByRequestorId(Long requestorId);

    Collection<ItemRequest> findByRequestorIdNotOrderByCreatedDesc(Long requestorId);
}