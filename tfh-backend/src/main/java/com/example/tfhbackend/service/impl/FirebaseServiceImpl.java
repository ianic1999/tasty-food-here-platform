package com.example.tfhbackend.service.impl;

import com.example.tfhbackend.dto.FirebaseRequest;
import com.example.tfhbackend.service.FirebaseService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
class FirebaseServiceImpl implements FirebaseService {
    private final FirebaseMessaging firebaseMessaging;

    @Override
    public void sendNotification(FirebaseRequest request) {
        try {
            Notification notification = Notification.builder()
                    .setTitle(request.getTitle())
                    .setBody(request.getBody())
                    .build();
            Message message = Message.builder()
                    .setToken(request.getDeviceId())
                    .setNotification(notification)
                    .build();

            firebaseMessaging.send(message);
        } catch (FirebaseMessagingException e) {
            log.warn(e.getMessage());
        }
    }
}
