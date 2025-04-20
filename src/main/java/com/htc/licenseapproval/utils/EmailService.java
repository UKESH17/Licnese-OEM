package com.htc.licenseapproval.utils;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

	private JavaMailSender javaMailSender;

	public void sendVerficationOtpEmail(String email, String otp) throws MessagingException {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();

		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

		String subject = "Verify OTP";
		String text = "Your verification OTP is " + otp;

		mimeMessageHelper.setSubject(subject);
		mimeMessageHelper.setText(text);
		mimeMessageHelper.setTo(email);

		try {
			javaMailSender.send(mimeMessage);
		} catch (MailException e) {
			throw new MailSendException(e.getMessage());
		}

	}
}
