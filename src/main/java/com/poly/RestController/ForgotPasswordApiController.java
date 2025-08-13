package com.poly.RestController;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.Service.UserService;

@RestController
@RequestMapping("/auth/forgot-password")
public class ForgotPasswordApiController {

    @Autowired
    private UserService userService;

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");

        try {
            userService.sendOtpToEmail(email);
            return ResponseEntity.ok("Sent");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Lỗi gửi email. Vui lòng thử lại.");
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String otp = payload.get("otp");
        if (!userService.verifyOtp(email, otp)) {
            return ResponseEntity.badRequest().body("Invalid OTP");
        }
        return ResponseEntity.ok("Verified");
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String newPassword = payload.get("newPassword");
        userService.updatePassword(email, newPassword);
        return ResponseEntity.ok("Password updated");
    }
}
