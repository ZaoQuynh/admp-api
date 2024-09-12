package com.example.admpapi.service;

import org.springframework.stereotype.Service;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

    private final ConcurrentHashMap<String, String> otpStorage = new ConcurrentHashMap<>();

    public String generateOtp(String key) {

        String otp = String.format("%06d", new Random().nextInt(999999));
        otpStorage.put(key, otp);
        return otp;
    }

    public boolean verifyOtp(String key, String otp) {
        return otp.equals(otpStorage.get(key));
    }

    public void clearOtp(String key) {
        otpStorage.remove(key);
    }
}
