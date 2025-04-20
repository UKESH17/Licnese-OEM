package com.htc.licenseapproval.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.htc.licenseapproval.entity.LicenseLogMessages;

@Repository
public interface LicenseLogMessagesRepository  extends JpaRepository<LicenseLogMessages, Long>{

}
