package com.htc.licenseapproval.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.htc.licenseapproval.entity.UserCredentials;

@Repository 
public interface UserCredentialsRepository extends JpaRepository<UserCredentials, String>{

	public Optional<UserCredentials> findByUsername(String username);

}
