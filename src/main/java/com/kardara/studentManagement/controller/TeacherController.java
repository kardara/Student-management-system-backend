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
import com.kardara.studentManagement.model.Teacher;
import com.kardara.studentManagement.service.TeacherService;

@RestController
@RequestMapping(value = "teacher")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @PostMapping(value = "add")
    public ResponseEntity<?> add (@RequestBody Teacher teacher){
        OperationResult res = teacherService.add(teacher);
        if(res.isSuccess()){
            return new ResponseEntity<>(res.getMessage(), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(res.getMessage(), HttpStatus.CONFLICT);
    }

    @GetMapping(value = "get")
    public ResponseEntity<?> get (@RequestParam(required = false) UUID id){
        if (id != null) {
            return new ResponseEntity<>(teacherService.get(id), HttpStatus.OK);
        }

        return new ResponseEntity<>(teacherService.get(), HttpStatus.OK);
    }

    @PutMapping(value = "update")
    public ResponseEntity<?> update(@RequestBody Teacher teacher){
        OperationResult res = teacherService.update(teacher);
        if (res.isSuccess()) {
            return new ResponseEntity<>(res.getMessage(), HttpStatus.OK);
        }
        return new ResponseEntity<>(res.getMessage(), HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "delete")
    public ResponseEntity<?> delete(@RequestBody Teacher teacher){
        OperationResult res = teacherService.delete(teacher);
        if (res.isSuccess()) {

            return new ResponseEntity<>(res.getMessage(), HttpStatus.OK);
        }
        return new ResponseEntity<>(res.getMessage(), HttpStatus.NOT_FOUND);
    }

    /*  */

    @GetMapping(value = "/getcourses")
        public ResponseEntity<?> getByCourse(@RequestParam UUID teacher){
            return new ResponseEntity<>(teacherService.getCourses(teacher), HttpStatus.OK);
        }
}
