package com.htc.licenseapproval.entity;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserCredentials extends BaseEntity implements UserDetails {

	private static final long serialVersionUID = 1L;
	
	@Id
	private String username;
	
	@Column(nullable = false)
	private String password;
	
	@Column(unique = true,nullable = false)
	
	private String email;
	
	private boolean isOTPenabled=false;
	
	@Transient
	private Collection<? extends GrantedAuthority> authorities;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

}
