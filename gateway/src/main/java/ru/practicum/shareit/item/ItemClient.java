package ru.practicum.shareit.item;

import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.NewCommentRequest;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;

import java.util.HashMap;
import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build()
        );
    }

    public ResponseEntity<Object> addItem(Long userId, NewItemRequest requestDto) {
        return post("", userId, requestDto);
    }

    public ResponseEntity<Object> getItem(Long userId, Long itemId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> getItems(@Nullable String pathPart, Long userId, @Nullable String text) {
        Map<String, Object> parameters = new HashMap<>();

        String path = "";
        if (pathPart != null) {
            path = path + pathPart;
        }

        if (text != null) {
            parameters.put("text", text);
            path = path + "?text={text}";
        }

        return get(path, userId, parameters);
    }

    public ResponseEntity<Object> updateItem(Long itemId, Long userId, UpdateItemRequest requestDto) {
        return patch("/" + itemId, userId, null, requestDto);
    }

    public ResponseEntity<Object> deleteItem(Long itemId, Long userId) {
        return delete("/" + itemId, userId);
    }

    public ResponseEntity<Object> addComment(String pathPart, Long userId, NewCommentRequest requestDto) {
        return post(pathPart, userId, requestDto);
    }
}
