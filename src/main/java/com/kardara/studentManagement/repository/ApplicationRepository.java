package com.kardara.studentManagement.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kardara.studentManagement.model.Application;

public interface ApplicationRepository extends JpaRepository<Application, UUID> {

    
}
