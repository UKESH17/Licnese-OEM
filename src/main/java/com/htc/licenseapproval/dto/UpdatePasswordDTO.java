package com.htc.licenseapproval.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdatePasswordDTO {

	@NotBlank
	@NotNull
	private String username;
	@NotBlank(message = "Password cannot be null")
	@NotNull
	@Size(min = 12, message = "Password must be at least 12 characters long.")
	@Pattern(regexp = ".*[A-Z].*", message = "Password must contain at least one uppercase letter.")
	@Pattern(regexp = ".*[a-z].*", message = "Password must contain at least one lowercase letter.")
	@Pattern(regexp = ".*\\d.*", message = "Password must contain at least one number.")
	@Pattern(regexp = ".*[!@#$%^&*(),.?\":'{}|<>].*", message = "Password must contain at least one special character.")
	private String password;

}
