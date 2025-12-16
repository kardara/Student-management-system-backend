package com.kardara.studentManagement.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kardara.studentManagement.model.Staff;


public interface StaffRepository extends JpaRepository<Staff, UUID> {

    boolean existsByFirstNameAndLastNameAndEmail(String firstName, String lastName, String email);
    boolean existsByEmail(String email);
    Optional<Staff> findByEmail(String email);    
}
