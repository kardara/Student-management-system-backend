package com.kardara.studentManagement.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.kardara.studentManagement.DTO.OperationResult;
import com.kardara.studentManagement.model.OfferedCourse;
import com.kardara.studentManagement.model.Teacher;
import com.kardara.studentManagement.repository.OfferedCourseRepository;
import com.kardara.studentManagement.repository.TeacherRepository;

@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private OfferedCourseRepository offeredCourseRepository;

    @Autowired
    private SemesterService semesterService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(8);

    public OperationResult add(Teacher teacher) {
        if (teacher.getPassword() != null && !teacher.getPassword().isEmpty()) {
            teacher.setPassword(encoder.encode(teacher.getPassword()));
        }
        teacherRepository.save(teacher);
        return new OperationResult(true,
                "Teacher " + teacher.getFirstName() + " " + teacher.getLastName() + " added successfully");
    }

    public List<Teacher> get() {
        return teacherRepository.findAll();
    }

      public Teacher get(UUID id) {
        return teacherRepository.findById(id).get();
    }

    public OperationResult update(Teacher teacher) {
        Optional<Teacher> t = teacherRepository.findById(teacher.getId());

        if (t.isPresent()) {
            Teacher newTeacher = t.get();
            newTeacher.setAddress(teacher.getAddress());
            newTeacher.setEmail(teacher.getEmail());
            newTeacher.setFirstName(teacher.getFirstName());
            newTeacher.setLastName(teacher.getLastName());
            newTeacher.setPhone(teacher.getPhone());
            newTeacher.setQualification(teacher.getQualification());
            newTeacher.setRole(teacher.getRole());

            if (teacher.getPassword() != null) {
                newTeacher.setPassword(encoder.encode(teacher.getPassword()));
            }

            teacherRepository.save(newTeacher);
            return new OperationResult(true,
                    "Teacher " + teacher.getFirstName() + " " + teacher.getLastName() + " updated successfully");
        }
        return new OperationResult(false, "Teacher not found");
    }

    public OperationResult delete(Teacher teacher) {
        Optional<Teacher> t = teacherRepository.findById(teacher.getId());
        if (t.isPresent()) {
            teacherRepository.deleteById(teacher.getId());
            return new OperationResult(true,
                    "Teacher " + teacher.getFirstName() + " " + teacher.getLastName() + " deleted successfully");
        }
        return new OperationResult(false, "Teacher not found");
    }

    public List<OfferedCourse> getCourses(UUID teacherId) {
        Optional<Teacher> t = teacherRepository.findById(teacherId);

        if (t.isPresent()) {
            Teacher teacher = t.get();
            List<OfferedCourse> courses = offeredCourseRepository.findByTeacherAndSemesterWithRegistrations(teacher, semesterService.getCurrentSemester());

            for (OfferedCourse course : courses) {
                course.setTeacher(null);
                // Ensure registrations are loaded
                if (course.getRegistrations() != null) {
                    course.getRegistrations().size(); // Initialize lazy collection
                }
            }

            return courses;
        }
        return null;
    }
}
