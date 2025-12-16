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

import com.kardara.studentManagement.model.AcademicUnit;
import com.kardara.studentManagement.DTO.OperationResult;
import com.kardara.studentManagement.service.AcademicUnitService;

@RestController
@RequestMapping(value = "academicunit")
public class AcademicUnitController {

    @Autowired
    private AcademicUnitService academicUnitService;

    // @PostMapping("addprogram")
    // public ResponseEntity<?> addProgram(@RequestBody AcademicUnit academicUnit) {

    //     OperationResult res = academicUnitService.addProgram(academicUnit);
    //     if (res.isSuccess()) {
    //         return new ResponseEntity<>(res.getMessage(), HttpStatus.CREATED);
    //     }
    //     return new ResponseEntity<>(res.getMessage(), HttpStatus.CONFLICT);
    // }

    @PostMapping(value = "add")
    public ResponseEntity<?> add(@RequestBody AcademicUnit academicUnit,@RequestParam(required = false) String parentCode) {

        if (parentCode == null || parentCode.isEmpty()) {
            OperationResult res = academicUnitService.add(academicUnit);
            if (res.isSuccess()) {
                return new ResponseEntity<>(res.getMessage(), HttpStatus.CREATED);
            }
            return new ResponseEntity<>(res.getMessage(), HttpStatus.CONFLICT);

        }

        OperationResult res = academicUnitService.add(academicUnit, parentCode);
        if (res.isSuccess()) {
            return new ResponseEntity<>(res.getMessage(), HttpStatus.CREATED);
        }

        return new ResponseEntity<>(res.getMessage(), HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "get")
    public ResponseEntity<?> get() {
        return new ResponseEntity<>(academicUnitService.get(), HttpStatus.OK);
    }

    @PutMapping(value = "update")
    public ResponseEntity<?> update(@RequestBody AcademicUnit academicUnit,
            @RequestParam(required = false) String parentCode) {
        OperationResult res = academicUnitService.update(academicUnit, parentCode);
        if (res.isSuccess()) {
            return new ResponseEntity<>(res.getMessage(), HttpStatus.OK);
        }

        return new ResponseEntity<>(res.getMessage(), HttpStatus.CONFLICT);
    }

    @DeleteMapping(value = "delete")
    public ResponseEntity<?> delete(@RequestBody AcademicUnit academicUnit) {
        OperationResult res = academicUnitService.delete(academicUnit);

        if (res.isSuccess()) {
            return new ResponseEntity<>(res.getMessage(), HttpStatus.OK);
        }

        return new ResponseEntity<>(res.getMessage(), HttpStatus.CONFLICT);
    }

}
