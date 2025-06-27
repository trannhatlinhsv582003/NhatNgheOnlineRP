// UserService.java
package com.poly.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.poly.DTO.RegisterRequest;
import com.poly.Model.User;

@Service
public interface UserService {
	List<User> findAll();

	Optional<User> findById(Integer id);

	User save(User user);

	void deleteById(Integer id);

	// auth
	UserDetails loadUserByUsername(String email);

	boolean register(RegisterRequest request);
}
