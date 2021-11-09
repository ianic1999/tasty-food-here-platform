package com.example.tfhbackend.controller;

import com.example.tfhbackend.dto.JwtDTO;
import com.example.tfhbackend.dto.MessageDTO;
import com.example.tfhbackend.dto.request.AuthenticationRequest;
import com.example.tfhbackend.dto.request.UserRequest;
import com.example.tfhbackend.dto.response.Response;
import com.example.tfhbackend.model.exception.AuthenticationException;
import com.example.tfhbackend.model.exception.UserException;
import com.example.tfhbackend.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<Response<MessageDTO>> register(@RequestBody UserRequest request) throws UserException {
        return ResponseEntity.ok(
                new Response<>(authenticationService.register(request))
        );
    }

    @PostMapping("/login")
    public ResponseEntity<Response<JwtDTO>> login(@RequestBody AuthenticationRequest request) throws AuthenticationException {
        return ResponseEntity.ok(
                new Response<>(authenticationService.authenticate(request))
        );
    }
}
