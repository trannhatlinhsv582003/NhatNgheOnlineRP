// UserRepository.java
package com.poly.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.Model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	// Optional: tìm user bằng email
	User findByEmail(String email);

	// Tìm kiếm theo email
	boolean existsByEmail(String email);
}
