package com.example.tfhbackend.service.impl;

import com.example.tfhbackend.dto.FeedbackDTO;
import com.example.tfhbackend.mapper.Mapper;
import com.example.tfhbackend.model.Feedback;
import com.example.tfhbackend.model.exception.EntityNotFoundException;
import com.example.tfhbackend.repository.FeedbackRepository;
import com.example.tfhbackend.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final Mapper<Feedback, FeedbackDTO> mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<FeedbackDTO> get(int page, int perPage) {
        Pageable pageable = PageRequest.of(page - 1, perPage);
        return feedbackRepository.findAll(pageable).map(mapper::map);
    }

    @Override
    @Transactional(readOnly = true)
    public FeedbackDTO getById(Long id) {
        return mapper.map(findFeedbackById(id));
    }

    @Override
    @Transactional
    public FeedbackDTO add(FeedbackDTO dto) {
        Feedback feedback = Feedback.builder()
                .text(dto.getText())
                .rating(dto.getRating())
                .build();
        return mapper.map(feedbackRepository.save(feedback));
    }

    @Override
    @Transactional
    public FeedbackDTO update(FeedbackDTO dto) {
        Feedback feedback = findFeedbackById(dto.getId());
        feedback.setText(dto.getText());
        feedback.setRating(dto.getRating());
        return mapper.map(feedback);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        feedbackRepository.deleteById(id);
    }

    private Feedback findFeedbackById(Long id) {
        return feedbackRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Feedback with id " + id + " not found"));
    }
}
