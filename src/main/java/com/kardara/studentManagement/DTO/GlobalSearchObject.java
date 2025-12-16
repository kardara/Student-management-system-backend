package com.kardara.studentManagement.DTO;

import java.util.List;

import com.kardara.studentManagement.model.AcademicUnit;
import com.kardara.studentManagement.model.Attendance;
import com.kardara.studentManagement.model.Course;
import com.kardara.studentManagement.model.Fees;
import com.kardara.studentManagement.model.OfferedCourse;
import com.kardara.studentManagement.model.Payment;
import com.kardara.studentManagement.model.Semester;
import com.kardara.studentManagement.model.Staff;
import com.kardara.studentManagement.model.Student;
import com.kardara.studentManagement.model.StudentRegistration;
import com.kardara.studentManagement.model.Teacher;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GlobalSearchObject {

    private List<Student> students;
    private List<Teacher> teachers;
    private List<Staff> staffs;

    private List<OfferedCourse> offeredCourses;
    private List<Course> courses;

    private List<AcademicUnit> academicUnits;
    private List<Semester> semesters;

    private List<Fees> fees;
    private List<Attendance> attendances;
    private List<Payment> payments;

    private List<StudentRegistration> registrations;

    //// Students
    public GlobalSearchObject(List<StudentRegistration> registrations, List<Course> courses,
            List<Fees> fees, List<Payment> payments) {
        this.registrations = registrations;
        this.courses = courses;
        // this.grades = grades;
        this.fees = fees;
        this.payments = payments;
    }

    /// Teachers
    public GlobalSearchObject(List<OfferedCourse> offeredCourses,
            List<Attendance> attendances,
            List<AcademicUnit> academicUnits) {
        this.offeredCourses = offeredCourses;
        this.academicUnits = academicUnits;
        this.attendances = attendances;
    }

}
