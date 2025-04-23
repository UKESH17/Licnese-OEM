package com.htc.licenseapproval.entity;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class BUdetails{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long buId;
	
	@Column(name = "Business_Unit", nullable = false,unique = true)
	private String bu;
	
	@Column(name = "BU_Head", nullable = false)
	private String buHead;
	
	private String buDeliveryHead;
	
	@OneToMany(mappedBy = "buDetails")
	@JsonIgnore
	private Set<RequestHeader> requests;
}
