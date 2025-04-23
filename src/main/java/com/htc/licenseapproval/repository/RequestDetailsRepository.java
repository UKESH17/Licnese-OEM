package com.htc.licenseapproval.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.htc.licenseapproval.entity.RequestDetails;

@Repository
public interface RequestDetailsRepository extends JpaRepository<RequestDetails, String> {

	public List<RequestDetails> findAllByEmpid(Long employeeId);
	
}
