package com.kardara.studentManagement.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kardara.studentManagement.DTO.OperationResult;
import com.kardara.studentManagement.model.OfferedCourse;
import com.kardara.studentManagement.model.Semester;
import com.kardara.studentManagement.model.Student;
import com.kardara.studentManagement.model.StudentRegistration;
import com.kardara.studentManagement.repository.OfferedCourseRepository;
import com.kardara.studentManagement.repository.StudentRegistrationRepository;
import com.kardara.studentManagement.repository.StudentRepository;

import jakarta.transaction.Transactional;

@Service
public class StudentRegistrationService {

    @Autowired
    private StudentRegistrationRepository studentRegistrationRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private OfferedCourseRepository offeredCourseRepository;

    @Autowired
    private FeesService feesService;

    @Autowired
    private SemesterService semesterService;

    @Transactional
    public OperationResult create(int studentId, UUID[] coursesId) {
        Optional<Student> st = studentRepository.findById(studentId);
        if (st.isPresent()) {
            Student student = st.get();
            Semester semester = semesterService.getCurrentSemester();
            Optional<StudentRegistration> exist = studentRegistrationRepository.findByStudentAndSemester(student,
                    semester);
            if (exist.isPresent()) {
                return new OperationResult(false, "Cannot create two registrations for the same student and semester");
            }

            Set<OfferedCourse> courses = new HashSet<>();
            if (coursesId != null) {
                for (UUID uuid : coursesId) {
                    Optional<OfferedCourse> c = offeredCourseRepository.findById(uuid);
                    if (c.isPresent()) {
                        courses.add(c.get());
                    }
                }
            }
            StudentRegistration studentRegistration = new StudentRegistration();
            studentRegistration.setStudent(student);
            studentRegistration.setSemester(semester);
            studentRegistration.setDate(LocalDate.now());
            if (courses.size() != 0) {
                studentRegistration.setCourses(new ArrayList<>(courses));
                // double check
                if (studentRegistrationRepository.findByStudentAndSemester(student, semester).isPresent()) {
                    return new OperationResult(false,
                            "Cannot create two registrations for the same student and semester");
                }
                /////
                studentRegistrationRepository.save(studentRegistration);
                boolean res = feesService.create(student, studentRegistration);
                if (res) {
                    return new OperationResult(true, "Registration created successfully");

                } else {
                    return new OperationResult(true, "Registration created successfully, creating fees failed");
                }

            } else {

                // double check
                if (studentRegistrationRepository.findByStudentAndSemester(student, semester).isPresent()) {
                    return new OperationResult(false,
                            "Cannot create two registrations for the same student and semester");
                }
                /////
                studentRegistrationRepository.save(studentRegistration);
                boolean res = feesService.create(student, studentRegistration);
                if (res) {
                    return new OperationResult(true, "Registration created successfully, no course added");

                } else {
                    return new OperationResult(true,
                            "Registration created successfully, with 0 course. creating fees failed");
                }
            }

        }
        return new OperationResult(false, "Student not found");
    }

    public List<StudentRegistration> getAll() {
        return studentRegistrationRepository.findAll();
    }

    public StudentRegistration getByStudent(int studentId) {

        Optional<Student> st = studentRepository.findById(studentId);
        if (st.isPresent()) {
            Student student = st.get();

            Semester semester = semesterService.getCurrentSemester();
            Optional<StudentRegistration> stRg = studentRegistrationRepository.findByStudentAndSemester(student,
                    semester);
            if (stRg.isPresent()) {
                return stRg.get();
            }

        }
        return null;

    }

    public OperationResult addCourse(int studentId, UUID[] course) {
        Optional<Student> st = studentRepository.findById(studentId);
        if (st.isPresent()) {
            Student student = st.get();

            Semester semester = semesterService.getCurrentSemester();
            Optional<StudentRegistration> stRg = studentRegistrationRepository.findByStudentAndSemester(student,
                    semester);
            if (stRg.isPresent()) {
                StudentRegistration studentRegistration = stRg.get();
                int courseNumber = studentRegistration.getCourses().size();
                Set<OfferedCourse> courseSet = new HashSet<>(studentRegistration.getCourses());

                for (UUID uuid : course) {
                    Optional<OfferedCourse> c = offeredCourseRepository.findById(uuid);
                    if (c.isPresent()) {
                        courseSet.add(c.get());
                    }
                }
                if (courseNumber != courseSet.size()) {
                    studentRegistration.setCourses(new ArrayList<>(courseSet));
                    studentRegistrationRepository.save(studentRegistration);
                    boolean res = feesService.update(student, studentRegistration);
                    if (res) {
                        return new OperationResult(true, "Courses updated successfully");

                    } else {
                        return new OperationResult(true, "Courses updated successfully, fees not updated");
                    }
                } else {

                    return new OperationResult(false, "Course not added, they were not found");
                }

            } else {
                return new OperationResult(false, "Student registration not found");
            }

        }
        return new OperationResult(false, "Student not found");
    }

    public OperationResult removeCourse(int studentId, UUID courseId) {

        Optional<Student> st = studentRepository.findById(studentId);
        if (st.isPresent()) {
            Student student = st.get();

            Semester semester = semesterService.getCurrentSemester();
            Optional<StudentRegistration> stRg = studentRegistrationRepository.findByStudentAndSemester(student,
                    semester);
            if (stRg.isPresent()) {
                StudentRegistration studentRegistration = stRg.get();
                // int courseNumber = studentRegistration.getCourses().size();
                Set<OfferedCourse> courseSet = new HashSet<>(studentRegistration.getCourses());

                Optional<OfferedCourse> c = offeredCourseRepository.findById(courseId);
                if (c.isPresent()) {
                    courseSet.remove(c.get());

                    studentRegistration.setCourses(new ArrayList<>(courseSet));
                    studentRegistrationRepository.save(studentRegistration);
                    boolean res = feesService.update(student, studentRegistration);
                    if (res) {
                        return new OperationResult(true, "Courses removed successfully");

                    } else {

                        return new OperationResult(true, "Courses removed successfully, fees not updated");
                    }
                } else {

                    return new OperationResult(false, "Course not deleted, they were not found");
                }
            } else {
                return new OperationResult(false, "Student registration not found");
            }

        }
        return new OperationResult(false, "Student not found");
    }

    /* Custom */

    public List<Student> getStudentByCourse(UUID courseId) {
        Optional<OfferedCourse> course = offeredCourseRepository.findById(courseId);

        if (course.isPresent()) {
            List<OfferedCourse> c = new ArrayList<OfferedCourse>();
            c.add(course.get());
            List<StudentRegistration> stReg = studentRegistrationRepository.findByCourses(c);
            List<Student> students = new ArrayList<Student>();
            for (StudentRegistration studentRegistration : stReg) {

                Student student = studentRegistration.getStudent();
                student.setDepartment(null);
                student.setPassword(null);

                students.add(student);
            }

            return students;

        } else {
            return null;
        }

    }
}
