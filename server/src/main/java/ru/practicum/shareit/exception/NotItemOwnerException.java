package ru.practicum.shareit.exception;

public class NotItemOwnerException extends RuntimeException {
    public NotItemOwnerException(String message) {
        super(message);
    }
}