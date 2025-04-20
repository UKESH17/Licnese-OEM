package com.htc.licenseapproval.dto;

import java.util.Set;

import com.htc.licenseapproval.enums.RequestType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestResponseDTO {

	private String requestHeaderId;
	private String requestorName;
	private RequestType requestType;
	private BUdetailsDTO buDetails;
	private String approvedBy;
	private DownloadResponse approvalMail;
	private DownloadResponse excelFile;
	private Set<RequestDetailsDTO> requestDetails;

}
