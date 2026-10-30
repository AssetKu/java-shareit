package ru.practicum.shareit.item;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

@Component
public class ItemClient extends BaseClient {

    private static final String API_PREFIX = "/items";

    public ItemClient(RestTemplateBuilder builder) {
        super(
                builder
                        .rootUri("http://localhost:9090")
                        .build()
        );
    }

    private HttpHeaders headers(Long userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Sharer-User-Id", String.valueOf(userId));
        return headers;
    }

    public ResponseEntity<Object> create(Long userId,
                                         ItemDto itemDto) {

        return post(
                API_PREFIX,
                itemDto,
                headers(userId)
        );
    }

    public ResponseEntity<Object> update(Long userId,
                                         Long itemId,
                                         ItemDto itemDto) {

        return patch(
                API_PREFIX + "/" + itemId,
                itemDto,
                headers(userId)
        );
    }

    public ResponseEntity<Object> getById(Long itemId) {

        return get(
                API_PREFIX + "/" + itemId,
                new HttpHeaders()
        );
    }

    public ResponseEntity<Object> getOwnerItems(Long userId) {

        return get(
                API_PREFIX,
                headers(userId)
        );
    }

    public ResponseEntity<Object> search(String text) {

        return get(
                API_PREFIX + "/search?text=" + text,
                new HttpHeaders()
        );
    }

    public ResponseEntity<Object> createComment(Long userId,
                                                Long itemId,
                                                CommentDto commentDto) {

        return post(
                API_PREFIX + "/" + itemId + "/comment",
                commentDto,
                headers(userId)
        );
    }
}