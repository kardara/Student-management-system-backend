package com.kardara.studentManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kardara.studentManagement.DTO.OperationResult;
import com.kardara.studentManagement.model.Semester;
import com.kardara.studentManagement.service.SemesterService;

@RestController
@RequestMapping(value = "semester")
public class SemesterController {

    @Autowired
    private SemesterService semesterService;

    @PostMapping(value = "add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> add (@RequestBody Semester semester){
        OperationResult res =  semesterService.add(semester);

        if (res.isSuccess()) {
            return new ResponseEntity<>(res.getMessage(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(res.getMessage(), HttpStatus.UNAUTHORIZED);

        }  
    }

    @GetMapping(value = "get")
    public ResponseEntity<?> get(){
        return new ResponseEntity<>(semesterService.get(), HttpStatus.OK);
    }

    @PutMapping(value = "update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update (@RequestBody Semester semester){
        OperationResult res = semesterService.update(semester);

        if (res.isSuccess()) {
            return new ResponseEntity<>(res.getMessage(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(res.getMessage(), HttpStatus.NOT_FOUND);  
        }
    }

    @DeleteMapping(value = "delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete (@RequestBody Semester semester){

        OperationResult res = semesterService.delete(semester);

        if (res.isSuccess()) {
            return new ResponseEntity<>(res.getMessage(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(res.getMessage(), HttpStatus.NOT_FOUND);  
        }
    }
    
}
