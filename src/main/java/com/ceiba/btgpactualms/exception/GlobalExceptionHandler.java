package com.ceiba.btgpactualms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // 🔥 Errores de negocio
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBusiness(BusinessException ex) {
        return new ErrorResponse(ex.getMessage(), 400);
    }

    // 🔥 Errores generales
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGeneral(Exception ex) {
        return new ErrorResponse("Error interno del servidor", 500);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ErrorResponse handleAccessDenied(AccessDeniedException ex) {
        return new ErrorResponse("Acceso denegado", 403);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntime(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "mensaje", ex.getMessage(),
                        "codigo", 400
                ));
    }
}
