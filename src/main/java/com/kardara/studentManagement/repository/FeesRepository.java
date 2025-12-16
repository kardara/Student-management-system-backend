package com.kardara.studentManagement.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kardara.studentManagement.model.Fees;
import com.kardara.studentManagement.model.Student;
import com.kardara.studentManagement.model.StudentRegistration;

public interface FeesRepository extends JpaRepository<Fees, UUID>{

    Optional<Fees> findByStudentAndRegistration(Student student, StudentRegistration registration);
    
}
