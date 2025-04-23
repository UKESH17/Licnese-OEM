package com.htc.licenseapproval.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.htc.licenseapproval.entity.UserLog;
import com.htc.licenseapproval.repository.LogRepository;
import com.htc.licenseapproval.service.ExcelService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/licenseApproval/approvalRequest")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Excel Api", description = "For Handling Excels")
public class ExcelController {

	@Autowired
	private ExcelService excelService;

	@Autowired
	private LogRepository logRepository;

	@GetMapping(value = "/excelReport/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@Operation(summary = "Overall Excel report")
	public ResponseEntity<ByteArrayResource> overallReport() {
		ByteArrayResource resource = new ByteArrayResource(excelService.downloadExcel());

		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		UserLog userLog = UserLog.builder().logDetails("Overall repost excel downloaded by -> " + username)
				.loggedTime(LocalDateTime.now()).build();

		logRepository.save(userLog);

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=OverallReport-" + LocalDate.now() + ".xlsx")
				.contentType(
						MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
				.body(resource);

	}
	
	//not completed

	@PostMapping("/excelRequest/upload")
	@Operation(summary = "UPLOAD Excel requests with the help of microsoft form")
	public ResponseEntity<String> uploadNewLicenseRequestExcel(@RequestPart("Excel-file") MultipartFile file) {
		if (file.isEmpty()) {
			return ResponseEntity.badRequest().body("Please upload a file!");
		}

		String fileName = file.getOriginalFilename();
		if (fileName != null && !(fileName.endsWith(".xls") || fileName.endsWith(".xlsx"))) {
			return ResponseEntity.badRequest()
					.body("Invalid file format! Please upload an Excel file (.xls or .xlsx).");
		}
		excelService.readExcelAndProcess(file);

		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		UserLog userLog = UserLog.builder().logDetails("Microsoft excel license request uploaded by -> " + username)
				.loggedTime(LocalDateTime.now())
				.build();

		logRepository.save(userLog);

		return ResponseEntity.ok("Excel successfully uploaded");

	}
//
////Microsoft form 
////yet to do
//	@PostMapping
//	public ResponseEntity<String> microsoftExcel(@RequestPart MultipartFile file)
//			throws MalformedURLException, IOException {
//
//		Workbook workbook = new XSSFWorkbook(file.getInputStream());
//		// Read first sheet
//		Sheet sheet = workbook.getSheetAt(0);
//		Iterator<Row> rowIterator = sheet.iterator();
//		rowIterator.next();
//		Row row = rowIterator.next();
//
//		String pdfPath = row.getCell(15).getStringCellValue();
//
//		HttpURLConnection connection = (HttpURLConnection) new URL(pdfPath).openConnection();
//		connection.setRequestMethod("POST");
//
//		return null;
//
//	}
//
////FOR GOOGLE FORMS
////Not in use
//	@GetMapping(value = "/excelupload/url", produces = MediaType.APPLICATION_PDF_VALUE)
//	public ResponseEntity<byte[]> accessExcel(@RequestParam String url) {
//		try {
//			// Convert Google Sheets URL if necessary
//			if (url.contains("docs.google.com/spreadsheets")) {
//				url = url.replaceAll("/edit.*", "/export?format=xlsx");
//				System.out.println(url);
//			}
//
//			// Open URL connection
//			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
//			connection.setRequestMethod("GET");
//
//			// Read InputStream
//			try (InputStream inputStream = connection.getInputStream();
//					Workbook workbook = new XSSFWorkbook(inputStream)) {
//
//				// Read first sheet
//				Sheet sheet = workbook.getSheetAt(0);
//				Iterator<Row> rowIterator = sheet.iterator();
//
//				if (!rowIterator.hasNext()) {
//					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//				}
//
//				rowIterator.next(); // Skip header row
//				if (!rowIterator.hasNext()) {
//					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//				}
//
//				// Extract file path from cell
//				Row row = rowIterator.next();
//				String filePath = row.getCell(3).getStringCellValue();
//				System.out.println(filePath);
//				String id = filePath.split("id=")[1];
//				String toDownload = "https://drive.google.com/uc?export=download&id=" + id;
//				System.out.println(toDownload);
//				// Path file = Paths.get(toDownload);
//				HttpURLConnection connection2 = (HttpURLConnection) new URL(toDownload).openConnection();
//				connection2.setRequestMethod("GET");
//				InputStream inputStream2 = connection2.getInputStream();
//				byte[] pdf = inputStream2.readAllBytes();
//
//				return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(pdf);
//			}
//		} catch (MalformedURLException e) {
//			log.info("MalformedURLException ");
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//
//		} catch (IOException e) {
//			log.info("IOException");
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//		}
//	}
}
