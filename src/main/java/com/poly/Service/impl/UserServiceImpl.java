// UserServiceImpl.java
package com.poly.Service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.poly.DTO.RegisterRequest;
import com.poly.Model.OtpToken;
import com.poly.Model.User;
import com.poly.Repository.OtpRepository;
import com.poly.Repository.UserRepository;
import com.poly.Security.CustomUserDetails;
import com.poly.Service.UserService;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private JavaMailSender mailSender;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }

    // auth
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Không tìm thấy người dùng");
        }
        return new CustomUserDetails(user);
    }

    @Override
    public boolean register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return false;
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setFullName(request.getFirstName() + " " + request.getLastName());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole("Customer");
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);
        return true;
    }

    @Override
    public List<User> findByRole(String role) {
        return userRepository.findByRole(role);
    }

    //MAIL
    @Override
    public void sendOtpToEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("Email không tồn tại");
        }

        String otp = String.format("%06d", new java.util.Random().nextInt(999999));

        OtpToken token = new OtpToken();
        token.setEmail(email);
        token.setOtp(otp);
        token.setCreatedAt(LocalDateTime.now());
        token.setVerified(false);
        otpRepository.save(token);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Mã OTP khôi phục mật khẩu");
        message.setText("Mã OTP của bạn là: " + otp + "\nThời hạn sử dụng: 5 phút.");
        mailSender.send(message);
    }

    @Override
    public boolean verifyOtp(String email, String otp) {
        Optional<OtpToken> optional = otpRepository.findTopByEmailOrderByCreatedAtDesc(email);
        if (optional.isEmpty()) {
            return false;
        }

        OtpToken token = optional.get();

        if (token.isVerified()) {
            return false;
        }

        if (java.time.Duration.between(token.getCreatedAt(), LocalDateTime.now()).toMinutes() > 5) {
            return false;
        }

        if (!token.getOtp().equals(otp)) {
            return false;
        }

        token.setVerified(true);
        otpRepository.save(token);
        return true;
    }

    @Override
    public void updatePassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("Email không tồn tại");
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

}
