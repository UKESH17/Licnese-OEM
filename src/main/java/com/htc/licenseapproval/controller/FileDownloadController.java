package com.htc.licenseapproval.controller;

import java.io.IOException;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.htc.licenseapproval.service.RequestDetailsService;
import com.htc.licenseapproval.service.RequestHeaderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
@RestController
@RequestMapping("/licenseApproval/approvalRequest")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "File Download APIs", description = "For downloading files")
public class FileDownloadController {

	@Autowired
	private RequestHeaderService requestListService;

	@Autowired
	private RequestDetailsService requestDetailsService;

	@Operation(summary = "Download certificate", description = "Download certificate PDF by file ID")
	@GetMapping(value = "/download/certificates/{fileId}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<byte[]> downloadCertificate(@PathVariable Long fileId) throws IOException {
		return ResponseEntity.ok(requestDetailsService.downloadCertificate(fileId));
	}

	@Operation(summary = "Download request list Excel", description = "Download Excel sheet of license requests by request-header-id")
	@GetMapping(value = "/download/requestlistexcel/{requestId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<ByteArrayResource> downloadRequestExcel(@PathVariable String requestId) throws IOException {
		ByteArrayResource resource = new ByteArrayResource(requestListService.getExcelFile(requestId));

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=LicenseRequests_" + LocalDate.now() + ".xlsx")
				.contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
				.body(resource);
	}

	@Operation(summary = "Download approval mail", description = "Download approval mail as PDF by request ID")
	@GetMapping(value = "/download/approvalMail/{requestId}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<byte[]> downloadApprovalMail(@PathVariable String requestId) throws IOException {
		byte[] approvalMail = requestListService.getApprovalMail(requestId);
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(approvalMail);
	}
}

