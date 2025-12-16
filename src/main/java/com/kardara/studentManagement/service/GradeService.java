package com.kardara.studentManagement.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kardara.studentManagement.DTO.GradeFormat;
import com.kardara.studentManagement.DTO.OperationResult;
import com.kardara.studentManagement.model.Grades;
import com.kardara.studentManagement.model.OfferedCourse;
import com.kardara.studentManagement.model.Student;
import com.kardara.studentManagement.model.enumeration.EGradeStatus;

import com.kardara.studentManagement.repository.GradesRepository;
import com.kardara.studentManagement.repository.OfferedCourseRepository;
import com.kardara.studentManagement.repository.StudentRepository;

@Service
public class GradeService {

    @Autowired
    private GradesRepository gradesRepository;

    @Autowired
    private OfferedCourseRepository offeredCourseRepository;

    @Autowired
    private StudentRepository studentRepository;

    public OperationResult addCourse(List<GradeFormat> gradesList, UUID courseId) {
        Optional<OfferedCourse> oc = offeredCourseRepository.findById(courseId);
        if (oc.isPresent()) {
            OfferedCourse offeredCourse = oc.get();
            List<Grades> grades = new ArrayList<Grades>();
            for (GradeFormat a : gradesList) {
                if (a.getGrade() > 20 || a.getGrade() < 0) {
                    continue;
                }
                Optional<Student> st = studentRepository.findById(a.getId());
                if (st.isEmpty()) {
                    continue;
                }
                Student student = st.get();
                Grades grade = new Grades();
                grade.setStudent(student);
                grade.setCourse(offeredCourse);
                grade.setGrade(a.getGrade());

                EGradeStatus status = EGradeStatus.FAILED;
                if (a.getGrade() >= 10) {
                    status = EGradeStatus.PASSED;
                }
                grade.setStatus(status);

                grades.add(grade);
            }
            gradesRepository.saveAll(grades);
            updateGPA(gradesList);
            if (gradesList.size() != grades.size()) {
                int b = gradesList.size() - grades.size();
                return new OperationResult(true, "Grade saved but " + b + " grades(s) have not been savedd");
            }
            return new OperationResult(true, "Grade saved");

        } else {
            return new OperationResult(false, "Course not found");

        }
    }

    public List<Grades> get() {
        return gradesRepository.findAll();
    }

    public List<Grades> getStudent(int id) {
        Optional<Student> st = studentRepository.findById(id);
        if (st.isPresent()) {
            Student student = st.get();

            List<Grades> grades = gradesRepository.findByStudent(student);

            for (Grades grade : grades) {
                grade.setStudent(null);
            }

            return grades;
        }
        return null;
    }

    public List<Grades> getByCourses(UUID courseId){

        Optional<OfferedCourse> course = offeredCourseRepository.findById(courseId);
        if(course.isPresent()){
            return gradesRepository.findByCourse(course.get());
        }
        return null;
    }

    private void updateGPA(List<GradeFormat> list) {
        for (GradeFormat gr : list) {
            Optional<Student> st = studentRepository.findById(gr.getId());

            if (st.isPresent()) {
                Student student = st.get();
                List<Grades> grades = gradesRepository.findByStudent(student);
                int credits = 0;
                double tot = 0;

                for (Grades grade : grades) {
                    int c = (int) grade.getCourse().getCourse().getCredit();
                    credits += c;

                    double g = grade.getGrade();
                    tot += g * c;
                }

                double gpa = tot / credits;
                student.setGpa(gpa);
                studentRepository.save(student);
            }
        }
    }

}
