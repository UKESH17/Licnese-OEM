//package com.htc.licenseapproval.controller;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.time.LocalDateTime;
//import java.util.Iterator;
//
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.ByteArrayResource;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RequestPart;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.htc.licenseapproval.service.ExcelService;
//
//import lombok.extern.slf4j.Slf4j;
//
//@RestController
//@Slf4j
//@RequestMapping("/licenseApproval/approvalRequest")
//@CrossOrigin(origins="http://localhost:5173")
//public class ExcelController {
//
//
//	@Autowired
//    private ExcelService excelServiceImplement;
//
//
////@GetMapping(value = "/download/{fileName}", produces = MediaType.APPLICATION_PDF_VALUE)
////public ResponseEntity<byte[]> downloadAllCertificates(@PathVariable String fileName) throws IOException {
////	byte[] approvalMail = requestListService.getApprovalMail(requestId);
////	return new ResponseEntity<>(approvalMail, HttpStatus.ACCEPTED);
////
////}
//
////@GetMapping(value = "/download/allCertificates/{requestId}", produces = MediaType.APPLICATION_PDF_VALUE)
////public ResponseEntity<List<Courses>> downloadAllCertificates(@PathVariable Long requestId) throws IOException {
////	byte[] approvalMail = requestListService.getApprovalMail(requestId);
////	return new ResponseEntity<>(approvalMail, HttpStatus.ACCEPTED);
////
////}
//
//
//	@GetMapping(value = "/excelReport/download",produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
//	public ResponseEntity<ByteArrayResource> download() {
//		ByteArrayResource resource = new ByteArrayResource(excelServiceImplement.downloadExcel());
//
//		return ResponseEntity.ok()
//				.header(HttpHeaders.CONTENT_DISPOSITION,
//						"attachment; filename=LicenseRequests_" + LocalDateTime.now() + ".xlsx")
////				.contentType(
////						MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
//				.body(resource);
//
//	}
//	
//	@PostMapping("/excelReport/upload")
//	public ResponseEntity<String> uploadNewLicenseRequestExcel(@RequestPart("Excel-file") MultipartFile file) {
//		if (file.isEmpty()) {
//			return ResponseEntity.badRequest().body("Please upload a file!");
//		}
//
//		String fileName = file.getOriginalFilename();
//		if (fileName != null && !(fileName.endsWith(".xls") || fileName.endsWith(".xlsx"))) {
//			return ResponseEntity.badRequest()
//					.body("Invalid file format! Please upload an Excel file (.xls or .xlsx).");
//		}
//		excelServiceImplement.readExcelAndProcess(file);
//		return ResponseEntity.ok("Excel successfully uploaded");
//		
//
//	}
//
////Microsoft form 
//
////@PostMapping
////public ResponseEntity<String> microsoftExcel(@RequestPart MultipartFile file) throws MalformedURLException, IOException{
////	
////		 Workbook workbook = new XSSFWorkbook(file.getInputStream());		            
////	            // Read first sheet
////	            Sheet sheet = workbook.getSheetAt(0);
////	            Iterator<Row> rowIterator = sheet.iterator();
////	            rowIterator.next();
////	            Row row = rowIterator.next();         
////		
////		String pdfPath = row.getCell(15).getStringCellValue();
////		
////		 HttpURLConnection connection = (HttpURLConnection) new URL(pdfPath).openConnection();
////	        connection.setRequestMethod("POST");
////	        
////		
////		
////
////	return null;
////	
////}
//
////FOR GOOGLE FORMS
//
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
//}
