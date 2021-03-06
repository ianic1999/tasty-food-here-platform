package com.example.tfhbackend.mapper;

import com.example.tfhbackend.dto.FeedbackDTO;
import com.example.tfhbackend.model.Feedback;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
class FeedbackDtoMapper implements Mapper<Feedback, FeedbackDTO> {

    @Override
    public FeedbackDTO map(Feedback entity) {
        return FeedbackDTO.builder()
                .text(entity.getText())
                .rating(entity.getRating())
                .bookingId(entity.getBookingId())
                .build();
    }
}
