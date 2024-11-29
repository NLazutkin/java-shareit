package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.entity.Item;

import java.util.Collection;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByUserId(Long ownerId);

    @Query("select it " +
            "from Item as it " +
            "join it.user as u " +
            "where it.available = true " +
            "and (lower(it.name) like lower(concat('%', ?1, '%')) " +
            "or lower(it.description) like lower(concat('%', ?1, '%'))) ")
    Collection<Item> findItemsForTenant(String text);

    Collection<Item> findByRequestId(Long requestId);

    Collection<Item> findByRequestIdIn(List<Long> requestIds);
}
