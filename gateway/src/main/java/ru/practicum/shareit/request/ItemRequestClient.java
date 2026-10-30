package ru.practicum.shareit.request;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@Component
public class ItemRequestClient extends BaseClient {

    private static final String API_PREFIX = "/requests";

    public ItemRequestClient(RestTemplateBuilder builder) {
        super(
                builder
                        .rootUri("http://localhost:9090")
                        .build()
        );
    }

    public ResponseEntity<Object> create(Long userId,
                                         ItemRequestDto dto) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Sharer-User-Id", String.valueOf(userId));

        return post(API_PREFIX, dto, headers);
    }

    public ResponseEntity<Object> getOwnRequests(Long userId) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Sharer-User-Id", String.valueOf(userId));

        return get(API_PREFIX, headers);
    }

    public ResponseEntity<Object> getAllRequests(Long userId) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Sharer-User-Id", String.valueOf(userId));

        return get(API_PREFIX + "/all", headers);
    }

    public ResponseEntity<Object> getById(Long userId,
                                          Long requestId) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Sharer-User-Id", String.valueOf(userId));

        return get(API_PREFIX + "/" + requestId, headers);
    }
}