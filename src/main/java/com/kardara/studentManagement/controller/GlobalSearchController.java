package com.kardara.studentManagement.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kardara.studentManagement.service.GlobalSearchService;

@RestController
@RequestMapping(value = "globalSearch")
public class GlobalSearchController {

    @Autowired
    private GlobalSearchService globalSearchService;

    @GetMapping(value = "admin")
    public ResponseEntity<?> adminSearch() {
        return new ResponseEntity<>(globalSearchService.adminSearch(), HttpStatus.OK);
    }

    @GetMapping(value = "student")
    public ResponseEntity<?> studentSearch(@RequestParam String id) {

        return new ResponseEntity<>(globalSearchService.studentSearch(id), HttpStatus.OK);
    }

     @GetMapping(value = "teacher")
    public ResponseEntity<?> teacherSearch(@RequestParam UUID id) {

        return new ResponseEntity<>(globalSearchService.teacherSearch(id), HttpStatus.OK);
    }
}
