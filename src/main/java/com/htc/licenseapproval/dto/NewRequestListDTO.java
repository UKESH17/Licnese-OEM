package com.htc.licenseapproval.dto;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.htc.licenseapproval.entity.UploadedFile;
import com.htc.licenseapproval.enums.RequestType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NewRequestListDTO {

	private String requestorName;
	private RequestType requestType;
	private BUdetailsDTO buDetails;
	private String approvedBy;
	private Set<RequestDetailsDTO> requestDetails;
	@JsonIgnore
	private UploadedFile excelFile;
	private String businessNeed;

 }
