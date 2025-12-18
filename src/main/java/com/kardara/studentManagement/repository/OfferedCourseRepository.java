package com.kardara.studentManagement.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kardara.studentManagement.model.Course;
import com.kardara.studentManagement.model.OfferedCourse;
import com.kardara.studentManagement.model.Semester;
import com.kardara.studentManagement.model.Teacher;

import java.util.List;


public interface OfferedCourseRepository extends JpaRepository<OfferedCourse, UUID> {

    boolean existsBySemesterAndCourseAndGroup(Semester semester, Course course, char group);

    List<OfferedCourse> findByTeacher(Teacher teacher);
    List <OfferedCourse> findByTeacherAndSemester(Teacher teacher,Semester semester);

    @Query("SELECT oc FROM OfferedCourse oc LEFT JOIN FETCH oc.registrations WHERE oc.teacher = :teacher AND oc.semester = :semester")
    List<OfferedCourse> findByTeacherAndSemesterWithRegistrations(@Param("teacher") Teacher teacher, @Param("semester") Semester semester);


}
