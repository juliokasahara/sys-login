package com.syslogin.exeption;

import com.syslogin.presenter.ResponseHandler;
import com.syslogin.utils.AppMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;

@ControllerAdvice
@RequiredArgsConstructor
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    private final AppMessage appMessage;

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        return ResponseHandler.generateResponse(appMessage.getMessage("internal.server.error"), HttpStatus.INTERNAL_SERVER_ERROR, details);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public final ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex, WebRequest request){
        return ResponseHandler.generateResponse(appMessage.getMessage("access.denied"), HttpStatus.FORBIDDEN, null);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, String> details = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            details.put(fieldError.getField(), fieldError.getDefaultMessage());
        });

        Map<String, Map<String, String>> fieldErrors = new HashMap<>();
        fieldErrors.put("fieldErrors", details);
        return ResponseHandler.generateResponse(appMessage.getMessage("form.error.validation"), HttpStatus.BAD_REQUEST, fieldErrors);
    }
}
