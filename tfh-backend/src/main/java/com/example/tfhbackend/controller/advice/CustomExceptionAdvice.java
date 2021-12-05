package com.example.tfhbackend.controller.advice;

import com.example.tfhbackend.controller.*;
import com.example.tfhbackend.dto.response.ExceptionResponse;
import com.example.tfhbackend.model.exception.AuthenticationException;
import com.example.tfhbackend.model.exception.CustomRuntimeException;
import com.example.tfhbackend.model.exception.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice(basePackageClasses = {
        AuthenticationController.class,
        BookingController.class,
        FeedbackController.class,
        MenuItemController.class,
        OrderController.class,
        TableController.class,
        UserController.class
})
public class CustomExceptionAdvice {
    @ExceptionHandler({
            EntityNotFoundException.class,
            CustomRuntimeException.class,
            AuthenticationException.class
    })
    public ResponseEntity<ExceptionResponse> handleCustomException(Exception exception) {
        Map<String, List<String>> messages = new HashMap<>();
        messages.put("no_field", new ArrayList<>());
        messages.get("no_field").add(exception.getMessage());
        return ResponseEntity
                .badRequest()
                .body(
                        new ExceptionResponse("validations", messages)
                );
    }
}
