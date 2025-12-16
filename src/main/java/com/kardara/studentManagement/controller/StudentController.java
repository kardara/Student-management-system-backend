package com.kardara.studentManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kardara.studentManagement.DTO.OperationResult;
import com.kardara.studentManagement.model.Student;
import com.kardara.studentManagement.service.StudentService;

@RestController
@RequestMapping(value = "student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping(value = "add")
    public ResponseEntity<?> add(@RequestBody Student student, @RequestParam String department) {
        OperationResult res = studentService.add(student, department);
        if (res.isSuccess()) {
            return new ResponseEntity<>(res.getMessage(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(res.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping(value = "get")
    public ResponseEntity<?> get(@RequestParam(required = false) Integer id) {
        if (id != null) {
            return new ResponseEntity<>(studentService.get(id), HttpStatus.OK);
        }
        return new ResponseEntity<>(studentService.get(), HttpStatus.OK);
    }

    @PutMapping(value = "update")
    public ResponseEntity<?> update(@RequestBody Student student, @RequestParam String department) {
        OperationResult res = studentService.update(student, department);

        if (res.isSuccess()) {
            return new ResponseEntity<>(res.getMessage(), HttpStatus.OK);

        } else {
            return new ResponseEntity<>(res.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping(value = "delete")
    public ResponseEntity<?> delete(@RequestBody Student student) {
        OperationResult res = studentService.delete(student);
        if (res.isSuccess()) {
            return new ResponseEntity<>(res.getMessage(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(res.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /* Specail queries */
    @GetMapping(value = "getbydepartment")
    public ResponseEntity<?> getByDepartment(@RequestParam String departmentCode) {
        return new ResponseEntity<>(studentService.getByDepartment(departmentCode), HttpStatus.OK);
    }

    @GetMapping(value = "getbyfaculty")
    public ResponseEntity<?> getByFaculty(@RequestParam String facultyCode) {
        return new ResponseEntity<>(studentService.getByFaculty(facultyCode), HttpStatus.OK);
    }

}
