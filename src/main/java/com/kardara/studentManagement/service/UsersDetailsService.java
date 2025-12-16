package com.kardara.studentManagement.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kardara.studentManagement.model.Staff;
import com.kardara.studentManagement.model.StaffPrincipal;
import com.kardara.studentManagement.model.Student;
import com.kardara.studentManagement.model.StudentPrincipal;
import com.kardara.studentManagement.model.Teacher;
import com.kardara.studentManagement.model.TeacherPrincipal;
import com.kardara.studentManagement.repository.StaffRepository;
import com.kardara.studentManagement.repository.StudentRepository;
import com.kardara.studentManagement.repository.TeacherRepository;

@Service
public class UsersDetailsService implements UserDetailsService {

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    public UserDetails loadUserByUsername(String email) {

        Optional<Student> student = studentRepository.findByEmail(email);
        
        if(student.isPresent()){
            System.out.println("Load student");
            return new StudentPrincipal(student.get());
        }
        
        Optional<Teacher> teacher = teacherRepository.findByEmail(email);
        
        if (teacher.isPresent()) {
            System.out.println("Load teacher");
            return new TeacherPrincipal(teacher.get());
        }
        
        Optional<Staff> staff = staffRepository.findByEmail(email);
        if (staff.isPresent()) {
            System.out.println("Load staff");
            return new StaffPrincipal(staff.get());
        }

        throw new UsernameNotFoundException("Email not found");

    }

}
