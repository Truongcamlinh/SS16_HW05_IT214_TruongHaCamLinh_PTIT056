package com.tiki.flashsale.exception;

import java.time.Instant;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(ProductUnavailableException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    Map<String,Object> unavailable(ProductUnavailableException e) {
        return body(503, e.getMessage());
    }
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Map<String,Object> badRequest(IllegalArgumentException e) { return body(400, e.getMessage()); }
    private Map<String,Object> body(int status, String message) {
        return Map.of("timestamp", Instant.now().toString(), "status", status, "message", message);
    }
}
