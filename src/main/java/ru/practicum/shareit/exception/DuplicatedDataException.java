package ru.practicum.shareit.exception;

public class DuplicatedDataException extends RuntimeException {
    public DuplicatedDataException(String message) {
        super(message);
    }
}
