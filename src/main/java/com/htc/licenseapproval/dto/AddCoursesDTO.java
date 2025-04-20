package com.htc.licenseapproval.dto;

import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddCoursesDTO {
	
	private boolean certificateDone;

	private String courseDetails;

	private float hoursSpent;
	
	private Set<byte[]> certificate;
	
}
