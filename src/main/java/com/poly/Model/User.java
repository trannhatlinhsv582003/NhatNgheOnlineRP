package com.poly.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "Users")
@Data
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "UserID")
	private Integer userID;

	@Column(name = "FullName", nullable = false)
	private String fullName;

	@Column(name = "Email", nullable = false, unique = true)
	private String email;

	@Column(name = "PasswordHash", nullable = false)
	private String passwordHash;

	@Column(name = "Address")
	private String address;

	@Column(name = "Phone")
	private String phone;

	@Column(name = "Gender")
	private Boolean gender;

	@Column(name = "BirthDay")
	private LocalDate birthDay;

	@Column(name = "Role", nullable = false)
	private String role;

	@Column(name = "CreatedAt")
	private LocalDateTime createdAt;

	@Column(name = "ImageURL")
	private String imageUrl;

	@Column(name = "LastLogin")
	private LocalDateTime lastLogin;

	@Column(name = "LastAction")
	private String lastAction;

	@Column(name = "LastIP")
	private String lastIP;
}
