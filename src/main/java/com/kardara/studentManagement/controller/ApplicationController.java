package com.kardara.studentManagement.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kardara.studentManagement.DTO.OperationResult;
import com.kardara.studentManagement.controller.other.GenericController;
import com.kardara.studentManagement.model.Application;
import com.kardara.studentManagement.service.ApplicationService;

@RestController
@RequestMapping(value = "application")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @GetMapping(value = "get")
    public ResponseEntity<?> get() {
        try {
            return new ResponseEntity<>(applicationService.get(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }

    @PostMapping(value = "apply")
    public ResponseEntity<?> apply(@RequestBody Application application, @RequestParam String code) {
        OperationResult res = applicationService.apply(application, code);
        return GenericController.getResponse(res, HttpStatus.CREATED, HttpStatus.CONFLICT);
    }

    @PostMapping(value = "approve")
    public ResponseEntity<?> approve(@RequestBody UUID id) {
        return GenericController.getResponse(applicationService.approve(id), HttpStatus.ACCEPTED, HttpStatus.CONFLICT);
    }

    @PostMapping(value = "reject")
    public ResponseEntity<?> reject(@RequestBody UUID id) {
        return GenericController.getResponse(applicationService.reject(id), HttpStatus.ACCEPTED, HttpStatus.CONFLICT);
    }
}
