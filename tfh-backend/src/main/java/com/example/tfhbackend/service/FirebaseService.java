package com.example.tfhbackend.service;

import com.example.tfhbackend.dto.FirebaseRequest;

public interface FirebaseService {
    void sendNotification(FirebaseRequest request);
}
