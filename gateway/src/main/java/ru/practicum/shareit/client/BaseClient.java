package ru.practicum.shareit.client;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class BaseClient {

    protected final RestTemplate rest;

    protected BaseClient(RestTemplate rest) {
        this.rest = rest;
    }

    protected ResponseEntity<Object> get(String path,
                                         HttpHeaders headers) {

        return rest.exchange(
                path,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Object.class
        );
    }

    protected ResponseEntity<Object> post(String path,
                                          Object body,
                                          HttpHeaders headers) {

        return rest.exchange(
                path,
                HttpMethod.POST,
                new HttpEntity<>(body, headers),
                Object.class
        );
    }

    protected ResponseEntity<Object> patch(String path,
                                           Object body,
                                           HttpHeaders headers) {

        return rest.exchange(
                path,
                HttpMethod.PATCH,
                new HttpEntity<>(body, headers),
                Object.class
        );
    }

    protected ResponseEntity<Object> delete(String path,
                                            HttpHeaders headers) {

        return rest.exchange(
                path,
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                Object.class
        );
    }
}