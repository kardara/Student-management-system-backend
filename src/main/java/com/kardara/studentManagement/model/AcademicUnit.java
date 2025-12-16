package com.kardara.studentManagement.model;

import java.util.List;
import java.util.UUID;

import com.kardara.studentManagement.model.enumeration.EAcademicUnitType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "academic_unit")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AcademicUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private EAcademicUnitType type;


    /*  */

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private AcademicUnit parent;


    /*  */

    @OneToMany(mappedBy = "department")
    @JsonIgnore
    private List<Student> students;

    @OneToMany(mappedBy = "academicUnit")
    // @JsonManagedReference
    @JsonIgnore
    private List<Course> courses;


}
