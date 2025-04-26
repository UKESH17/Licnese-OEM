package com.htc.licenseapproval.dto;

import jakarta.validation.constraints.*;

import lombok.Data;

@Data
public class RegisterUser {

	@NotBlank(message = "Username cannot be blank")
	@NotNull
	private String username;

	@NotNull(message = "Password cannot be null")
	@NotBlank
	@Size(min = 12, message = "Password must be at least 12 characters long.")
	@Pattern(regexp = ".*[A-Z].*", message = "Password must contain at least one uppercase letter.")
	@Pattern(regexp = ".*[a-z].*", message = "Password must contain at least one lowercase letter.")
	@Pattern(regexp = ".*\\d.*", message = "Password must contain at least one number.")
	@Pattern(regexp = ".*[!@#$%^&*(),.?\":'{}|<>].*", message = "Password must contain at least one special character.")
	private String password;
	
	@NotBlank
	@Email
	private String email;

}
