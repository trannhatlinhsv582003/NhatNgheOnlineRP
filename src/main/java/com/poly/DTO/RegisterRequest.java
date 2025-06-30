package com.poly.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
	// Validations
	@NotBlank(message = "Email không được để trống")
	@Email(message = "Email không đúng định dạng")
	private String email;

	@NotBlank(message = "Họ không được để trống")
	private String firstName;

	@NotBlank(message = "Tên không được để trống")
	private String lastName;

	@NotBlank(message = "Mật khẩu không được để trống")
	@Size(min = 3, message = "Mật khẩu phải có ít nhất 3 ký tự")
	private String password;
}
