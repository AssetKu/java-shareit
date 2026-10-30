package ru.practicum.shareit.booking;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.client.BaseClient;

@Component
public class BookingClient extends BaseClient {

    private static final String API_PREFIX = "/bookings";

    public BookingClient(RestTemplateBuilder builder) {
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

    public ResponseEntity<Object> create(
            Long userId,
            BookingCreateDto dto) {

        return post(
                API_PREFIX,
                dto,
                headers(userId)
        );
    }

    public ResponseEntity<Object> approve(
            Long userId,
            Long bookingId,
            Boolean approved) {

        return patch(
                API_PREFIX + "/" + bookingId + "?approved=" + approved,
                null,
                headers(userId)
        );
    }

    public ResponseEntity<Object> getById(
            Long userId,
            Long bookingId) {

        return get(
                API_PREFIX + "/" + bookingId,
                headers(userId)
        );
    }

    public ResponseEntity<Object> getBookings(
            Long userId,
            String state) {

        return get(
                API_PREFIX + "?state=" + state,
                headers(userId)
        );
    }

    public ResponseEntity<Object> getOwnerBookings(
            Long userId,
            String state) {

        return get(
                API_PREFIX + "/owner?state=" + state,
                headers(userId)
        );
    }
}