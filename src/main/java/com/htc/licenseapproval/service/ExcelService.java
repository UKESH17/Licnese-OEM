package com.htc.licenseapproval.service;

import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.htc.licenseapproval.dto.RequestDetailsDTO;

public interface ExcelService {

	public Set<RequestDetailsDTO> readExcelAndProcess(MultipartFile file);

	//public byte[] downloadExcel();

}
