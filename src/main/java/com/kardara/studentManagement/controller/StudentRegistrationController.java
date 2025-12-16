package com.kardara.studentManagement.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kardara.studentManagement.DTO.OperationResult;
import com.kardara.studentManagement.service.StudentRegistrationService;

@RestController
@RequestMapping(value = "studentregistration")
public class StudentRegistrationController {

    @Autowired
    private StudentRegistrationService studentRegistrationService;

    @PostMapping(value = "create")
    public ResponseEntity<?> create(@RequestParam int studentId, @RequestParam(required = false) UUID[] coursesId) {
        OperationResult res = studentRegistrationService.create(studentId, coursesId);
        if (res.isSuccess()) {
            return new ResponseEntity<>(res.getMessage(), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(res.getMessage(), HttpStatus.CONFLICT);
    }

    @GetMapping(value = "get")
    public ResponseEntity<?> get() {
        return new ResponseEntity<>(studentRegistrationService.getAll(), HttpStatus.OK);
    }

    @GetMapping(value = "student/get")
    public ResponseEntity<?> getbystudent(@RequestParam int studentId) {
        return new ResponseEntity<>(studentRegistrationService.getByStudent(studentId), HttpStatus.OK);
    }

    @PutMapping(value = "course/add")
    public ResponseEntity<?> addCourse(@RequestParam int studentId, @RequestParam UUID[] coursesId) {
        OperationResult res = studentRegistrationService.addCourse(studentId, coursesId);
        if (res.isSuccess()) {
            return new ResponseEntity<>(res.getMessage(), HttpStatus.OK);
        }
        return new ResponseEntity<>(res.getMessage(), HttpStatus.CONFLICT);
    }

    @DeleteMapping(value = "course/remove")
    public ResponseEntity<?> delete(@RequestParam int studentId,
            @RequestParam UUID coursesId) {
        OperationResult res = studentRegistrationService.removeCourse(studentId, coursesId);
        if (res.isSuccess()) {
            return new ResponseEntity<>(res.getMessage(), HttpStatus.OK);
        }
        return new ResponseEntity<>(res.getMessage(), HttpStatus.CONFLICT);
    }

    /* custom */

    @GetMapping(value = "/course/getstudent")
    public ResponseEntity<?> getByCourse(@RequestParam UUID courseId) {
        return new ResponseEntity<>(studentRegistrationService.getStudentByCourse(courseId), HttpStatus.OK);
    }
}
