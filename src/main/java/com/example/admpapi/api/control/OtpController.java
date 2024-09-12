package com.example.admpapi.api.control;

import com.example.admpapi.Utils;
import com.example.admpapi.service.EmailService;
import com.example.admpapi.service.OtpService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/otp")
public class OtpController {

    private final EmailService emailService;
    private final OtpService otpService;

    public OtpController(EmailService emailService, OtpService otpService) {
        this.emailService = emailService;
        this.otpService = otpService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendOtp(@RequestBody Map<String, String> body) {
        try {
            String email = body.get("email");
            String type = body.get("type");

            String key = Utils.createKeyOtp(email, type);

            String otp = otpService.generateOtp(key);
            emailService.sendOtpEmail(email, otp);

            return ResponseEntity.ok("OTP sent successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to send OTP");
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyOtp(@RequestBody Map<String, String> body) {
        try {
            String email = body.get("email");
            String type = body.get("type");
            String otp = body.get("otp");

            String key = Utils.createKeyOtp(email, type);

            if (otpService.verifyOtp(key, otp)) {
                otpService.clearOtp(key); // Clear OTP after verification
                return ResponseEntity.ok("OTP verified successfully");
            } else {
                return ResponseEntity.status(400).body("Invalid OTP");
            }
        } catch (Exception e){
            return ResponseEntity.status(500).body("Failed to verify OTP");
        }
    }
}
