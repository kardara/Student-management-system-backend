package com.kardara.studentManagement.controller;

import java.util.UUID;

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
import com.kardara.studentManagement.model.OfferedCourse;
import com.kardara.studentManagement.service.OfferedCourseService;

@RestController
@RequestMapping(value = "offeredcourse")
public class OfferedCourseController {


    @Autowired
    private OfferedCourseService offeredCourseService;


    @PostMapping(value = "add")
    public ResponseEntity<?> add(@RequestBody OfferedCourse offeredCourse, @RequestParam String courseCode,
             @RequestParam UUID teacherId) {

        OperationResult res = offeredCourseService.add(offeredCourse, courseCode, teacherId);
        if (res.isSuccess()) {
            return new ResponseEntity<>(res.getMessage(), HttpStatus.CREATED);

        } else {
            return new ResponseEntity<>(res.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping(value = "get")
    public ResponseEntity<?> get(@RequestParam(required = false) UUID id) {

        if (id != null) {
        return new ResponseEntity<>(offeredCourseService.get(id), HttpStatus.OK);

        }

        return new ResponseEntity<>(offeredCourseService.get(), HttpStatus.OK);
    }

    @PutMapping(value = "update")
    public ResponseEntity<?> update(@RequestBody OfferedCourse offeredCourse, @RequestParam UUID courseId,
            @RequestParam UUID semesterId, @RequestParam UUID teacherId) {

        OperationResult res = offeredCourseService.update(offeredCourse, courseId, semesterId, teacherId);
        if (res.isSuccess()) {
            return new ResponseEntity<>(res.getMessage(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(res.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @DeleteMapping(value = "delete")
    public ResponseEntity<?> delete(@RequestBody OfferedCourse offeredCourse) {

        OperationResult res = offeredCourseService.delete(offeredCourse);
        if (res.isSuccess()) {
            return new ResponseEntity<>(res.getMessage(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(res.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    

}
