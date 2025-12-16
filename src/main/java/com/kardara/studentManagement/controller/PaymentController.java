package com.kardara.studentManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kardara.studentManagement.DTO.OperationResult;
import com.kardara.studentManagement.controller.other.GenericController;
import com.kardara.studentManagement.model.Payment;
import com.kardara.studentManagement.service.PaymentService;

@RestController
@RequestMapping(value = "payment/")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping(value = "add")
    public ResponseEntity<?> add(@RequestBody Payment payment, @RequestParam int studentId) {
        OperationResult res = paymentService.add(payment, studentId);
        return GenericController.getResponse(res, HttpStatus.OK, HttpStatus.CONFLICT);
    }

    @GetMapping(value = "get")
    public ResponseEntity<?> get(){
        return new ResponseEntity<>(paymentService.get(), HttpStatus.OK);
    }

    @DeleteMapping(value = "delete")
    public ResponseEntity<?> delete(@RequestBody Payment payment) {
        OperationResult res = paymentService.delete(payment);
        return GenericController.getResponse(res, HttpStatus.OK, HttpStatus.NOT_FOUND);
    }
}
