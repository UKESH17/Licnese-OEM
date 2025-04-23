package com.htc.licenseapproval.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.htc.licenseapproval.constants.LogMessages;
import com.htc.licenseapproval.dto.LoginRequest;
import com.htc.licenseapproval.dto.RegisterUser;
import com.htc.licenseapproval.entity.OTP;
import com.htc.licenseapproval.entity.UpdatePasswordDTO;
import com.htc.licenseapproval.entity.UserCredentials;
import com.htc.licenseapproval.entity.UserLog;
import com.htc.licenseapproval.enums.OTPtype;
import com.htc.licenseapproval.repository.LogRepository;
import com.htc.licenseapproval.repository.UserCredentialsRepository;
import com.htc.licenseapproval.service.UserService;
import com.htc.licenseapproval.utils.EmailService;
import com.htc.licenseapproval.utils.OTPservice;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@Slf4j
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Authentication Controller", description = "APIs for handling users login and singup")
public class AuthController {

	@Autowired
	private UserService userService;

	@Autowired
	private LogRepository logRepository;

	@Autowired
	private OTPservice otpService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserCredentialsRepository userCredentialsRepository;

	@PostMapping("/registerUser")
	@Operation(summary = "Register user", description = "To register new user")
	public ResponseEntity<String> registerUser(@RequestBody RegisterUser registerUser) {

		UserCredentials userCredentials = new UserCredentials();
		userCredentials.setUsername(registerUser.getUsername());
		userCredentials.setPassword(registerUser.getPassword());
		userCredentials.setEmail(registerUser.getEmail());
		UserCredentials user = userService.saveUser(userCredentials);
		if (user != null) {

			UserLog userLog = UserLog.builder()
					.logDetails(String.format(LogMessages.REGISTER_USER, user.getUsername()))
					.loggedTime(LocalDateTime.now())
					.build();

			logRepository.save(userLog);
			
			return ResponseEntity.ok("Registered Successfully with username " + user.getUsername());

		}
		return new ResponseEntity<String>("Not found", HttpStatus.NOT_FOUND);
	}

	
	@DeleteMapping("/deleteUser")
	@Operation(summary = "Delete user", description = "To register user by username")
	public ResponseEntity<String> deleteUser(@RequestParam String userName) {
		if (userService.deleteUser(userName) != null) {
			
			UserLog userLog = UserLog.builder()
					.logDetails(String.format(LogMessages.USER_DELETED,  userName))
					.loggedTime(LocalDateTime.now())
					.build();

			logRepository.save(userLog);
			
			return ResponseEntity.ok("Deleted successfully " + userName);
			
		}
		return new ResponseEntity<String>("Not found", HttpStatus.NOT_FOUND);
	}

	@PostMapping("/login")
	@Operation(summary = "Login user", description = "To login user by username")
	public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
		try {
			
			String username = loginRequest.getUsername();

			authenticate(username, loginRequest.getPassword());

			UserCredentials user = userService.findByUsername(username);
			if (!user.isOTPenabled()) {
				
				OTP otp = otpService.generateOTP(user);
				user.setOTPenabled(true);
				userCredentialsRepository.save(user);
			  
				
			    emailService.sendVerficationOtpEmail(user.getEmail(), otp.getOtp(),user.getUsername(),OTPtype.LOGIN);
				

				UserLog userLog = UserLog.builder()
						.logDetails(String.format(LogMessages.LOGIN_SUCCESS, username))
						.loggedTime(LocalDateTime.now())
						.build();
				
				logRepository.save(userLog);
				
				log.info("Logged successfully as username : " + username);
			    
			    return ResponseEntity.ok("OTP send to your registered mail : " +user.getEmail());
			}

			return ResponseEntity.ok("Use the previous OTP sent through the email within 2 mins");

		} catch( MessagingException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
				
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
		}
	}

	@PostMapping("/login/otpVerification")
	public ResponseEntity<String> otpVerification(@RequestParam String OTP, @RequestParam String username,
			HttpServletRequest request) {
		UserCredentials userCredentials = userService.findByUsername(username);
		if (otpService.validateOTP(OTP, username) && userCredentials.isOTPenabled()) {
			
			UserDetails userDetails = userService.loadUserByUsername(username);
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
					userDetails, null, userDetails.getAuthorities());
			authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
<<<<<<< HEAD
			request.getSession(true).setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
			request.getSession(true).setMaxInactiveInterval(30*60);
			UserLog userLog = UserLog.builder ()
					.logDetails(String.format(LogMessages.OTP_VERIFIED, userCredentials.getEmail()))
					.loggedTime(LocalDateTime.now())
					.build();

			logRepository.save(userLog);
			
			return ResponseEntity.status(HttpStatus.ACCEPTED)
					.body("OTP verified successfully \nlogged in successfully");
=======
			 request.getSession(true).setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

			return ResponseEntity.status(HttpStatus.ACCEPTED).body("OTP verified successfully \nlogged in successfully");
>>>>>>> origin/main
		}

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP");

	}

	@PostMapping("/forgotpassword")
	public ResponseEntity<String> forgotPasssword(@RequestParam String username) throws MessagingException {
		OTP otp = null;
		UserCredentials user = userService.findByUsername(username);
		if (!user.isOTPenabled()) {
			otp = otpService.generateOTP(user);
			
			UserLog userLog2 = UserLog.builder()
					.logDetails(String.format(LogMessages.FORGET_PASSWORD, user.getUsername()))
					.loggedTime(LocalDateTime.now())
					.build();

			logRepository.save(userLog2);
			emailService.sendVerficationOtpEmail(user.getEmail(), otp.getOtp(),user.getUsername(),OTPtype.FORGOT_PASSWORD);
						
			return ResponseEntity.ok("OTP successfully sent to your registered e-mail id : "+user.getEmail());
		}
		return ResponseEntity.ok("Use the previous OTP sent through the email within 2 mins");

	}

	@PostMapping("/changePassword/{OTP}")
	public ResponseEntity<String> changePassword(@RequestBody UpdatePasswordDTO loginRequest,
			@PathVariable String OTP) {
		if (otpService.validateOTP(OTP, loginRequest.getUsername())) {

			UserCredentials credentials = userService.updatePassword(loginRequest.getUsername(),
					loginRequest.getPassword());
			if (credentials != null) {
				
				UserLog userLog = UserLog.builder()
						.logDetails(String.format(LogMessages.OTP_VERIFIED, credentials.getUsername()))
						.loggedTime(LocalDateTime.now())
						.build();

				logRepository.save(userLog);
				
				return ResponseEntity.status(HttpStatus.ACCEPTED).body("Password changed successfully for username : "+loginRequest.getUsername());
			}
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Error");
		}

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP");
	}
	
	@PostMapping("/logout")
	public ResponseEntity<String> logout(HttpServletRequest request) {
	    HttpSession session = request.getSession(false);
	    if (session != null) {
	        session.invalidate(); 
	    }
	    SecurityContextHolder.clearContext(); 
	    return ResponseEntity.ok("Logged out successfully");
	}


	@PostMapping("/logout")
	public ResponseEntity<String> logout(HttpServletRequest request) {
		
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		HttpSession session = request.getSession(false);
		
		if (session != null) {
			session.invalidate();
		}
		SecurityContextHolder.clearContext();
		
		UserLog userLog = UserLog.builder()
				.logDetails(String.format(LogMessages.USER_LOGGEDOUT, username))
				.loggedTime(LocalDateTime.now())
				.build();

		logRepository.save(userLog);
		
		return ResponseEntity.ok("Logged out successfully");
	}

	private Authentication authenticate(String userName, String password) {
		return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
	}

}

