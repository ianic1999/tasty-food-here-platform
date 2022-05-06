package com.example.tfhbackend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "incrementDomain")
    @GenericGenerator(name = "incrementDomain", strategy = "increment")
    private Long id;

    @NotEmpty(message = "Feedback message should be provided")
    @Column(columnDefinition = "TEXT", name = "feedback_text")
    private String text;

    @Min(value = 1, message = "Rating value is in range 1-5")
    @Max(value = 5, message = "Rating value is in range 1-5")
    private Integer rating;

    @NotEmpty(message = "Booking ID should be provided")
    @Getter
    @Setter
    private String bookingId;

    private String fullName;

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Feedback feedback = (Feedback) o;
        return id.equals(feedback.id);
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}
