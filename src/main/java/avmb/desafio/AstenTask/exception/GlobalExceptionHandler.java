package avmb.desafio.AstenTask.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
    }
    @ExceptionHandler(InvalidInsertException.class)
    public ResponseEntity<String> handleCustomValidationException(InvalidInsertException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ex.getMessage());
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleInvalidEnumException(HttpMessageNotReadableException ex) {
        String message = ex.getMostSpecificCause().getMessage();
        if (message != null && message.contains("Cannot deserialize value of type")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid enum value in request body: " + message);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request body, status and priority can not be empty");
    }
}
