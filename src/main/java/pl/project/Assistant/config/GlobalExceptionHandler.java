package pl.project.Assistant.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import pl.project.Assistant.exception.BadRequestException;
import pl.project.Assistant.exception.ConflictException;
import pl.project.Assistant.exception.ForbiddenException;
import pl.project.Assistant.exception.ResourceNotFoundException;
import pl.project.Assistant.exception.UnauthorizedException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice

public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage()));
    return  ResponseEntity.badRequest().body(errors);
        }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleNotReadable(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body("Malformed request body");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.badRequest().body("Invalid value for parameter '" + ex.getName() + "'");
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleUnexceptedRuntimeException(RuntimeException ex) {
        log.error("Unexpected error", ex);
        return ResponseEntity.status(500).body("Internal server error");
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex){
        return ResponseEntity.status(404).body(ex.getMessage());
    }
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<String> handleForbidden(ForbiddenException ex){
        return ResponseEntity.status(403).body(ex.getMessage());
    }
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<String> handleConflict(ConflictException ex){
        return ResponseEntity.status(409).body(ex.getMessage());
    }
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handleBadRequest(BadRequestException ex){
        return ResponseEntity.status(400).body(ex.getMessage());
    }
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String> handleUnauthorized(UnauthorizedException ex){
        return ResponseEntity.status(401).body(ex.getMessage());
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolation(DataIntegrityViolationException ex){
        return ResponseEntity.status(409).body("Data integrity violation");
    }
}
