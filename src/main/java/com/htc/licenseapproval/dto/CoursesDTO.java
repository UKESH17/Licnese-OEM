package com.htc.licenseapproval.dto;

import java.util.Set;

import lombok.Data;

@Data
public class CoursesDTO {
	
	private boolean certificateDone;

	private String courseDetails;

	private float hoursSpent;
	
	private Set<DownloadResponse> certificate;
	
}
