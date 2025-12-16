package com.kardara.studentManagement.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kardara.studentManagement.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    
}
