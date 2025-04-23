package com.htc.licenseapproval.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.htc.licenseapproval.dto.BUdetailsDTO;
import com.htc.licenseapproval.dto.ChangeExpireData;
import com.htc.licenseapproval.dto.CoursesDTO;
import com.htc.licenseapproval.dto.LicenseApprovalDTO;
import com.htc.licenseapproval.dto.NewRequestListDTO;
import com.htc.licenseapproval.dto.RequestDetailsDTO;
import com.htc.licenseapproval.dto.RequestResponseDTO;
import com.htc.licenseapproval.entity.BUdetails;
import com.htc.licenseapproval.entity.Courses;
import com.htc.licenseapproval.entity.UploadedFile;
import com.htc.licenseapproval.enums.RequestType;
import com.htc.licenseapproval.enums.Status;
import com.htc.licenseapproval.file.compressor.Compressor;
import com.htc.licenseapproval.repository.BUdetailsRepository;
import com.htc.licenseapproval.response.BaseResponse;
import com.htc.licenseapproval.service.ExcelService;
import com.htc.licenseapproval.service.RequestDetailsService;
import com.htc.licenseapproval.service.RequestHeaderService;
import com.htc.licenseapproval.utils.DateFormatter;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/licenseApproval/approvalRequest")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "License Approval Request APIs", description = "Endpoints for handling license approval requests")
public class RequestListController {

	@Autowired
	private RequestHeaderService requestListService;

	@Autowired
	private RequestDetailsService requestDetailsService;

	@Autowired
	private Compressor compressor;

	@Autowired
	private ExcelService excelService;
	
	@Autowired
	private BUdetailsRepository buRepository;
	

	@Operation(summary = "For cheecking purpose", description = "Sample endpoint")
	@PostMapping(value = "/check")
	public LocalDateTime postMethodName(@RequestParam String expiredDate,@RequestParam String reason)
	{
		ChangeExpireData changeExpiredDateDTO = new ChangeExpireData(reason, expiredDate);
		System.out.println(changeExpiredDateDTO);

		return DateFormatter.normaliseDate(changeExpiredDateDTO.getExpireDate());
	}

	
	@Operation(summary = "Create new license request", description = "Submit a new license request with approval mail and optional Excel file")
	@PostMapping(value = "/newRequest", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<BaseResponse<RequestResponseDTO>> newRequest(
			@RequestPart(value="JSON-body",required = true) String newRequestListJson,
			@RequestPart(value ="approval-mail"
			,required = true) MultipartFile approvalmail,
			@RequestPart(value = "excel", required = false) MultipartFile excel) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		NewRequestListDTO newRequestListDTO = objectMapper.readValue(newRequestListJson, NewRequestListDTO.class);
		
		String fileName = approvalmail.getOriginalFilename();
		if (fileName != null && !(fileName.endsWith(".pdf"))) {
			BaseResponse<RequestResponseDTO> response = new BaseResponse<RequestResponseDTO>();
			response.setMessage("Invalid file format! Please upload a PDF file (.pdf).");
			response.setData(null);
			return ResponseEntity.badRequest().body(response);
		}

		UploadedFile approvalMail = new UploadedFile();
		approvalMail.setFileName(approvalmail.getOriginalFilename());
		approvalMail.setFileType(approvalmail.getContentType());
		approvalMail.setFileData(compressor.compress(approvalmail.getBytes()));

		UploadedFile excelFile = new UploadedFile();

		if (newRequestListDTO.getRequestType().equals(RequestType.MULTIPLE) && excel != null) {
			newRequestListDTO.setRequestDetails(excelService.readExcelAndProcess(excel));

			excelFile.setFileName(excel.getOriginalFilename());
			excelFile.setFileType(excel.getContentType());
			excelFile.setFileData(compressor.compress(excel.getBytes()));
			newRequestListDTO.setExcelFile(excelFile);
		}

		BaseResponse<RequestResponseDTO> response = new BaseResponse<RequestResponseDTO>();
		response.setMessage("Request successfully received and file uploaded successfully with name "
				+ approvalmail.getOriginalFilename());
		response.setData(requestListService.newRequestHeader(newRequestListDTO, approvalMail));
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@Operation(summary = "Approve or reject a request", description = "Change the status of a license request (approve or reject)")
	@PutMapping(value = "/approval/{requestHeaderId}")
	public ResponseEntity<LicenseApprovalDTO> approveRequest(@RequestParam Status status, @PathVariable String requestHeaderId)
			throws IOException {
		return new ResponseEntity<>(requestListService.requestApproval(requestHeaderId, status), HttpStatus.ACCEPTED);
	}
	
	@Operation(summary = "For updating end date", description = "Change the end date of the license request ")
	@PutMapping(value = "/endDate/{requestID}")
	public ResponseEntity<RequestResponseDTO> changeExpiryDate(@RequestParam String expiredDate,@RequestParam String reason,@PathVariable String requestID)
	{
		ChangeExpireData changeExpiredDateDTO = new ChangeExpireData(reason, expiredDate);
		
		return new ResponseEntity<>(requestDetailsService.changeExpireDate(requestID, changeExpiredDateDTO), HttpStatus.ACCEPTED);
	}

	@Operation(summary = "Add courses to a request", description = "Attach course details and certificate files to a request")
	@PostMapping(value = "/add/Courses/{requestDetailsId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<RequestDetailsDTO> addCourses(
		    @PathVariable String requestDetailsId,
		    @RequestPart(value="course-JSON") String coursesDTOjson,
		    @RequestPart(value="certificates",required = false) MultipartFile[] files
		) 
			throws IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		CoursesDTO coursesDTOs = objectMapper.readValue(coursesDTOjson, CoursesDTO.class);
		
		Courses courses = new Courses();
		courses.setCertificateDone(coursesDTOs.isCertificateDone());
		courses.setCourseDetails(coursesDTOs.getCourseDetails());
		courses.setHoursSpent(coursesDTOs.getHoursSpent());

		if(files!=null) {
		Set<UploadedFile> uploadedFiles = new HashSet<>();
		for (MultipartFile multipartFile : files) {
			UploadedFile uploadedFile = new UploadedFile();
			uploadedFile.setFileData(compressor.compress(multipartFile.getBytes()));
			uploadedFile.setFileName(multipartFile.getOriginalFilename());
			uploadedFile.setFileType(multipartFile.getContentType());
			uploadedFiles.add(uploadedFile);
			uploadedFile.setCourses(courses);
		}
		courses.setCertificates(uploadedFiles);
		}
		return ResponseEntity.ok(requestDetailsService.addCourses(requestDetailsId, courses));
	}
	
	@PostMapping("/addBU")
	public ResponseEntity<BUdetails> addBU(@RequestBody BUdetailsDTO bu){
		BUdetails bUdetails = new BUdetails();
		bUdetails.setBu(bu.getBu());
		bUdetails.setBuHead(bu.getBuHead());
		bUdetails.setBuDeliveryHead(bu.getBuDeliveryHead());
		return ResponseEntity.ok(buRepository.save(bUdetails));
	} 
	
	@PostMapping(value="/excel/addAllBU", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Set<BUdetailsDTO>> addBUbyExcel(@RequestPart("bu-excel") MultipartFile file){ 
		return ResponseEntity.ok(excelService.readExcelAndProcessforBudetails(file));
	} 
}
