package com.kardara.studentManagement.model;

import java.time.LocalDate;
import java.util.UUID;

import com.kardara.studentManagement.model.enumeration.EApplicationStatus;
import com.kardara.studentManagement.model.enumeration.EStudentProgram;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "applications")
@Getter
@Setter
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "application_date")
    private LocalDate applicationDate;

    @Column(name = "program")
    private EStudentProgram program;

    @Column(name = "application_status")
    private EApplicationStatus status;

    /*  */

    @ManyToOne
    @JoinColumn(name = "department_id")
    private AcademicUnit department;


    

}
