package com.example.tfhbackend.controller;

import com.example.tfhbackend.service.SmsService;
import com.example.tfhbackend.service.WaiterCallService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/arduino")
@CrossOrigin("*")
@RequiredArgsConstructor
public class ArduinoController {
    private final WaiterCallService waiterCallService;
    private final SmsService smsService;

    @PostMapping("/call/{tableNumber}")
    public ResponseEntity<Void> call(@PathVariable Integer tableNumber) {
        waiterCallService.call(tableNumber);
        return ResponseEntity.ok().build();
    }
}
