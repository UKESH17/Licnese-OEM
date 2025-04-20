package com.htc.licenseapproval.dto;

import java.util.Set;

import lombok.Data;

@Data
public class LicenseApprovalDTO {

	private String requestHeaderId;
	private String message;
	private Set<LicenseDetailsDTO> licenseDetailsDTOs;
	private String approvalGivenBy;

}
