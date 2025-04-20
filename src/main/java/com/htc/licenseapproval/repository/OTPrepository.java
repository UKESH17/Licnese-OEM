package com.htc.licenseapproval.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.htc.licenseapproval.entity.OTP;

@Repository
public interface OTPrepository extends JpaRepository<OTP, Long>{

	public OTP findOtpByUserUsername(String username);
}
