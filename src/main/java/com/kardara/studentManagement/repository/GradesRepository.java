package com.kardara.studentManagement.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kardara.studentManagement.model.Grades;
import com.kardara.studentManagement.model.OfferedCourse;
import com.kardara.studentManagement.model.Student;

import java.util.List;


public interface GradesRepository extends JpaRepository<Grades, UUID>{

    List<Grades> findByStudent(Student student);
    List<Grades> findByCourse(OfferedCourse course);
    
    
}
