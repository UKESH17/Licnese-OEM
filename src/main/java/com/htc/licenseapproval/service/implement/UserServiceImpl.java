package com.htc.licenseapproval.service.implement;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.htc.licenseapproval.entity.UserCredentials;
import com.htc.licenseapproval.repository.UserCredentialsRepository;
import com.htc.licenseapproval.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserCredentialsRepository credentialsRepository;
	
	@Autowired
    private PasswordEncoder passwordEncoder;


	@Override
	public UserCredentials saveUser(UserCredentials credentials) {
		if (credentialsRepository.findById(credentials.getUsername()).isPresent()) {
            throw new RuntimeException("Username already present");
        }
		credentials.setPassword(passwordEncoder.encode(credentials.getPassword()));
		return credentialsRepository.save(credentials) ;
	} 

	@Override
	public String deleteUser(String username) {
		UserCredentials cred = credentialsRepository.findById(username).orElseThrow(()->new RuntimeException("user not found with username : "+username));
	    credentialsRepository.delete(cred);
	    return "Deleted successfully ";
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserCredentials userCred =credentialsRepository.findById(username).orElseThrow(()->new RuntimeException("user not found with username : "+username));
		return new User(userCred.getUsername(), userCred.getPassword(),authorities());
	}
	
	private Collection<? extends GrantedAuthority> authorities() {
        return Arrays.asList(new SimpleGrantedAuthority("USER"));
    }

	@Override
	public UserCredentials findByUsername(String username) {
		return credentialsRepository.findById(username).orElseThrow(()-> new RuntimeException("user not found with username - "+username));
	}

	@Override
	public UserCredentials updatePassword(String username, String password) {
		UserCredentials cred = credentialsRepository.findById(username).orElseThrow(()->new RuntimeException("user not found with username : "+username));
		System.out.println(password);
		cred.setPassword(passwordEncoder.encode(password));
		return credentialsRepository.save(cred);
	}

	@Override
	public UserCredentials updateEmail(String username, String email) {
		UserCredentials cred = credentialsRepository.findById(username).orElseThrow(()->new RuntimeException("user not found with username : "+username));
		cred.setEmail(email);
		return credentialsRepository.save(cred);
	}

}
