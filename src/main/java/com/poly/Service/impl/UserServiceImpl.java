// UserServiceImpl.java
package com.poly.Service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.poly.DTO.RegisterRequest;
import com.poly.Model.User;
import com.poly.Repository.UserRepository;
import com.poly.Security.CustomUserDetails;
import com.poly.Service.UserService;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public List<User> findAll() {
		return userRepository.findAll();
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

}
