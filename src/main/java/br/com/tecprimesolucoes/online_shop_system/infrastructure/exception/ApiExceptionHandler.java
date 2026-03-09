package br.com.tecprimesolucoes.online_shop_system.infrastructure.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ApiExceptionHandler {

        @ExceptionHandler(DomainException.class)
        public ResponseEntity<ApiError> handleDomainException(
                DomainException ex, HttpServletRequest request) {

            ApiError error = new ApiError(
                    LocalDateTime.now(),
                    ex.getStatus().value(),
                    ex.getStatus().getReasonPhrase(),
                    ex.getCode(),
                    ex.getClientMessage(),
                    request.getRequestURI()
            );

            return ResponseEntity.status(ex.getStatus()).body(error);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException e, HttpServletRequest request) {

            String msg = e.getBindingResult().getFieldErrors().stream()
                    .map(field -> field.getField() + ": " + field.getDefaultMessage())
                    .findFirst()
                    .orElse("Erro de validação.");

            ApiError error = new ApiError(
                    LocalDateTime.now(),
                    HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    "VALID-001",
                    msg,
                    request.getRequestURI()
            );

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
}
