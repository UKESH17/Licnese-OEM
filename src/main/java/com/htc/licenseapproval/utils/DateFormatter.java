package com.htc.licenseapproval.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import lombok.Data;

@Data
public class DateFormatter {
	public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	public static LocalDateTime normaliseDate(LocalDateTime dateTime) {
		return LocalDateTime.parse(dateTime.format(DateFormatter.formatter), DateFormatter.formatter);
	}
	
	public static LocalDateTime normaliseDate(String dateTime) {
		return LocalDateTime.parse(dateTime, DateFormatter.formatter);
	}
}
