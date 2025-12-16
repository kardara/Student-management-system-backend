package com.kardara.studentManagement.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kardara.studentManagement.DTO.OperationResult;
import com.kardara.studentManagement.model.Course;
import com.kardara.studentManagement.model.OfferedCourse;
import com.kardara.studentManagement.model.Semester;
import com.kardara.studentManagement.model.Teacher;
import com.kardara.studentManagement.model.enumeration.ESemesterStatus;
import com.kardara.studentManagement.repository.CourseRepository;
import com.kardara.studentManagement.repository.OfferedCourseRepository;
import com.kardara.studentManagement.repository.SemesterRepository;
import com.kardara.studentManagement.repository.TeacherRepository;

@Service
public class OfferedCourseService {

    @Autowired
    private OfferedCourseRepository offeredCourseRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    public OperationResult add(OfferedCourse offeredCourse, String courseCode, UUID teacherId) {
        Optional<Course> course = courseRepository.findByCode(courseCode);
        if (course.isPresent()) {

            Optional<Semester> semester = semesterRepository
                    .findFirstByStatusOrderByStartDateDesc(ESemesterStatus.ACTIVE);
            if (semester.isEmpty()) {
                semester = semesterRepository.findFirstByOrderByStartDateDesc();
            }
            if (semester.isPresent()) {

                boolean exists = offeredCourseRepository.existsBySemesterAndCourseAndGroup(semester.get(), course.get(),
                        offeredCourse.getGroup());
                if (!exists) {
                    Optional<Teacher> teacher = teacherRepository.findById(teacherId);
                    if (teacher.isPresent()) {
                        offeredCourse.setCourse(course.get());
                        offeredCourse.setSemester(semester.get());
                        offeredCourse.setTeacher(teacher.get());

                        offeredCourseRepository.save(offeredCourse);

                        return new OperationResult(true, "Offered course save successfully");

                    } else {
                        return new OperationResult(false, "Teacher not found");

                    }

                } else {
                    return new OperationResult(false,
                            "Cannot offer the same course with the same group twice, in the same semester.");
                }
            } else {

                return new OperationResult(false, "Semester not found");
            }

        } else {
            return new OperationResult(false, "Course not found");
        }
    }

    public List<OfferedCourse> get() {
        return offeredCourseRepository.findAll();
    }

     public OfferedCourse get(UUID id) {
        return offeredCourseRepository.findById(id).get();
    }

    public OperationResult update(OfferedCourse offeredCourse, UUID courseId, UUID semesterId, UUID teacherId) {

        Optional<OfferedCourse> old = offeredCourseRepository.findById(offeredCourse.getId());
        if (old.isPresent()) {

            Optional<Course> course = courseRepository.findById(courseId);
            if (course.isPresent()) {

                Optional<Semester> semester = semesterRepository.findById(semesterId);
                if (semester.isPresent()) {

                    boolean exists = offeredCourseRepository.existsBySemesterAndCourseAndGroup(semester.get(),
                            course.get(),
                            offeredCourse.getGroup());
                    if (!exists) {
                        Optional<Teacher> teacher = teacherRepository.findById(teacherId);
                        if (teacher.isPresent()) {
                            OfferedCourse newCourse = old.get();
                            newCourse.setCourse(course.get());
                            newCourse.setSemester(semester.get());
                            newCourse.setTeacher(teacher.get());
                            newCourse.setGroup(offeredCourse.getGroup());
                            newCourse.setDay(offeredCourse.getDay());
                            newCourse.setTime(offeredCourse.getTime());
                            newCourse.setRoom(offeredCourse.getRoom());
                            newCourse.setSize(offeredCourse.getSize());

                            offeredCourseRepository.save(newCourse);

                            return new OperationResult(true, "Offered course updated successfully");

                        } else {
                            return new OperationResult(false, "Teacher not found");

                        }

                    } else {
                        return new OperationResult(false,
                                "Cannot offer the same course with the same group twice, in the same semester.");
                    }
                } else {

                    return new OperationResult(false, "Semester not found");
                }

            } else {
                return new OperationResult(false, "Course not found");
            }

        } else {
            return new OperationResult(false, "Course not found !");
        }
    }

    public OperationResult delete(OfferedCourse offeredCourse) {
        Optional<OfferedCourse> course = offeredCourseRepository.findById(offeredCourse.getId());
        if (course.isPresent()) {
            offeredCourseRepository.deleteById(course.get().getId());
            return new OperationResult(true, "Course deleted successfully");
        } else {
            return new OperationResult(false, "Course not found");
        }
    }



}
