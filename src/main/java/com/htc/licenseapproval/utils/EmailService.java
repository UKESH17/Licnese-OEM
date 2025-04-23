package com.htc.licenseapproval.utils;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.htc.licenseapproval.constants.LogMessages;
import com.htc.licenseapproval.entity.UserLog;
import com.htc.licenseapproval.enums.OTPtype;
import com.htc.licenseapproval.repository.LogRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService {

	@Autowired
	private JavaMailSender javaMailSender;
	
	@Autowired
	private LogRepository logRepository;
	
	@Value("${spring.mail.username}")
	private String fromMail;

	public void sendVerficationOtpEmail(String email, String otp,String username, OTPtype otpType) throws MessagingException {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();

		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
		
		
		String subject = otpType.equals(OTPtype.LOGIN) ? "Verify OTP for Login " :"Verify OTP to change the password";
		String title =  otpType.equals(OTPtype.LOGIN) ? "login " : "change password";
		String HTMLcontent = "<!DOCTYPE html>\r\n"
		        + "<html lang=\"en\">\r\n"
		        + "<head>\r\n"
		        + "    <meta charset=\"UTF-8\">\r\n"
		        + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n"
		        + "    <title>[TITLE]</title>\r\n"
		        + "</head>\r\n"
		        + "<body style=\"font-family: Arial, sans-serif; margin: 0; padding: 20px; background-color: #f4f4f4;\">\r\n"
		        + "    <div style=\"background-color: #ffffff; padding: 30px; border-radius: 5px; box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);\">\r\n"
		        + "        <h2 style=\"color: #333;\">OTP Verification</h2>\r\n"
		        + "        <p style=\"color: #555; line-height: 1.6;\">Hello [Username],</p>\r\n"
		        + "        <p style=\"color: #555; line-height: 1.6;\">Please use the following One-Time Password (OTP) to [TITLE]:</p>\r\n"
		        + "        <div style=\"background-color: #e0f7fa; color: #00897b; font-size: 24px; font-weight: bold; padding: 15px 30px; text-align: center; border-radius: 5px; max-width: 400px;\">\r\n" // Modified styles here
		        + "            [OTP_CODE]\r\n"
		        + "        </div>\r\n"
		        + "        <p style=\"color: #777; font-size: 12px; line-height: 1.4;\">This OTP is valid only for 2 minutes. Please do not share it with anyone.</p>\r\n"
		        + "        <p style=\"color: #555; line-height: 1.6;\">Thank you,<br>Training Departmant<br>License approval App</p><im>\r\n"
		        + "    </div>\r\n"
		        + "</body>\r\n"
		        + "</html>";

		HTMLcontent = HTMLcontent.replace("[OTP_CODE]", otp);
		HTMLcontent = HTMLcontent.replace("[TITLE]", title);
        HTMLcontent = HTMLcontent.replace("[Username]", username);
        
		mimeMessageHelper.setFrom(fromMail);
		mimeMessageHelper.setSubject(subject);
		mimeMessageHelper.setText(HTMLcontent,true);
		mimeMessageHelper.setTo(email);

		try {
			
			javaMailSender.send(mimeMessage);
			UserLog userLog2 = UserLog.builder()
					.logDetails(String.format(LogMessages.OTP_SENT, email))
					.loggedTime(LocalDateTime.now())
					.build();

			logRepository.save(userLog2);
			
			log.info("mail sent to email");
			
		} catch (MailException e) { 
			throw new MailSendException(e.getMessage());
		}

	}
}
