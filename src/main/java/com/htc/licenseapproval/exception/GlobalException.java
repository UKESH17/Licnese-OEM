package com.htc.licenseapproval.exception;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.htc.licenseapproval.response.BaseResponse;

@RestControllerAdvice
public class GlobalException {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<BaseResponse<List<String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {

		List<String> errors = ex.getBindingResult().getFieldErrors().stream()
				.map(error -> error.getField() + ": " + error.getDefaultMessage()).collect(Collectors.toList());
		
		BaseResponse<List<String>> response = new BaseResponse<>();
		response.setCode(HttpStatus.BAD_REQUEST.value());
		response.setData(errors);
		response.setMessage("Login");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		
	}

	@ExceptionHandler(IOException.class)
	public ResponseEntity<BaseResponse<String>> handleException(IOException exc) {
		BaseResponse<String> response = new BaseResponse<>();
		response.setCode(HttpStatus.BAD_REQUEST.value());
		response.setData(exc.getMessage());
		response.setMessage("Login");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<BaseResponse<String>> handleException(RuntimeException exc) {
		BaseResponse<String> response = new BaseResponse<>();
		response.setCode(HttpStatus.BAD_REQUEST.value());
		response.setData(exc.getMessage());
		response.setMessage("Login");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

}