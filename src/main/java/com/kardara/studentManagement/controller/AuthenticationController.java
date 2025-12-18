package com.kardara.studentManagement.controller;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kardara.studentManagement.DTO.LoginRequest;
import com.kardara.studentManagement.DTO.LoginResponse;
import com.kardara.studentManagement.service.AuthenticationService;
import com.kardara.studentManagement.service.JWTUtilities;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private JWTUtilities jwtUtilities;

    @Value("${front.end.ip}")
    private String frontEndIp;

    @PostMapping(value = "login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        LoginResponse res = authenticationService.login(loginRequest);
        if (res.isSuccess()) {
            return new ResponseEntity<>(res, HttpStatusCode.valueOf(200));
        } else {
            return new ResponseEntity<>(res, HttpStatusCode.valueOf(401));
        }
    }

    @PostMapping(value = "otpvalidation")
    public ResponseEntity<?> validateOtp(@RequestParam UUID otpID, String otp) {

        LoginResponse res = authenticationService.validateOTP(otpID, otp);
        if (res.isSuccess()) {
            return new ResponseEntity<>(res, HttpStatusCode.valueOf(200));
        } else {
            return new ResponseEntity<>(res, HttpStatusCode.valueOf(406));
        }
    }

    @PostMapping(value = "resetpassword")
    public ResponseEntity<?> resetPassword(@RequestBody LoginRequest loginRequest) {
        LoginResponse res = authenticationService.resetPasswordRequest(loginRequest);
        if (res.isSuccess()) {
            return new ResponseEntity<>(res, HttpStatusCode.valueOf(200));
        } else {
            return new ResponseEntity<>(res, HttpStatusCode.valueOf(406));
        }
    }


    @PostMapping(value = "resetpassword/otpvalidation")
    public ResponseEntity<?> changePassword(@RequestParam String otp, @RequestParam UUID otpID, @RequestBody String password) {
        LoginResponse res = authenticationService.validatePasswordResetOTP(otpID, otp, password);
        if (res.isSuccess()) {
            return new ResponseEntity<>(res, HttpStatusCode.valueOf(200));
        } else {
            return new ResponseEntity<>(res, HttpStatusCode.valueOf(406));
        }
    }

    @GetMapping(value = "oauth2/success")
    public void oauth2LoginSuccess(HttpServletResponse response, HttpServletRequest request,
            Authentication authentication) throws IOException {
        try {
            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();

            // Extract username (email or login)
            String username = oauth2User.getAttribute("email") != null
                    ? oauth2User.getAttribute("email")
                    : oauth2User.getAttribute("login");

            if (username == null) {
                response.sendRedirect(frontEndIp + "/auth/oauth2/failed");
                return;
            }

            // Check if user exists
            LoginResponse userData = authenticationService.getUserData(username);
            if (!userData.isSuccess()) {
                response.sendRedirect(frontEndIp + "/auth/oauth2/failed");
                return;
            }

            String token = jwtUtilities.generateToken(username);

            // Set the token in the Authorization header
            response.addCookie(new Cookie("token", token) {
                {
                    setSecure(true); // Only send over HTTPS
                    setPath("/"); // Cookie available to the entire domain
                }
            });
            response.sendRedirect(frontEndIp + "/auth/oauth2/success");
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            response.sendRedirect(frontEndIp + "/auth/oauth2/failed");
        }
    }

    @GetMapping("me")
    public ResponseEntity<?> getMe(Authentication auth) {
        if (auth == null) {
            return ResponseEntity.status(401).build();
        }
        String email = auth.getName();
        LoginResponse res = authenticationService.getUserData(email);
        if (res.isSuccess()) {
            return ResponseEntity.ok(res);
        } else {
            return ResponseEntity.status(404).body(res);
        }
    }

}
