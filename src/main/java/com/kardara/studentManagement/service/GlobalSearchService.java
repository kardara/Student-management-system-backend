package com.kardara.studentManagement.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kardara.studentManagement.DTO.GlobalSearchObject;
import com.kardara.studentManagement.model.Student;
import com.kardara.studentManagement.model.Teacher;
import com.kardara.studentManagement.repository.AcademicUnitRepository;
import com.kardara.studentManagement.repository.AttendaceRepository;
import com.kardara.studentManagement.repository.CourseRepository;
import com.kardara.studentManagement.repository.FeesRepository;
import com.kardara.studentManagement.repository.GradesRepository;
import com.kardara.studentManagement.repository.OfferedCourseRepository;
import com.kardara.studentManagement.repository.PaymentRepository;
import com.kardara.studentManagement.repository.SemesterRepository;
import com.kardara.studentManagement.repository.StaffRepository;
import com.kardara.studentManagement.repository.StudentRegistrationRepository;
import com.kardara.studentManagement.repository.StudentRepository;
import com.kardara.studentManagement.repository.TeacherRepository;

@Service
public class GlobalSearchService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private OfferedCourseRepository offeredCourseRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private AcademicUnitRepository academicUnitRepository;

    @Autowired
    private FeesRepository feesRepository;

    @Autowired
    private AttendaceRepository attendaceRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private StudentRegistrationRepository studentRegistrationRepository;

    public GlobalSearchObject adminSearch() {
        GlobalSearchObject globalSearchObject = new GlobalSearchObject();
        globalSearchObject.setStudents(studentRepository.findAll());
        globalSearchObject.setTeachers(teacherRepository.findAll());
        globalSearchObject.setStaffs(staffRepository.findAll());

        globalSearchObject.setOfferedCourses(offeredCourseRepository.findAll());
        globalSearchObject.setCourses(courseRepository.findAll());

        globalSearchObject.setAcademicUnits(academicUnitRepository.findAll());
        globalSearchObject.setSemesters(semesterRepository.findAll());
        globalSearchObject.setFees(feesRepository.findAll());
        globalSearchObject.setAttendances(attendaceRepository.findAll());
        globalSearchObject.setPayments(paymentRepository.findAll());
        globalSearchObject.setRegistrations(studentRegistrationRepository.findAll());
        return globalSearchObject;
    }

    public GlobalSearchObject studentSearch(String id) {
        Optional<Student> st = studentRepository.findById(Integer.parseInt(id));
        if (st.isEmpty())
            return null;

        return new GlobalSearchObject(studentRegistrationRepository.findByStudent(st.get()), courseRepository.findAll(),
                feesRepository.findAll(), paymentRepository.findAll());

    }

    public GlobalSearchObject teacherSearch(UUID id) {
        Optional<Teacher> st = teacherRepository.findById(id);
        if (st.isEmpty())  return null;

        GlobalSearchObject globalSearchObject = new GlobalSearchObject();
        globalSearchObject.setStudents(studentRepository.findAll());
        globalSearchObject.setTeachers(teacherRepository.findAll());
        globalSearchObject.setStaffs(staffRepository.findAll());

        globalSearchObject.setOfferedCourses(offeredCourseRepository.findAll());
        globalSearchObject.setCourses(courseRepository.findAll());

        globalSearchObject.setAcademicUnits(academicUnitRepository.findAll());
        globalSearchObject.setSemesters(semesterRepository.findAll());
        globalSearchObject.setFees(feesRepository.findAll());
        globalSearchObject.setAttendances(attendaceRepository.findAll());
        globalSearchObject.setPayments(paymentRepository.findAll());
        globalSearchObject.setRegistrations(studentRegistrationRepository.findAll());
        return globalSearchObject;

    }

}
