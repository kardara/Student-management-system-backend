package com.kardara.studentManagement.model;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.kardara.studentManagement.model.enumeration.ESemesterName;
import com.kardara.studentManagement.model.enumeration.ESemesterStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "semester")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Semester {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "year")
    private int year;

    @Column(name = "name")
    private ESemesterName name;

    @Column(name = "startDate")
    private LocalDate startDate;

    @Column(name = "endDate")
    private LocalDate endDate;

    @Column(name = "status")
    private ESemesterStatus status;

    /*  */

    @OneToMany(mappedBy = "semester", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<StudentRegistration> registrations;

    @OneToMany(mappedBy = "semester", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<OfferedCourse> courses;

}
