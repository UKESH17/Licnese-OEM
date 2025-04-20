package com.htc.licenseapproval.service;

import java.io.IOException;
import java.time.Month;
import java.util.List;
import java.util.Map;

import com.htc.licenseapproval.dto.LicenseApprovalDTO;
import com.htc.licenseapproval.dto.NewRequestListDTO;
import com.htc.licenseapproval.dto.RequestResponseDTO;
import com.htc.licenseapproval.dto.ResponseDTO;
import com.htc.licenseapproval.entity.RequestDetails;
import com.htc.licenseapproval.entity.RequestHeader;
import com.htc.licenseapproval.entity.UploadedFile;
import com.htc.licenseapproval.enums.Status;

public interface RequestHeaderService {
	
	public RequestHeader findById(String requestId);

	public RequestResponseDTO newRequestHeader(NewRequestListDTO newRequestListDTO, UploadedFile approvalMail);

	public LicenseApprovalDTO requestApproval(String requestId, Status status);

	public byte[] getApprovalMail(String requestId) throws IOException;
	
	public byte[] getExcelFile(String requestId) throws IOException;

	public ResponseDTO<List<RequestResponseDTO>> totalRequest();

	public List<RequestDetails> getAllRequestLists();

	public ResponseDTO<List<RequestResponseDTO>> totalRequestsByEmp(Long empid);
	
	public ResponseDTO<List<RequestResponseDTO>> allActiveLicense();

	public ResponseDTO<List<RequestResponseDTO>> allPendingLicense();
 
	public ResponseDTO<List<RequestResponseDTO>> allExpireSoonLicense();

	public ResponseDTO<List<RequestResponseDTO>> allExpiredLicense();

	public ResponseDTO<List<RequestResponseDTO>> pendingRequest();

	public ResponseDTO<List<RequestResponseDTO>> rejectedRequest();

	public ResponseDTO<List<RequestResponseDTO>> approvedRequest();

	public Map<Month, Integer> quarterlyReport();

	public Map<Month, Integer> annualReport();

	public ResponseDTO<List<RequestResponseDTO>> totalRequestPerBU(String name);

	

}
