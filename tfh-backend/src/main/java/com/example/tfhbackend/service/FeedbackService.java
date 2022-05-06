package com.example.tfhbackend.service;

import com.example.tfhbackend.dto.FeedbackDTO;
import org.springframework.data.domain.Page;

public interface FeedbackService {
    Page<FeedbackDTO> get(int page, int perPage);
    FeedbackDTO getById(Long id);
    FeedbackDTO add(FeedbackDTO feedback);
    void remove(Long id);
}
