package com.kardara.studentManagement.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.kardara.studentManagement.model.Staff;
import com.kardara.studentManagement.model.Student;
import com.kardara.studentManagement.model.Teacher;
import com.kardara.studentManagement.repository.StaffRepository;
import com.kardara.studentManagement.repository.StudentRepository;
import com.kardara.studentManagement.repository.TeacherRepository;

@Service
public class UsersDetailsOauth2Services extends DefaultOAuth2UserService {

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest req) {

        OAuth2User oAuth2User = super.loadUser(req);

        // Oauth provider, should add that in the model, maybe later
        // String oAuthProvider = req.getClientRegistration().getClientId();

        String email = oAuth2User.getAttribute("email");

        Optional<Student> student = studentRepository.findByEmail(email);
        if (student.isPresent()) {
            return oAuth2User;
        }

        Optional<Teacher> teacher = teacherRepository.findByEmail(email);
        if (teacher.isPresent()) {
            return oAuth2User;

        }

        Optional<Staff> staff = staffRepository.findByEmail(email);
        if (staff.isPresent()) {
            return oAuth2User;
        }

        throw new OAuth2AuthenticationException("User not found");

    }

}
