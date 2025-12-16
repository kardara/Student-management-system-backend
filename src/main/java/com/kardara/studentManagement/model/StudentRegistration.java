package com.kardara.studentManagement.model;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "student_registration")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StudentRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "date")
    private LocalDate date;


    /*  */
    @OneToOne(mappedBy = "registration")
    private Fees fees;

    /*  */

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "semester_id")
    private Semester semester;

    /*  */

    @ManyToMany
    private List<OfferedCourse> courses;

    /*  */

    // @Column(name = "department_id")
    // private AcademicUnit department;

}
