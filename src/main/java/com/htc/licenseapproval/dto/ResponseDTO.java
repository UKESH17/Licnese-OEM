package com.htc.licenseapproval.dto;

import lombok.Data;

@Data
public class ResponseDTO<T> {
	private long count;
	private T data;
}
