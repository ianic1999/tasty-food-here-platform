package com.example.tfhbackend.controller;

import com.example.tfhbackend.dto.MessageDTO;
import com.example.tfhbackend.dto.UserDTO;
import com.example.tfhbackend.dto.request.UserRequest;
import com.example.tfhbackend.dto.response.PaginatedResponse;
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

    @GetMapping
    public ResponseEntity<PaginatedResponse<UserDTO>> get(@RequestParam(defaultValue = "1") int page,
                                                          @RequestParam(defaultValue = "10") int perPage) {
        return ResponseEntity.ok(
                new PaginatedResponse<>(userService.get(page, perPage))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<UserDTO>> getById(@PathVariable Long id) throws UserException {
        return ResponseEntity.ok(
                new Response<>(userService.getById(id))
        );
    }

    @GetMapping("/me")
    public ResponseEntity<Response<UserDTO>> getCurrentUser() throws UserException {
        return ResponseEntity.ok(
                new Response<>(userService.getCurrentLoggedUser())
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<UserDTO>> update(@PathVariable Long id,
                                                    @RequestBody UserRequest request) throws UserException {
        request.setId(id);
        return ResponseEntity.ok(
                new Response<>(userService.update(request))
        );
    }

    @PostMapping("/activate/{id}")
    public ResponseEntity<Response<MessageDTO>> activate(@PathVariable Long id) throws UserException {
        return ResponseEntity.ok(
                new Response<>(userService.activateUser(id))
        );
    }
}
