package com.univo.be_univo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SmsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SmsService.class);

    public void sendPasswordResetOtp(String phoneNumber, String otp) {
        LOGGER.info("Password reset OTP for phone {} is {}", phoneNumber, otp);
    }
}
