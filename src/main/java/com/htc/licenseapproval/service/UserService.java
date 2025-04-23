package com.htc.licenseapproval.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.htc.licenseapproval.entity.UserCredentials;

public interface UserService extends UserDetailsService{

	public UserCredentials saveUser(UserCredentials credentials);

	public String deleteUser(String username);
	
	public UserCredentials findByUsername(String username);

	public UserCredentials updatePassword(String username, String password);
	
	public UserCredentials updateEmail(String username, String email);
	
}
