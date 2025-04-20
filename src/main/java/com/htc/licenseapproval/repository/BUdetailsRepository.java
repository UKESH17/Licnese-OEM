package com.htc.licenseapproval.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.htc.licenseapproval.entity.BUdetails;

public interface BUdetailsRepository extends JpaRepository<BUdetails, Long> {
	
	public Optional<BUdetails> findByBu(String name);

}
