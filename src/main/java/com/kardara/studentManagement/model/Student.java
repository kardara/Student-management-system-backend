package com.kardara.studentManagement.model;

import com.kardara.studentManagement.model.enumeration.EStudentProgram;
import com.kardara.studentManagement.model.enumeration.EStudentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIgnore;


import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

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

    @Column(name = "enrollment_date")
    private LocalDate enrollmentDate;

    @Column(name = "status")
    private EStudentStatus status;

    @Column(name = "program")
    private EStudentProgram program;

    @Column(name = "gpa")
    private double gpa = 0;

    @Column(name = "password")
    private String password;


    /*  */

    @ManyToOne
    @JoinColumn(name = "department_id")
    private AcademicUnit department;

    /*  */

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<StudentRegistration> registrations;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Payment> payments;

    @OneToMany(mappedBy ="student", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Grades> grades;

    @OneToMany(mappedBy ="student", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Fees> fees;
    
    @OneToMany(mappedBy ="student", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Attendance> attendances;

   
}
