package ru.practicum.shareit.enums;

import java.util.Optional;

public enum States {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    public static Optional<States> from(String stringState) {
        for (States state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}