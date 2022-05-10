package com.example.tfhbackend.job;

import com.example.tfhbackend.dto.FirebaseRequest;
import com.example.tfhbackend.model.Booking;
import com.example.tfhbackend.model.User;
import com.example.tfhbackend.model.enums.BookingStatus;
import com.example.tfhbackend.repository.BookingRepository;
import com.example.tfhbackend.repository.UserRepository;
import com.example.tfhbackend.service.FirebaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class WaiterAssignJob {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final FirebaseService firebaseService;

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void run() {
        LocalDateTime now = LocalDateTime.now();
        bookingRepository.findAllUpcoming()
                .stream()
                .filter(booking -> now.plusMinutes(5).isAfter(booking.getTime()))
                .forEach(this::assignWaiterToBooking);
    }

    private void assignWaiterToBooking(Booking booking) {
        User waiter = getFreeWaiterForRange(booking.getTime(), booking.getDuration());
        booking.setWaiter(waiter);
        if (Objects.isNull(booking.getTable().getWaiter())) {
            booking.getTable().setWaiter(waiter);
        }
        booking.setStatus(BookingStatus.ACTIVE);
        sendNotificationToWaiter(booking.getTable().getOrdinalNumber(), waiter.getDeviceId());
    }

    private User getFreeWaiterForRange(LocalDateTime start, int duration) {
        LocalDateTime end = start.plusMinutes(duration);
        List<User> freeWaiters = userRepository.getFreeWaitersForTimeRange(start, end);
        if (!freeWaiters.isEmpty())
            return freeWaiters.get(0);
        else
            return getWaiterWithLeastTablesForRange(start, end);
    }

    private User getWaiterWithLeastTablesForRange(LocalDateTime start, LocalDateTime end) {
        return userRepository.getAllWaitersForTimeRage(start, end)
                                .stream()
                                .collect(Collectors.groupingBy(Function.identity()))
                                .entrySet()
                                .stream()
                                .map(entry -> Map.entry(entry.getKey(), entry.getValue().size()))
                                .sorted(Map.Entry.comparingByValue())
                                .map(Map.Entry::getKey)
                                .findFirst()
                                .orElseThrow();
    }

    private void sendNotificationToWaiter(int tableId, String deviceId) {
        FirebaseRequest request = new FirebaseRequest(
                "New table",
                "Table " + tableId + " was assigned to you",
                deviceId
        );
        firebaseService.sendNotification(request);
    }
}
