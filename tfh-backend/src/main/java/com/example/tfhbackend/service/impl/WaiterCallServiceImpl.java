package com.example.tfhbackend.service.impl;

import com.example.tfhbackend.dto.FirebaseRequest;
import com.example.tfhbackend.model.User;
import com.example.tfhbackend.repository.TableRepository;
import com.example.tfhbackend.service.FirebaseService;
import com.example.tfhbackend.service.WaiterCallService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class WaiterCallServiceImpl implements WaiterCallService {
    private static final String NOTIFICATION_TITLE = "Your are called.";
    private static final String NOTIFICATION_BODY = "Table %s called you";

    private final TableRepository tableRepository;
    private final FirebaseService firebaseService;

    @Override
    @Transactional(readOnly = true)
    public void call(Integer tableNumber) {
        User waiter = tableRepository.getCurrentWaiter(tableNumber)
                .orElseThrow();

        FirebaseRequest request = new FirebaseRequest();
        request.setTitle(NOTIFICATION_TITLE);
        request.setBody(String.format(NOTIFICATION_BODY, tableNumber));
        request.setDeviceId(waiter.getDeviceId());

        firebaseService.sendNotification(request);
    }
}
