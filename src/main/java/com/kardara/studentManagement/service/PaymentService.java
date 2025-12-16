package com.kardara.studentManagement.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kardara.studentManagement.DTO.OperationResult;
import com.kardara.studentManagement.model.Payment;
import com.kardara.studentManagement.model.Student;
import com.kardara.studentManagement.repository.PaymentRepository;
import com.kardara.studentManagement.repository.StudentRepository;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private StudentRepository studentRepository;
    
    public OperationResult add (Payment payment, int studentId){
        Optional<Student> st = studentRepository.findById(studentId);
        if (st.isPresent()) {
            payment.setStudent(st.get());
            payment.setDate(LocalDate.now());
            paymentRepository.save(payment);

            return new OperationResult(true, "Payment saved successfully");
        } else {
            return new OperationResult(false, "Student not found, couldn't save the payment");
            
        }
    }

    public List<Payment> get (){
        return paymentRepository.findAll();
    }

    public OperationResult delete (Payment p  ){
        Optional<Payment> py = paymentRepository.findById(p.getId());
        if (py.isPresent()) {
            Payment payment = py.get();
            paymentRepository.delete(payment);

            return new OperationResult(true, "Payment deleted successfully");
        } else {
            return new OperationResult(false, "payment not found, couldn't delete it");
            
        }
    }


}
