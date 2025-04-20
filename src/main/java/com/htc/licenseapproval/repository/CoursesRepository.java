package com.htc.licenseapproval.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.htc.licenseapproval.entity.Courses;

@Repository
public interface CoursesRepository extends JpaRepository<Courses, Long>{

}
