package ru.practicum.shareit.exception;

<<<<<<< HEAD
import org.springframework.http.HttpStatus;
=======
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
>>>>>>> 68e9c73 (Fix add item requests and gateway)
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
<<<<<<< HEAD
=======
import org.springframework.web.client.HttpStatusCodeException;
>>>>>>> 68e9c73 (Fix add item requests and gateway)

import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
<<<<<<< HEAD
    public Map<String, String> handleValidation(
            MethodArgumentNotValidException e) {

        return Map.of(
                "error",
                e.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .findFirst()
                        .map(error -> error.getDefaultMessage())
                        .orElse("Validation error")
        );
    }
}
=======
    public Map<String, String> handleValidation(MethodArgumentNotValidException e) {
        return Map.of("error", e.getBindingResult().getFieldErrors().stream()
                .findFirst().map(error -> error.getDefaultMessage()).orElse("Validation error"));
    }

    @ExceptionHandler(HttpStatusCodeException.class)
    public ResponseEntity<String> handleServerError(HttpStatusCodeException e) {
        HttpHeaders headers = new HttpHeaders();
        MediaType contentType = e.getResponseHeaders() == null
                ? MediaType.APPLICATION_JSON
                : e.getResponseHeaders().getContentType();
        headers.setContentType(contentType == null ? MediaType.APPLICATION_JSON : contentType);
        return new ResponseEntity<>(e.getResponseBodyAsString(), headers, e.getStatusCode());
    }
}
>>>>>>> 68e9c73 (Fix add item requests and gateway)
