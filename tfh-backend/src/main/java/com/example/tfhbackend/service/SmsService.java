package com.example.tfhbackend.service;

public interface SmsService {
    void sendConfirmationSms(String referenceId, String phone, String fullName);
}
