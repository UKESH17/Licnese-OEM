package com.htc.licenseapproval.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
public class OTP {

	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String otp;
	@Column(nullable = false)
	private LocalDateTime expiryAt;
	@OneToOne
	private UserCredentials user;
	
} 
