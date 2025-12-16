package com.kardara.studentManagement.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kardara.studentManagement.DTO.OperationResult;
import com.kardara.studentManagement.model.AcademicUnit;
import com.kardara.studentManagement.model.Course;
import com.kardara.studentManagement.repository.AcademicUnitRepository;
import com.kardara.studentManagement.repository.CourseRepository;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private AcademicUnitRepository academicUnitRepository;

    public OperationResult add(Course course, String academicUnitCode) {
        boolean exist = courseRepository.existsByCode(course.getCode());

        if (exist) {
            return new OperationResult(false, "Course code already exists");
        }

        Optional<AcademicUnit> academicUnit = academicUnitRepository.findByCode(academicUnitCode);
        if (academicUnit.isPresent()) {
            course.setAcademicUnit(academicUnit.get());
            courseRepository.save(course);
            return new OperationResult(true, "Course " + course.getName() + " saved successfully");
        } else {
            return new OperationResult(false, "Invalid academic unit code");
        }
    }

    public List<Course> get() {
        return courseRepository.findAll();
    }
    public Course get(UUID id) {
        return courseRepository.findById(id).get();
    }

    public OperationResult update(Course course, String academicUnitCode) {
        Optional<Course> c = courseRepository.findById(course.getId());
        if (c.isPresent()) {
            Course newCourse = c.get();
            newCourse.setCode(course.getCode());
            newCourse.setName(course.getName());
            newCourse.setDescription(course.getDescription());
            newCourse.setCredit(course.getCredit());

            Optional<AcademicUnit> academicUnit = academicUnitRepository.findByCode(academicUnitCode);
            if (academicUnit.isPresent()) {
                newCourse.setAcademicUnit(academicUnit.get());
            }
            courseRepository.save(newCourse);
            return new OperationResult(true, "Course " + newCourse.getName() + " updated successfully");

        }
        return new OperationResult(false, "Course not found");

    }

    public OperationResult delete(Course course) {
        Optional<Course> c = courseRepository.findById(course.getId());
        if (c.isPresent()) {
            courseRepository.deleteById(course.getId());
            return new OperationResult(true, "Course " + c.get().getName() + " deleted successfully");
        }
        return new OperationResult(false, "Course not found !");
    }

}
