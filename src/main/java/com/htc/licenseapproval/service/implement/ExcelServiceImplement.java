package com.htc.licenseapproval.service.implement;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.htc.licenseapproval.dto.BUdetailsDTO;
import com.htc.licenseapproval.dto.LicenseDetailsDTO;
import com.htc.licenseapproval.dto.RequestDetailsDTO;
import com.htc.licenseapproval.entity.BUdetails;
import com.htc.licenseapproval.entity.Courses;
import com.htc.licenseapproval.entity.LicenseDetails;
import com.htc.licenseapproval.entity.RequestDetails;
import com.htc.licenseapproval.entity.RequestHeader;
import com.htc.licenseapproval.enums.LicenseType;
import com.htc.licenseapproval.mapper.MapperService;
import com.htc.licenseapproval.repository.BUdetailsRepository;
import com.htc.licenseapproval.service.ExcelService;
import com.htc.licenseapproval.service.RequestDetailsService;
import com.htc.licenseapproval.utils.DateFormatter;

@Service
public class ExcelServiceImplement implements ExcelService {

	@Autowired
	private RequestDetailsService requestDetailsService;
	
	@Autowired
	private BUdetailsRepository bUdetailsRepository;
	
	@Autowired
	private MapperService mapperService;

	@Override
	public byte[] downloadExcel() {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Licenses");

		Row header = sheet.createRow(0);
		header.createCell(0).setCellValue("Request id");
		header.createCell(1).setCellValue("Employee id");
		header.createCell(2).setCellValue("Employee name");
		header.createCell(3).setCellValue("Email id");
		header.createCell(4).setCellValue("Business Unit");
		header.createCell(5).setCellValue("Business Head");
		header.createCell(6).setCellValue("Business Need");
		header.createCell(7).setCellValue("Approved Manager");
		header.createCell(8).setCellValue("License Type");
		header.createCell(9).setCellValue("License start date");
		header.createCell(10).setCellValue("License expiry date");
		header.createCell(11).setCellValue("License Status");
		header.createCell(12).setCellValue("Enrollment count");
		header.createCell(13).setCellValue("Certification done");
		header.createCell(14).setCellValue("Course details");
		header.createCell(15).setCellValue("Hours spent");

		int rowNum = 1;
		Map<Long, List<RequestDetails>> overallReport = requestDetailsService.totalReports();

		for (Long empId : overallReport.keySet()) {

			for (RequestDetails license : overallReport.get(empId)) {
				RequestHeader requestHeader = license.getRequestHeader();
				BUdetails budetails = license.getRequestHeader().getBuDetails();
				LicenseDetails licenseDetails = license.getLicenseDetails();
				Row row = sheet.createRow(rowNum++);
				row.createCell(0).setCellValue(license.getRequestId());
				row.createCell(1).setCellValue(license.getEmpid());
				row.createCell(2).setCellValue(license.getEmpname());
				row.createCell(3).setCellValue(license.getEmailid());
				row.createCell(4).setCellValue(budetails.getBu());
				row.createCell(5).setCellValue(budetails.getBuHead());
				row.createCell(6).setCellValue(requestHeader.getBusinessNeed());
				row.createCell(7).setCellValue(requestHeader.getApprovedBy());
				row.createCell(8).setCellValue(licenseDetails.getLicenseType() != null 
						        ? licenseDetails.getLicenseType().toString() : "-");
				row.createCell(9).setCellValue(licenseDetails.getLicenseStartedDate() != null
								? licenseDetails.getLicenseStartedDate().format(DateFormatter.formatter): "-");
				row.createCell(10).setCellValue(licenseDetails.getLicenseExpireDate() != null
								? licenseDetails.getLicenseStartedDate().format(DateFormatter.formatter): "-");
				row.createCell(11).setCellValue(licenseDetails.getLicenceStatus().toString());
				row.createCell(12).setCellValue(requestDetailsService.totalEnrollmentcount(empId));

				Set<Courses> allCourses = license.getCourses();

				Iterator<Courses> iterator = allCourses.iterator();
				if (iterator.hasNext()) {
					Courses courses = iterator.next();

					row.createCell(13).setCellValue(courses.isCertificateDone());
					row.createCell(14).setCellValue(courses.getCourseDetails());
					row.createCell(15).setCellValue(courses.getHoursSpent());

				} else {
					row.createCell(13).setCellValue(false);
					row.createCell(14).setCellValue("-");
					row.createCell(15).setCellValue("-");
				}
			}
		}
		try (ByteArrayOutputStream fileOut = new ByteArrayOutputStream()) {
			workbook.write(fileOut);
			workbook.close();
			return fileOut.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

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
	
	
	@Override
	public Set<BUdetailsDTO> readExcelAndProcessforBudetails(MultipartFile file) {
		 Set<BUdetailsDTO> buDTOs = new HashSet<>();

		try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
			Sheet sheet = workbook.getSheetAt(0); // First sheet

			Iterator<Row> rowIterator = sheet.iterator();
			if (rowIterator.hasNext())
				rowIterator.next();

			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();

				BUdetails bUdetails = new BUdetails();

				bUdetails.setBu(row.getCell(1).getStringCellValue());
				bUdetails.setBuHead(row.getCell(2).getStringCellValue());
				bUdetails.setBuDeliveryHead(row.getCell(3).getStringCellValue());
				
				buDTOs.add(mapperService.toBUdetailsDTO(bUdetailsRepository.save(bUdetails)));							
			}

		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}

		return buDTOs;
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