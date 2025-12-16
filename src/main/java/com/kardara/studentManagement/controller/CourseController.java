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
import com.kardara.studentManagement.model.Course;
import com.kardara.studentManagement.service.CourseService;

@RestController
@RequestMapping(value = "course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping(value = "add")
    public ResponseEntity<?> add(@RequestBody Course course, @RequestParam String academicUnitCode){
        OperationResult res = courseService.add(course, academicUnitCode);

        if (res.isSuccess()) {
            return new ResponseEntity<>(res.getMessage(), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(res.getMessage(), HttpStatus.CONFLICT);
    }

    @GetMapping(value = "get")
    public ResponseEntity<?> get (@RequestParam(required = false) UUID id){
        if (id != null) {
            return new ResponseEntity<>(courseService.get(id), HttpStatus.OK);
        }
        return new ResponseEntity<>(courseService.get(), HttpStatus.OK);
    }

    @PutMapping(value = "update")
    public ResponseEntity<?> update(@RequestBody Course course, @RequestParam(required = false) String academicUnitCode){
        OperationResult res = courseService.update(course, academicUnitCode);
        if (res.isSuccess()) {
            return new ResponseEntity<>(res.getMessage(), HttpStatus.OK);
        }
        return new ResponseEntity<>(res.getMessage(), HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "delete")
    public ResponseEntity<?> delete(@RequestBody Course course){
        OperationResult res = courseService.delete(course);
        if(res.isSuccess()){
            return new ResponseEntity<>(res.getMessage(), HttpStatus.OK);
        }
        return new ResponseEntity<>(res.getMessage(), HttpStatus.NOT_FOUND);
    }
    
}
