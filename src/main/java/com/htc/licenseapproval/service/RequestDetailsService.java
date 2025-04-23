package com.htc.licenseapproval.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.htc.licenseapproval.dto.ChangeExpireData;
import com.htc.licenseapproval.dto.CoursesDTO;
import com.htc.licenseapproval.dto.RequestDetailsDTO;
import com.htc.licenseapproval.dto.RequestResponseDTO;
import com.htc.licenseapproval.entity.Courses;
import com.htc.licenseapproval.entity.LicenseLogMessages;
import com.htc.licenseapproval.entity.RequestDetails;

public interface RequestDetailsService {

	public RequestDetails findById(String requestID);
	
	public Set<CoursesDTO> getCourseandCertificates(String requestID) throws IOException;
	
	public RequestDetailsDTO addCourses(String requestID,Courses courses);
	
	public byte[] downloadCertificate(Long fileId) throws IOException, RuntimeException;
	
	public Set<LicenseLogMessages> licenseLogPerRequestId(String requestID);

	public RequestResponseDTO changeExpireDate(String requestID, ChangeExpireData changeExpiredDateDTO);
	
	public int totalEnrollmentcount(Long employeeId);
	
	//for controller usage
	public Map<Long, List<RequestDetailsDTO>> totalReport();

	public List<RequestDetails> allRequestDetailsByEmployeeId(Long employeeId);

	//for excel usage
	public Map<Long, List<RequestDetails>> totalReports();



	
}
