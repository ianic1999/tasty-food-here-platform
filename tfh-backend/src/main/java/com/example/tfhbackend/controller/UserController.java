package com.example.tfhbackend.controller;

import com.example.tfhbackend.dto.MessageDTO;
import com.example.tfhbackend.dto.response.Response;
import com.example.tfhbackend.model.exception.UserException;
import com.example.tfhbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserController {
    private final UserService userService;

    @PostMapping("/activate/{id}")
    public ResponseEntity<Response<MessageDTO>> activate(@PathVariable Long id) throws UserException {
        return ResponseEntity.ok(
                new Response<>(userService.activateUser(id))
        );
    }
}
