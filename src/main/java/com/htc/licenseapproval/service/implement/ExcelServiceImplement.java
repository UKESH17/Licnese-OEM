package com.htc.licenseapproval.service.implement;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.htc.licenseapproval.dto.LicenseDetailsDTO;
import com.htc.licenseapproval.dto.RequestDetailsDTO;
import com.htc.licenseapproval.entity.RequestHeader;
import com.htc.licenseapproval.enums.LicenseType;
import com.htc.licenseapproval.repository.RequestHeaderRepository;
import com.htc.licenseapproval.service.ExcelService;
import com.htc.licenseapproval.service.RequestHeaderService;

@Service
public class ExcelServiceImplement implements ExcelService {

	@Autowired
	private  RequestHeaderRepository requestListRepository;

	@Autowired
	private  RequestHeaderService requestListService;



//	@Override
//	public byte[] downloadExcel() {
//		Workbook workbook = new XSSFWorkbook();
//		Sheet sheet = workbook.createSheet("Licenses");
//
//		// Create Header Row
//		Row header = sheet.createRow(0);
//		header.createCell(0).setCellValue("Request Header Id");
//		header.createCell(1).setCellValue("Requestor Name");
//		header.createCell(2).setCellValue("Request Type");
//		header.createCell(3).setCellValue("Business Unit");
//		header.createCell(4).setCellValue("Business Head");
//		header.createCell(5).setCellValue("Business Need");
//		header.createCell(6).setCellValue("Approved Manager");
//	//	header.createCell(7).setCellValue("Request Id");
//	//	header.createCell(8).setCellValue("Employee Id");
//	//	header.createCell(9).setCellValue("Employee Name");
//	//	header.createCell(10).setCellValue("Email Id");
//		header.createCell(11).setCellValue("Request Status");
//	//	header.createCell(12).setCellValue("License Type");
//	//	header.createCell(13).setCellValue("License Start Date");
//	//	header.createCell(14).setCellValue("License Expiry Date");
//	//	header.createCell(15).setCellValue("Requested Date");
//		header.createCell(16).setCellValue("License Status");
//	//	header.createCell(17).setCellValue("Courses Details");
//		header.createCell(18).setCellValue("Hours Spent");
//		header.createCell(19).setCellValue("Certificate Done");
//		header.createCell(20).setCellValue("Approval Given By");
//		header.createCell(21).setCellValue("Approval Mail");
//		header.createCell(22).setCellValue("Excel file");
//
//		int rowNum = 1;
//		for (RequestHeader license : requestListRepository.findAll()) {
//			
//			Row row = sheet.createRow(rowNum++);
//			row.createCell(0).setCellValue(license.getRequestHeaderId());
//			row.createCell(1).setCellValue(license.getRequestorName());
//			row.createCell(2).setCellValue(license.getRequestType()!=null ? license.getRequestType().toString():"");
//			row.createCell(3).setCellValue(license.getBuDetails().getBu());
//			row.createCell(4).setCellValue(license.getBuDetails().getBuHead());
//			row.createCell(5).setCellValue(license.getBusinessNeed());
//			row.createCell(6).setCellValue(license.getApprovedBy());
//			row.createCell(7).setCellValue(license.);
//			row.createCell(8).setCellValue(license.getCourses() != null ?license.getCourses().getHoursSpent():0);
//			row.createCell(9).setCellValue(license.getLicenseStartedDate()!=null?license.getLicenseStartedDate().format(formatter):"");
//			row.createCell(10).setCellValue(license.getLicenseExpireDate()!=null?license.getLicenseExpireDate().format(formatter):"");
//			row.createCell(11).setCellValue(license.getStatus()!=null ? license.getStatus().toString():"");
//			row.createCell(12).setCellValue(license.getRequestedDate()!=null?license.getRequestedDate().format(formatter):"");
//			row.createCell(13).setCellValue(license.getLicenceStatus()!=null ? license.getLicenceStatus().toString():"");
//			row.createCell(14).setCellValue(license.getBUhead()!=null ? license.getBUhead():"");
//			row.createCell(15).setCellValue(license.getBUdetails()!=null ? license.getBUdetails():"");
//			row.createCell(16).setCellValue(license.getBusinessNeed()!=null ? license.getBusinessNeed():"");
//
//		}
//		try (ByteArrayOutputStream fileOut = new ByteArrayOutputStream()) {
//			workbook.write(fileOut);
//			workbook.close();
//			return fileOut.toByteArray();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
	
	/* TO READ AND PROCESS REQUEST EXCEL */

	@Override
	public Set<RequestDetailsDTO> readExcelAndProcess(MultipartFile file) {
	    Set<RequestDetailsDTO> requestDetails = new HashSet<>();
	    
	    try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
	        Sheet sheet = workbook.getSheetAt(0); // First sheet

	        Iterator<Row> rowIterator = sheet.iterator();
	        if (rowIterator.hasNext())
	            rowIterator.next(); 

	        while (rowIterator.hasNext()) {
	            Row row = rowIterator.next();
	            
	            RequestDetailsDTO request = new RequestDetailsDTO();
	            LicenseDetailsDTO license = new LicenseDetailsDTO();

	            request.setEmpid((long) row.getCell(0).getNumericCellValue());
	            request.setEmpname(row.getCell(1).getStringCellValue());
	            request.setEmailid(row.getCell(2).getStringCellValue());

	            String licenseType = row.getCell(3).getStringCellValue();
	            license.setLicenseType(toLicenseType(licenseType));
	            request.setLicenseDetails(license);     

	            requestDetails.add(request);
	        }

	    } catch (IOException e) {
	        throw new RuntimeException(e.getMessage());
	    }

	    return requestDetails;
	}





	private LicenseType toLicenseType(String type) {

		switch (type.toUpperCase()) {
		case "PLURALS":
			return LicenseType.PLURALS;
		case "LINKEDIN":
			return LicenseType.LINKEDIN;
		}

		throw new RuntimeException("LicenseType not found with " + type);

	}


}