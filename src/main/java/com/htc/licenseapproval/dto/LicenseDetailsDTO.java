package com.htc.licenseapproval.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.htc.licenseapproval.enums.LicenceStatus;
import com.htc.licenseapproval.enums.LicenseType;
import lombok.Data;

@Data
public class LicenseDetailsDTO {

	private LicenseType licenseType ;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "IST")
	private LocalDateTime licenseStartedDate;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "IST")
	private LocalDateTime licenseExpireDate;
	 
	private LocalDateTime requestedDate;
	
	private LicenceStatus licenceStatus;
	
}
