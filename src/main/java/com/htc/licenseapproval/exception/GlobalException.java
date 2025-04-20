package com.htc.licenseapproval.exception;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {

	@ExceptionHandler(IOException.class)
	public ResponseEntity<String> handleException(IOException exc) {
		return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(exc.getMessage());
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<String> handleException(RuntimeException exc) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exc.getMessage());
	}
	
}