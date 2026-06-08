package com.leonardo.barbershop.appointment.exception;

import com.leonardo.barbershop.appointment.dto.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ErrorResponse buildError(HttpStatus status, Exception ex){
        return new ErrorResponse(status.value(), status.getReasonPhrase(), ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(EmailAlreadyRegisteredException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyRegistered(EmailAlreadyRegisteredException ex){
        ErrorResponse error = buildError(HttpStatus.CONFLICT, ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException ex){
        ErrorResponse error = buildError(HttpStatus.NOT_FOUND, ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ServiceAlreadyDeactivatedException.class)
    public ResponseEntity<ErrorResponse> handleServiceAlreadyDeactivated(ServiceAlreadyDeactivatedException ex){
        ErrorResponse error = buildError(HttpStatus.CONFLICT, ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
}
