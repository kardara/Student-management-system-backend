package com.kardara.studentManagement.model;

import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fees")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Fees {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    @Column(name = "date")
    private LocalDate date;

    @Column(name = "amount")
    private int amount;

    /*  */

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    /*  */
    @OneToOne
    @JoinColumn(name = "registration_id")
    @JsonIgnore
    private StudentRegistration registration;

}
