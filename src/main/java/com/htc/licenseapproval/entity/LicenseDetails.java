package com.htc.licenseapproval.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.htc.licenseapproval.enums.LicenceStatus;
import com.htc.licenseapproval.enums.LicenseType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class LicenseDetails extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long licenseDetailsId;
	
	@Enumerated(EnumType.STRING)
	private LicenseType licenseType =LicenseType.NULL;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "IST")
	private LocalDateTime licenseStartedDate;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "IST")
	private LocalDateTime licenseExpireDate;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "IST")
	private LocalDateTime requestedDate;
	
	@Enumerated(EnumType.STRING)
	private LicenceStatus licenceStatus;
	
}
