package com.example.tfhbackend.controller;

import com.example.tfhbackend.dto.MessageDTO;
import com.example.tfhbackend.dto.UserDTO;
import com.example.tfhbackend.dto.request.UserRequest;
import com.example.tfhbackend.dto.response.PaginatedResponse;
import com.example.tfhbackend.dto.response.Response;
import com.example.tfhbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<PaginatedResponse<UserDTO>> get(@RequestParam(defaultValue = "1") int page,
                                                          @RequestParam(defaultValue = "10") int perPage) {

        log.info("/api/users: GET request with parameters: page: {}, perPage: {}", page, perPage);
        var response = userService.get(page, perPage);
        log.info("/api/users: Response status: {}", HttpStatus.OK);
        return ResponseEntity.ok(
                new PaginatedResponse<>(response)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<UserDTO>> getById(@PathVariable Long id) {
        log.info("/api/users/{id}: GET request with parameters: id: {}", id);
        var response = userService.getById(id);
        log.info("/api/users/{id}: Response Status: {}", HttpStatus.OK);
        return ResponseEntity.ok(
                new Response<>(response)
        );
    }

    @GetMapping("/me")
    public ResponseEntity<Response<UserDTO>> getCurrentUser() {
        log.info("/api/users/me: GET request for current authenticated user");
        var result = userService.getCurrentLoggedUser();
        log.info("/api/users/me: Response for user with id {} : {}", result.getId(), HttpStatus.OK);
        return ResponseEntity.ok(
                new Response<>(result)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<UserDTO>> update(@PathVariable Long id,
                                                    @RequestBody UserRequest request) {
        log.info("/api/users/{id}: PUT request for updating user with id {}", id);
        request.setId(id);
        var response = userService.update(request);
        log.info("/api/users/{id}: User with id {} updated, status: {}", id, HttpStatus.OK);
        return ResponseEntity.ok(
                new Response<>(response)
        );
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<Response<MessageDTO>> activate(@PathVariable Long id) {
        log.info("/api/users/{id}/activate: POST request for activating user with id {}", id);
        var response = userService.activateUser(id);
        log.info("/api/users/{id}/activate: User with id {} activated, status: {}", id, HttpStatus.OK);
        return ResponseEntity.ok(
                new Response<>(response)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        log.info("/api/users/{id}: DELETE request for user with id {}", id);
        userService.remove(id);
        log.info("/api/users/{id}: User with id {} deleted, status: {}", id, HttpStatus.NO_CONTENT);
        return ResponseEntity.noContent().build();
    }
}
