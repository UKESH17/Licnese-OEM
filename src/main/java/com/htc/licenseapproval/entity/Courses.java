package com.htc.licenseapproval.entity;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor

public class Courses extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long courseId;
		
	private boolean certificateDone;
	
	@Column(name = "course_details", nullable = false)
	private String courseDetails;
	
	@Column(name = "hours_spent", nullable = true)
	private float hoursSpent = 0;
	
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER,mappedBy = "courses")
	private Set<UploadedFile> certificates;
	
	@ManyToOne
	private RequestDetails requestDetails;

}
