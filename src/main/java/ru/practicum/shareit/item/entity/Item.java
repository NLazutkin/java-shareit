package ru.practicum.shareit.item.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.request.entity.ItemRequest;
import ru.practicum.shareit.user.entity.User;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"id"})
@Entity
@Table(name = "items")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "description", nullable = false)
    String description;

    @Column(name = "available", nullable = false)
    Boolean available;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "owner_id")
    User user;

    @JoinColumn(name = "request_id")
    Long requestId;
}
