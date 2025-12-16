package com.kardara.studentManagement.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.kardara.studentManagement.DTO.OperationResult;
import com.kardara.studentManagement.model.AcademicUnit;
import com.kardara.studentManagement.model.Student;
import com.kardara.studentManagement.model.enumeration.EAcademicUnitType;
import com.kardara.studentManagement.repository.AcademicUnitRepository;
import com.kardara.studentManagement.repository.StudentRepository;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AcademicUnitRepository academicUnitRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(8);

    public OperationResult add(Student student, String code) {


        Optional<AcademicUnit> department = academicUnitRepository.findByCode(code);

        if (department.isPresent()) {
            if (department.get().getType().equals(EAcademicUnitType.DEPARTMENT)) {
                // student.setPassword(encoder.encode(student.getPassword()));
                Optional<Student> exist = studentRepository.findByEmail(student.getEmail());
                if (exist.isPresent()) {
                    return new OperationResult(false, "Student with email " + student.getEmail()+ " already exist");

                }
                student.setDepartment(department.get());
                student.setEnrollmentDate(LocalDate.now());

                studentRepository.save(student);
                return new OperationResult(true, "Student successfully saved");

            } else {
                return new OperationResult(false, "Academic unit given is not a department");
            }
        } else {
            return new OperationResult(false, "Department Not found");
        }
    }

    public List<Student> get() {
        return studentRepository.findAll();
    }

    public Student get(int id) {

        Optional<Student> st = studentRepository.findById(id);
        if (st.isPresent()) {
            return st.get();
        }
        return null;
    }

    public OperationResult update(Student student, String departmentCode) {
        Optional<Student> st = studentRepository.findById(student.getId());
        if (st.isPresent()) {
            Optional<AcademicUnit> department = academicUnitRepository.findByCode(departmentCode);
            if (department.isPresent()) {
                student.setDepartment(department.get());
            }else{
                return new OperationResult(false, "Departement not found");
            }
            if (student.getPassword() != null) {
                student.setPassword(encoder.encode(student.getPassword()));
            }else{student.setPassword(st.get().getPassword());          }


            studentRepository.save(student);
            return new OperationResult(true, "Student updated successfully");

        } else {
            return new OperationResult(false, "Student not found!");
        }
    }

    public OperationResult delete(Student student) {
        Optional<Student> st = studentRepository.findById(student.getId());
        if (st.isPresent()) {
            studentRepository.deleteById(student.getId());
            return new OperationResult(true, "Student deleted successfully");
        } else {
            return new OperationResult(false, "Student not found!");
        }
    }

    /* Customs queries */

    public List<Student> getByDepartment(String departementCode) {
        Optional<AcademicUnit> unit = academicUnitRepository.findByCode(departementCode);
        if (unit.isPresent()) {
            AcademicUnit department = unit.get();
            if (department.getType().name().equals("DEPARTMENT")) {
                List<Student> students = department.getStudents();
                for (Student student : students) {
                    student.setDepartment(null);
                    student.setPassword(null);
                }

                return students;
            }
            return null;

        } else {
            return null;
        }

    }

    public List<Student> getByFaculty(String departementCode) {
        Optional<AcademicUnit> unit = academicUnitRepository.findByCode(departementCode);
        if (unit.isPresent()) {
            AcademicUnit fac = unit.get();
            if (fac.getType().name().equals("FACULTY")) {
                List<AcademicUnit> departments = academicUnitRepository.findByParent(fac);

                List<Student> students = new ArrayList<Student>();

                for (AcademicUnit department : departments) {
                    List<Student> temps = department.getStudents();

                    for (Student student : temps) {
                        student.setDepartment(null);
                        student.setPassword(null);
                    }

                    students.addAll(temps);
                }
                return students;
            }
            return null;

        } else {
            return null;
        }
    }

}
