package com.example.tfhbackend.controller.advice;

import com.example.tfhbackend.controller.*;
import com.example.tfhbackend.dto.response.ExceptionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice(basePackageClasses = {
        BookingController.class,
        FeedbackController.class,
        MenuItemController.class,
        OrderController.class,
        TableController.class,
        UserController.class
})
public class ConstraintViolationAdvice {
    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<ExceptionResponse> handleConstraintViolationException(Exception exception) {
        ConstraintViolationException constraintViolationException = (ConstraintViolationException) ((TransactionSystemException) exception).getRootCause();
        Map<String, List<String>> messages = constraintViolationException.getConstraintViolations()
                .stream()
                .collect(Collectors.groupingBy(constraint -> constraint.getPropertyPath().toString(), Collectors.toList()))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().stream().map(ConstraintViolation::getMessage).collect(Collectors.toList())));
        return ResponseEntity
                .badRequest()
                .body(
                        new ExceptionResponse("validations", messages)
                );
    }
}
