package com.htc.licenseapproval.utils;

import java.security.SecureRandom;

public class OtpGenerator {

	public static String generateOTP() {
		
	SecureRandom secureRandom = new SecureRandom();
	int randomNum = secureRandom.nextInt(900000)+100000;
	String OTP = String.valueOf(randomNum);
	return OTP;
	 
	}

}
