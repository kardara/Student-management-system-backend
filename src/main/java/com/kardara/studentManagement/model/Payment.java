package com.kardara.studentManagement.model;

import java.time.LocalDate;
import java.util.UUID;

import com.kardara.studentManagement.model.enumeration.EPaymentMode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "payment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "payment_mode")
    @Enumerated(EnumType.STRING)
    private EPaymentMode paymentMode;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "amount")
    private int amount;  
    
    /*  */

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
}
