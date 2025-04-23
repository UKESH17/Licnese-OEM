package com.htc.licenseapproval.utils;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.htc.licenseapproval.entity.OTP;
import com.htc.licenseapproval.entity.UserCredentials;
import com.htc.licenseapproval.repository.OTPrepository;
import com.htc.licenseapproval.repository.UserCredentialsRepository;
import com.htc.licenseapproval.service.UserService;

@Service
public class OTPservice {

	@Autowired
	private UserService userService;
	
	@Autowired
	private OTPrepository otpRepository;
	
	@Autowired
	private UserCredentialsRepository userCredentialsRepository;
	

    private int otpExpiryMinutes = 2;

    public OTP generateOTP(UserCredentials user) {
    	System.out.println(user.isOTPenabled());
    	if(!user.isOTPenabled()) {
    	OTP otp = new OTP(); 
        otp.setOtp(OtpGenerator.generateOTP());
        otp.setExpiryAt(LocalDateTime.now().plusMinutes(otpExpiryMinutes));
        otp.setUser(user);
		return otpRepository.save(otp);
    	}
    	throw new RuntimeException("OTP already sent");
                
    }
    
    public boolean validateOTP(String Otp, String username) {
    	
    	OTP savedOtp = otpRepository.findOtpByUserUsername(username);
    	if(savedOtp==null) {
    		throw new RuntimeException("Incorrect OTP or Expired OTP");
    	}
    	if(savedOtp.getExpiryAt().isBefore(LocalDateTime.now())){
    		UserCredentials user =userService.findByUsername(username);
    		user.setOTPenabled(false);
    		userCredentialsRepository.save(user);
    		removeOtp(savedOtp.getId());
    		throw new RuntimeException("OTP expired");
    	}
    	if(!savedOtp.getOtp().equals(Otp)) {
    		throw new RuntimeException("OTP does not matches");
    	}
    	return true;
    }
    
    public void removeOtp(Long id) {
    	otpRepository.deleteById(id);
    }
    
    public void removeOtp(String username) {
    	OTP otp =otpRepository.findOtpByUserUsername(username);
    	UserCredentials user =otp.getUser();
		user.setOTPenabled(false);   	
		userCredentialsRepository.save(user);
    	otpRepository.delete(otp);
    }
    
//    @Scheduled(fixedDelay = 1000)
//    private void updateOTP() {
//    	List<OTP> otps  =otpRepository.findAll();
//    	
//    	if(otps!= null) {
//    	for(OTP otp : otps) {
//    		if(otp.getExpiryAt().isBefore(LocalDateTime.now())) {
//    			UserCredentials user =otp.getUser();
//        		user.setOTPenabled(false);   	
//        		userCredentialsRepository.save(user);
//        		removeOtp(otp.getId());
//        		
//    		}
//    	}
//    	}}
    
    
    
    
    
    

}