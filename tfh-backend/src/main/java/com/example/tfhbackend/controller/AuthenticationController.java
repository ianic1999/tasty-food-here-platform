package com.example.tfhbackend.controller;

import com.example.tfhbackend.dto.JwtDTO;
import com.example.tfhbackend.dto.MessageDTO;
import com.example.tfhbackend.dto.request.AuthenticationRequest;
import com.example.tfhbackend.dto.request.UserRequest;
import com.example.tfhbackend.dto.response.Response;
import com.example.tfhbackend.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
@Slf4j
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<Response<MessageDTO>> register(@RequestBody UserRequest request) {
        log.info("/api/register: POST request with data: firstName: {}, lastName: {}, email: {}, phone, {}.",
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPhone());

        var response = authenticationService.register(request);
        log.info("/api/register: Response for registration with phone {} : {}",
                request.getPhone(),
                HttpStatus.OK);

        return ResponseEntity.ok(
                new Response<>(response)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<Response<JwtDTO>> login(@RequestBody AuthenticationRequest request) {
        log.info("/api/login: Request with data: phone: {}, remember: {}.",
                request.getPhone(),
                request.isRemember());

        var response = authenticationService.authenticate(request);

        log.info("/api/login: Response for login with phone {}: {}",
                request.getPhone(),
                HttpStatus.OK);

        return ResponseEntity.ok(
                new Response<>(response)
        );
    }
}
