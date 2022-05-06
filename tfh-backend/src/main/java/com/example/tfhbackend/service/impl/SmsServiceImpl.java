package com.example.tfhbackend.service.impl;

import com.example.tfhbackend.service.SmsService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
class SmsServiceImpl implements SmsService {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${sms.confirmation.message}")
    private String confirmationMessage;

    @Override
    public void sendConfirmationSms(String referenceId, String phone, String fullName) {
        try {
            Twilio.init(accountSid, authToken);
            Message.creator(
                    new PhoneNumber(phone),
                    new PhoneNumber("+19378218537"),
                    String.format(confirmationMessage, fullName, referenceId)
            )
                   .create();
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }
}
