package com.kardara.studentManagement.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kardara.studentManagement.model.Course;

import java.util.Optional;


public interface CourseRepository extends JpaRepository<Course, UUID>{

    boolean existsByCode(String code);
    Optional<Course> findByCode(String code);
    
}
