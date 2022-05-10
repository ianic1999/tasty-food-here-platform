package com.example.tfhbackend.job;

import com.example.tfhbackend.dto.FirebaseRequest;
import com.example.tfhbackend.model.Booking;
import com.example.tfhbackend.repository.BookingRepository;
import com.example.tfhbackend.service.FirebaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TableCloseReminderJob {

    private final BookingRepository bookingRepository;
    private final FirebaseService firebaseService;

    @Scheduled(cron = "0 * * * * *")
    @Transactional(readOnly = true)
    public void run() {

        bookingRepository.findAllActive()
                .stream()
                .filter(this::isBookingEnding)
                .forEach(this::remind);
    }

    private boolean isBookingEnding(Booking booking) {
        LocalDateTime now = LocalDateTime.now().plusMinutes(10);
        return now.getHour() == booking.getTime().plusMinutes(booking.getDuration()).getHour()
                && now.getMinute() == booking.getTime().plusMinutes(booking.getDuration()).getMinute();
    }

    private void remind(Booking booking) {
        FirebaseRequest request = new FirebaseRequest(
                "Booking finishing",
                "Table " + booking.getTable().getOrdinalNumber() + " should be closed in 10 minutes",
                booking.getWaiter().getDeviceId()
        );
        firebaseService.sendNotification(request);
    }
}
