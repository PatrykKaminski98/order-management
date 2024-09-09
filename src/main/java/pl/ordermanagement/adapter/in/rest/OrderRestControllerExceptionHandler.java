package pl.ordermanagement.adapter.in.rest;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.ordermanagement.adapter.in.rest.controller.OrderRestController;
import pl.ordermanagement.application.order.api.exception.InvalidOrderStatusException;
import pl.ordermanagement.application.order.api.exception.OrderNotFoundException;
import pl.ordermanagement.application.product.api.exception.ProductNotFoundException;

@ControllerAdvice(basePackageClasses = OrderRestController.class)
public class OrderRestControllerExceptionHandler {

    private static final ZoneId UTC = ZoneId.of("UTC");
    private static final String TIMESTAMP_UTC = "timestamp UTC:";

    @ExceptionHandler({OrderNotFoundException.class, ProductNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(NoSuchElementException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Resource Not Found");
        problemDetail.setProperty(TIMESTAMP_UTC, ZonedDateTime.now(UTC));
        return ResponseEntity.of(problemDetail).build();
    }

    @ExceptionHandler(InvalidOrderStatusException.class)
    public ResponseEntity<ErrorResponse> handleInvalidOrderStatusException(InvalidOrderStatusException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle("Resource Conflict");
        problemDetail.setProperty(TIMESTAMP_UTC, ZonedDateTime.now(UTC));
        return ResponseEntity.of(problemDetail).build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle("Invalid Parameters");
        problemDetail.setProperty(TIMESTAMP_UTC, ZonedDateTime.now(UTC));
        return ResponseEntity.of(problemDetail).build();
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleException(RuntimeException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        problemDetail.setTitle("Unexpected error occurred");
        problemDetail.setProperty(TIMESTAMP_UTC, ZonedDateTime.now(UTC));
        return ResponseEntity.of(problemDetail).build();
    }
}
