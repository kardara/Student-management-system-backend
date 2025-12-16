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
import org.springframework.web.bind.annotation.RestController;

import com.kardara.studentManagement.DTO.OperationResult;
import com.kardara.studentManagement.controller.other.GenericController;
import com.kardara.studentManagement.model.Staff;
import com.kardara.studentManagement.service.StaffService;

@RestController
@RequestMapping(value = "staff")
public class StaffController {

    @Autowired
    private StaffService staffService;

    @PostMapping(value = "add")
    public ResponseEntity<?> add(@RequestBody Staff staff) {
        OperationResult res = staffService.add(staff);
        return GenericController.getResponse(res, HttpStatus.CREATED, HttpStatus.CONFLICT);
    }

    @GetMapping(value = "get")
    public ResponseEntity<?> get() {
        return new ResponseEntity<>(staffService.get(), HttpStatus.OK);
    }

    @PutMapping(value = "update")
    public ResponseEntity<?> update (@RequestBody Staff staff){
        OperationResult res = staffService.update(staff);
        return GenericController.getResponse(res, HttpStatus.OK, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "delete")
    public ResponseEntity<?> delete (@RequestBody String email){
        OperationResult res = staffService.delete(email);
        return GenericController.getResponse(res, HttpStatus.OK, HttpStatus.NOT_FOUND);
    }
}
