package com.kardara.studentManagement.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.kardara.studentManagement.DTO.LoginRequest;
import com.kardara.studentManagement.DTO.LoginResponse;
import com.kardara.studentManagement.DTO.UserData;
import com.kardara.studentManagement.model.Staff;
import com.kardara.studentManagement.model.Student;
import com.kardara.studentManagement.model.Teacher;
import com.kardara.studentManagement.model.enumeration.ETeacherRole;
import com.kardara.studentManagement.repository.StaffRepository;
import com.kardara.studentManagement.repository.StudentRepository;
import com.kardara.studentManagement.repository.TeacherRepository;
import com.kardara.studentManagement.service.OtpService.OtpValidationResponse;
import com.kardara.studentManagement.service.OtpService.OtpValidationResult;

@Service
public class AuthenticationService {

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private SemesterService semesterService;

    @Autowired
    private OtpService otpService;

    // @Autowired
    // private EmailService emailService;

    @Autowired
    private JWTUtilities jwtUtilities;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(8);

    public LoginResponse login(LoginRequest req) {

        if (req.getLoginAs().equals("1")) {

            Optional<Teacher> t = teacherRepository.findByEmail(req.getUsername());
            if (t.isPresent()) {
                Teacher teacher = t.get();
                if (encoder.matches(req.getPassword(), teacher.getPassword())) {
                    // String token = jwtUtilities.generateToken(teacher.getEmail());

                    // email test
                    // emailService.adminLoginMailAlert(new UserData(teacher));
                    ///
                    UUID token = UUID.randomUUID();
                    otpService.generateAndSendOtp(token, String.valueOf(teacher.getEmail()), "TEACHER",
                            new UserData(teacher));

                    LoginResponse resp = new LoginResponse(true, token.toString(), teacher.getRole().toString(),
                            "Use the OTP sent to your email, to login" + teacher.getRole().toString(),
                            semesterService.getCurrentSemester());
                    // resp.setUserInfo(teacher);
                    return resp;
                } else {
                    return new LoginResponse(false, "Incorect credentials");
                }
            }

        } else if (req.getLoginAs().equals("2")) {

            /* STaff */

            Optional<Staff> st = staffRepository.findByEmail(req.getUsername());
            if (st.isPresent()) {
                Staff staff = st.get();
                if (encoder.matches(req.getPassword(), staff.getPassword())) {
                    // String token = jwtUtilities.generateToken(staff.getEmail());

                    // email test
                    // emailService.adminLoginMailAlert(new UserData(staff));
                    ///
                    ///
                    UUID token = UUID.randomUUID();
                    otpService.generateAndSendOtp(token, String.valueOf(staff.getEmail()), "STAFF",
                            new UserData(staff));

                    return new LoginResponse(true, token.toString(), staff.getRole().toString(),
                            "Use the OTP sent to your email, to login",
                            semesterService.getCurrentSemester());
                } else {
                    return new LoginResponse(false, "Incorect credentials");
                }

            }

        } else {

            if (isInteger(req.getUsername())) {
                Optional<Student> st = studentRepository.findById(Integer.parseInt(req.getUsername()));
                if (st.isPresent()) {
                    Student student = st.get();
                    if (encoder.matches(req.getPassword(), student.getPassword())) {
                        // String token = jwtUtilities.generateToken(student.getEmail());
                        // emailService.adminLoginMailAlert(new UserData(student));
                        UUID token = UUID.randomUUID();
                        otpService.generateAndSendOtp(token, String.valueOf(student.getId()), "STUDENT",
                                new UserData(student));
                        return new LoginResponse(true, token.toString(), "STUDENT",
                                "Use the OTP sent to your email, to login",
                                semesterService.getCurrentSemester());
                    } else {
                        return new LoginResponse(false, "Incorect credentials");
                    }

                }
            } else {
                Optional<Student> st = studentRepository.findByEmail(req.getUsername());
                if (st.isPresent()) {
                    Student student = st.get();
                    if (encoder.matches(req.getPassword(), student.getPassword())) {
                        // String token = jwtUtilities.generateToken(student.getEmail());

                        // email test
                        // emailService.adminLoginMailAlert(new UserData(student));
                        ///

                        UUID token = UUID.randomUUID();
                        otpService.generateAndSendOtp(token, String.valueOf(student.getId()), "STUDENT",
                                new UserData(student));

                        LoginResponse resp = new LoginResponse(true, token.toString(), "STUDENT",
                                "Use the OTP sent to your email, to login", semesterService.getCurrentSemester());
                        // resp.setUserInfo(student);

                        return resp;
                    } else {
                        return new LoginResponse(false, "Incorect credentials");
                    }
                }
            }
        }
        return new LoginResponse(false, "Incorect credentials");

    }

    public LoginResponse validateOTP(UUID otpId, String otp) {
        OtpValidationResponse<UserData> response = otpService.validateOtp(otpId, otp);
        if (response.getResult().equals(OtpValidationResult.SUCCESS)) {

            String token = jwtUtilities.generateToken(response.getData().getEmail());
            return new LoginResponse(true, token,
                    response.getUserRole(), "Successfully logged in as " + response.getData().getRole(),
                    semesterService.getCurrentSemester(), response.getData());
        }
        return new LoginResponse(false, response.getResult().toString());
    }

    public LoginResponse resetPasswordRequest(LoginRequest req) {
        if (req.getLoginAs().equals("1")) {
            Optional<Teacher> t = teacherRepository.findByEmail(req.getUsername());
            if (t.isPresent()) {
                Teacher teacher = t.get();

                UUID token = UUID.randomUUID();
                otpService.generateAndSendPasswordResetOtp(token, String.valueOf(teacher.getEmail()), "TEACHER",
                        new UserData(teacher));

                return new LoginResponse(true, "Check your email to reset password", token.toString());

            }
        } else if (req.getLoginAs().equals("2")) {

            /* STaff */
            Optional<Staff> st = staffRepository.findByEmail(req.getUsername());
            if (st.isPresent()) {
                Staff staff = st.get();

                UUID token = UUID.randomUUID();
                otpService.generateAndSendPasswordResetOtp(token, String.valueOf(staff.getEmail()), "STAFF",
                        new UserData(staff));
                return new LoginResponse(true, "Check the OTP in your email to reset password", token.toString());

            }

        } else {

            if (isInteger(req.getUsername())) {
                Optional<Student> st = studentRepository.findById(Integer.parseInt(req.getUsername()));
                if (st.isPresent()) {
                    Student student = st.get();
                    UUID token = UUID.randomUUID();
                    otpService.generateAndSendPasswordResetOtp(token, String.valueOf(student.getId()), "STUDENT",
                            new UserData(student));
                    return new LoginResponse(true, "Check the OTP in your email to reset password", token.toString());

                }
            } else {
                Optional<Student> st = studentRepository.findByEmail(req.getUsername());
                if (st.isPresent()) {
                    Student student = st.get();
                    UUID token = UUID.randomUUID();
                    otpService.generateAndSendPasswordResetOtp(token, String.valueOf(student.getId()), "STUDENT",
                            new UserData(student));

                    return new LoginResponse(true, "Check the OTP in your email to reset password", token.toString());

                }
            }
        }
        return new LoginResponse(false, "username not found");
    }

    public LoginResponse validatePasswordResetOTP(UUID otpId, String otp, String newPassword) {
        OtpValidationResponse<UserData> response = otpService.validateOtp(otpId, otp);
        if (response.getResult().equals(OtpValidationResult.SUCCESS)) {
            UserData data = response.getData();

            if (data.getRole().equals("STUDENT")) {
                Optional<Student> student = studentRepository.findById(Integer.parseInt(data.getId()));
                if (student.isPresent()) {
                    student.get().setPassword(encoder.encode(newPassword));
                    studentRepository.save(student.get());
                    return new LoginResponse(true, "Password reset successfully");
                }
            } else if (data.getRole().equals(ETeacherRole.ASSISTANT.toString()) || data.getRole().equals(ETeacherRole.LECTURER.toString())) {
                Optional<Teacher> teacher = teacherRepository.findByEmail(data.getEmail());
                if (teacher.isPresent()) {
                    teacher.get().setPassword(encoder.encode(newPassword));
                    teacherRepository.save(teacher.get());
                    return new LoginResponse(true, "Password reset successfully");
                }
            } else {
                Optional<Staff> staff = staffRepository.findByEmail(data.getEmail());
                if (staff.isPresent()) {
                    staff.get().setPassword(encoder.encode(newPassword));
                    staffRepository.save(staff.get());
                    return new LoginResponse(true, "Password reset successfully");
                }
                return new LoginResponse(false, "Failed to reset password");
            }
        }
        return new LoginResponse(false, "OTP validation failed, it may have expired");
    }

    private boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
