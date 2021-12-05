package com.example.tfhbackend.controller;

import com.example.tfhbackend.dto.FeedbackDTO;
import com.example.tfhbackend.dto.response.PaginatedResponse;
import com.example.tfhbackend.dto.response.Response;
import com.example.tfhbackend.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/feedbacks")
@CrossOrigin("*")
@Slf4j
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;

    @GetMapping
    public ResponseEntity<PaginatedResponse<FeedbackDTO>> get(@RequestParam(defaultValue = "1") int page,
                                                              @RequestParam(defaultValue = "10") int perPage) {
        log.info("/api/feedbacks: GET request with parameters: page: {}, perPage: {} ", page, perPage);
        var response = feedbackService.get(page, perPage);
        log.info("/api/feedbacks: Response status: {}", HttpStatus.OK);
        return ResponseEntity.ok(
                new PaginatedResponse<>(response)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<FeedbackDTO>> getById(@PathVariable Long id) {
        log.info("/api/feedbacks/{id}: GET request with parameters: id: {}", id);
        FeedbackDTO response = feedbackService.getById(id);
        log.info("/api/feedbacks/{id}: Response Status: {}", HttpStatus.OK);
        return ResponseEntity.ok(
                new Response<>(response)
        );
    }

    @PostMapping
    public ResponseEntity<Response<FeedbackDTO>> add(@RequestBody FeedbackDTO feedback) {
        log.info("/api/feedbacks: POST request with parameters: rating: {}, text: {}", feedback.getRating(), feedback.getText());
        var response = feedbackService.add(feedback);
        log.info("/api/feedbacks: Feedback added, id={}, Response status: {}", response.getId(), HttpStatus.CREATED);
        return new ResponseEntity<>(
                new Response<>(response),
                HttpStatus.CREATED
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        log.info("/api/feedbacks/{id}: DELETE request for removing feedback with id: {}", id);
        feedbackService.remove(id);
        log.info("/api/feedbacks/{id}: Item with id {} removed, Response status={}", id, HttpStatus.NO_CONTENT);
        return ResponseEntity.noContent().build();
    }
}
