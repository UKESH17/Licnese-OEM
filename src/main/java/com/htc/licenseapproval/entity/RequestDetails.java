package com.htc.licenseapproval.entity;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.htc.licenseapproval.entity.idgenerator.RequestId;
import com.htc.licenseapproval.enums.Status;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class RequestDetails extends BaseEntity{

	@Id
	@RequestId
	private String requestId;
	
	@Column(name = "employee_id")
	private Long empid;

	@Column(name = "employee_name", nullable = false)
	private String empname;

	@Column(name = "email_id", nullable = false)
	private String emailid;

	@Column(name = "request_status")
	@Enumerated(EnumType.STRING)
	private Status status = Status.PENDING;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "requestDetails")
	private Set<Courses> courses;

	private String approvalGivenBy;

	@OneToOne(cascade = CascadeType.ALL)
	private LicenseDetails licenseDetails;

	@ManyToOne
	@JsonIgnore
	private RequestHeader requestHeader;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "requestDetails")
	private Set<LicenseLogMessages> licenseLogMessages;

}
