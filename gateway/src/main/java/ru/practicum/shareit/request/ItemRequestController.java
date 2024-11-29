package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.NewRequest;
import ru.practicum.shareit.request.dto.UpdateRequest;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;
    private final String id = "/{request-id}";
    private final String all = "/all";

    private final String headerUserId = "X-Sharer-User-Id";
    private final String pvRequestId = "request-id";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> addItemRequest(@RequestHeader(headerUserId) Long userId,
                                                 @Valid @RequestBody NewRequest itemRequest) {
        return itemRequestClient.addItemRequest(userId, itemRequest);
    }

    @GetMapping(id)
    public ResponseEntity<Object> findItemRequest(@PathVariable(pvRequestId) Long requestId) {
        return itemRequestClient.getItemRequest(requestId);
    }

    @GetMapping
    public ResponseEntity<Object> findAllByRequestorId(@RequestHeader(headerUserId) Long requestorId) {
        return itemRequestClient.getItemRequests(null, requestorId);
    }

    @GetMapping(all)
    public ResponseEntity<Object> findAllOfAnotherRequestors(@RequestHeader(headerUserId) Long requestorId) {
        return itemRequestClient.getItemRequests(all, requestorId);
    }

    @PutMapping
    public ResponseEntity<Object> updateItemRequest(@RequestHeader(headerUserId) Long userId,
                                                    @Valid @RequestBody UpdateRequest newItemRequest) {
        return itemRequestClient.updateItemRequest(userId, newItemRequest);
    }

    @DeleteMapping(id)
    public ResponseEntity<Object> deleteItemRequest(@PathVariable(pvRequestId) Long requestId) {
        return itemRequestClient.deleteItemRequest(requestId);
    }
}
