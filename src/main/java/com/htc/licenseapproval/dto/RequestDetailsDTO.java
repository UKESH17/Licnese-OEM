package com.htc.licenseapproval.dto;

import java.util.Set;

import com.htc.licenseapproval.enums.Status;

import lombok.Data;

@Data
public class RequestDetailsDTO {
	
	private String requestId;
	private Long empid;
	private String empname;
	private String emailid;
	private LicenseDetailsDTO licenseDetails;
	private Status status;
	private Set<CoursesDTO> courses;
	private String approvalGivenBy;

}
