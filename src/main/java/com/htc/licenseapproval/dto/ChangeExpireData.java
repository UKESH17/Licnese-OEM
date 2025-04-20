package com.htc.licenseapproval.dto;

import lombok.ToString;

@ToString
public class ChangeExpireData {

	private String logMessage;
	private String expireDate;
	public ChangeExpireData(String logMessage, String expireDate) {
		super();
		this.logMessage = logMessage;
		this.expireDate = expireDate;
	}
	public String getLogMessage() {
		return logMessage;
	}
	public void setLogMessage(String logMessage) {
		this.logMessage = logMessage;
	}
	public String getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}
	
}
