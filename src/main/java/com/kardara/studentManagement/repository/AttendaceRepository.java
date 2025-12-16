package com.kardara.studentManagement.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kardara.studentManagement.model.Attendance;
import com.kardara.studentManagement.model.OfferedCourse;
import com.kardara.studentManagement.model.Student;

public interface AttendaceRepository extends JpaRepository<Attendance, UUID> {

    List<Attendance> findByCourse(OfferedCourse course);
    List<Attendance> findByCourseAndStudent(OfferedCourse course, Student student);
    List<Attendance> findByCourseAndDate(OfferedCourse course, LocalDate date);
    
}
