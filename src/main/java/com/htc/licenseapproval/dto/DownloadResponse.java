package com.htc.licenseapproval.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DownloadResponse {
	
	private String filename;
	private String url;

}
