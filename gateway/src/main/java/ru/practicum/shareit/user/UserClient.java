package ru.practicum.shareit.user;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.UserDto;

@Component
public class UserClient extends BaseClient {

    private static final String API_PREFIX = "/users";

    public UserClient(RestTemplateBuilder builder) {
        super(
                builder
                        .rootUri("http://localhost:9090")
                        .build()
        );
    }

    public ResponseEntity<Object> create(UserDto dto) {
        return post(API_PREFIX, dto, new HttpHeaders());
    }

    public ResponseEntity<Object> getById(Long id) {
        return get(API_PREFIX + "/" + id, new HttpHeaders());
    }

    public ResponseEntity<Object> getAll() {
        return get(API_PREFIX, new HttpHeaders());
    }

    public ResponseEntity<Object> update(Long id, UserDto dto) {
        return patch(API_PREFIX + "/" + id, dto, new HttpHeaders());
    }

    public ResponseEntity<Object> delete(Long id) {
        return delete(API_PREFIX + "/" + id, new HttpHeaders());
    }
}