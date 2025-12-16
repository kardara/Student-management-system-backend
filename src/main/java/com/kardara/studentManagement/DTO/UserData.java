package com.kardara.studentManagement.DTO;

import com.kardara.studentManagement.model.Staff;
import com.kardara.studentManagement.model.Student;
import com.kardara.studentManagement.model.Teacher;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserData {

    private String firstName;
    private String lastName;
    private String id;
    private String email;
    private String faculty;
    private String department;
    private String program;
    private String role;
    private String qualification;
    private String uuid;

    public UserData(Student st) {

        this.setFirstName(st.getFirstName());
        this.setLastName(st.getLastName());
        this.setId(""+ st.getId());
        this.setEmail(st.getEmail());
        if (st.getDepartment() != null) {
            this.setFaculty(st.getDepartment().getParent().getName());
            this.setDepartment(st.getDepartment().getName());
        }
        this.setProgram(st.getProgram().toString());
        this.setRole("STUDENT");
    }

    public UserData(Staff st) {

        this.setFirstName(st.getFirstName());
        this.setLastName(st.getLastName());
        this.setId(st.getEmail());
        this.setEmail(st.getEmail());

        this.setUuid(st.getId().toString());

        this.setRole(st.getRole().toString());
    }

    public UserData(Teacher st) {
        this.setFirstName(st.getFirstName());
        this.setLastName(st.getLastName());
        this.setId(st.getEmail());
        this.setEmail(st.getEmail());
        this.setQualification(st.getQualification().toString());
        this.setRole(st.getRole().toString());
        this.setUuid(st.getId().toString());
    }

}
