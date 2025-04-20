package com.htc.licenseapproval.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.htc.licenseapproval.dto.LoginRequest;
import com.htc.licenseapproval.dto.RegisterUser;
import com.htc.licenseapproval.entity.OTP;
import com.htc.licenseapproval.entity.UpdatePasswordDTO;
import com.htc.licenseapproval.entity.UserCredentials;
import com.htc.licenseapproval.repository.LogRepository;
import com.htc.licenseapproval.repository.UserCredentialsRepository;
import com.htc.licenseapproval.service.UserService;
import com.htc.licenseapproval.utils.EmailService;
import com.htc.licenseapproval.utils.OTPservice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@Slf4j
@CrossOrigin(origins = "http://localhost:5173")
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
	public ResponseEntity<String> registerUser(@RequestBody RegisterUser registerUser) {

		UserCredentials userCredentials = new UserCredentials();
		userCredentials.setUsername(registerUser.getUsername());
		userCredentials.setPassword(registerUser.getPassword());
		userCredentials.setEmail(registerUser.getEmail());
		UserCredentials user = userService.saveUser(userCredentials);
		if (user != null) {
			return ResponseEntity.ok("Registered Successfully with username " + user.getUsername());

		}
		return new ResponseEntity<String>("Not found", HttpStatus.NOT_FOUND);
	}

	@DeleteMapping("/deleteUser")
	public ResponseEntity<String> deleteUser(@RequestParam String userName) {
		if (userService.deleteUser(userName) != null) {
			return ResponseEntity.ok("Deleted successfully " + userName);
		}
		return new ResponseEntity<String>("Not found", HttpStatus.NOT_FOUND);
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
		try {

			authenticate(loginRequest.getUsername(), loginRequest.getPassword());
//			session.setAttribute("user", loginRequest.getUsername());
			log.info("Logged successfully as username : " + loginRequest.getUsername());
			UserCredentials user = userService.findByUsername(loginRequest.getUsername());
			if (!user.isOTPenabled()) {
				OTP otp = otpService.generateOTP(user);
				user.setOTPenabled(true);
				userCredentialsRepository.save(user);
				// mail Sender
				// emailService.sendVerficationOtpEmail(user.getEmail(), otp.getOtp());
				return ResponseEntity.ok("OTP send to mail " + otp.getOtp());
			}
			
			return ResponseEntity.ok("Use the previous OTP sent through the email within 2 mins");

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
		}
	}

	@PostMapping("login/otpVerification")
	public ResponseEntity<String> otpVerification(@RequestParam String OTP, @RequestParam String username,HttpServletRequest request) {
		UserCredentials userCredentials = userService.findByUsername(username);
		if (otpService.validateOTP(OTP, username) && userCredentials.isOTPenabled()) {
			UserDetails userDetails = userService.loadUserByUsername(username);
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
					userDetails, null, userDetails.getAuthorities());
			authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			 request.getSession(true).setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

			return ResponseEntity.status(HttpStatus.ACCEPTED).body("OTP verified successfully \nlogged in successfully");
		}

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP");

	}

	@PostMapping("/forgotpassword")
	public ResponseEntity<String> forgotPasssword(@RequestParam String username) {
		OTP otp = null;
		UserCredentials user = userService.findByUsername(username);
		if(!user.isOTPenabled()) {
		 otp = otpService.generateOTP(user);
		// mail Sender
		// emailService.sendVerficationOtpEmail(user.getEmail(), otp.getOtp());
		 return ResponseEntity.ok("OTP send to mail " + otp.getOtp());
		}
		return ResponseEntity.ok("Use the previous OTP sent through the email within 2 mins");
		
		

	}

	@PostMapping("/changePassword/{OTP}")
	public ResponseEntity<String> changePassword(@RequestBody UpdatePasswordDTO  loginRequest, @PathVariable String OTP) {
		if (otpService.validateOTP(OTP, loginRequest.getUsername())) {

			UserCredentials credentials = userService.updatePassword(loginRequest.getUsername(),
					loginRequest.getPassword());
			if (credentials != null) {
				return ResponseEntity.status(HttpStatus.ACCEPTED).body("password changed successfully");
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


	private Authentication authenticate(String userName, String password) {
		return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
	}

}

