package com.kardara.studentManagement.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.kardara.studentManagement.DTO.OperationResult;
import com.kardara.studentManagement.model.Staff;
import com.kardara.studentManagement.repository.StaffRepository;

@Service
public class StaffService {

    @Autowired
    private StaffRepository staffRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(8);

    public OperationResult add(Staff staff) {
        boolean exist = staffRepository.existsByFirstNameAndLastNameAndEmail(staff.getFirstName(), staff.getLastName(),
                staff.getEmail());

        boolean existMail = staffRepository.existsByEmail(staff.getEmail());
        if (existMail) {
            return new OperationResult(false, "Mail already exists");
        }

        if (exist) {
            return new OperationResult(false, "Cannot save Staff with same names and Email");

        } else {
            staff.setPassword(encoder.encode(staff.getPassword()));
            staffRepository.save(staff);
            return new OperationResult(true, "Staff " + staff.getFirstName() + " " + staff.getLastName()
                    + " added successfully as " + staff.getRole());
        }

    }

    public List<Staff> get() {
        return staffRepository.findAll();
    }

    public OperationResult update(Staff staff) {
        Optional<Staff> st = staffRepository.findById(staff.getId());

        if (st.isPresent()) {
            staff.setPassword(encoder.encode(staff.getPassword()));
            staffRepository.save(staff);
            return new OperationResult(true, "Staff updated successfully");
        }
        return new OperationResult(false, "Staff not found");

    }

    public OperationResult delete(String email) {
        Optional<Staff> st = staffRepository.findByEmail(email);
        if (st.isPresent()) {
            Staff staff = st.get();
            staffRepository.delete(staff);
            return new OperationResult(true, "Staff " + staff.getFirstName() + " " + staff.getLastName()
                    + " with role " + staff.getRole() + " deleted successfully");
        }
        return new OperationResult(false, "Staff with mail " + email + " not found");
    }

}
