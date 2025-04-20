package com.htc.licenseapproval.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.htc.licenseapproval.entity.LicenseDetails;

@Repository
public interface LicenseDetailsRepository extends JpaRepository<LicenseDetails, Long>{

}
