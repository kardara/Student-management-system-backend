package com.kardara.studentManagement.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kardara.studentManagement.DTO.LoginRequest;
import com.kardara.studentManagement.DTO.LoginResponse;

@SpringBootTest
public class AuthenticationServiceTest {

    @Autowired
    private AuthenticationService authenticationService;

    @Test
    public void testLoginDefaultAdmin() throws Exception {
        LoginRequest loginRequest = new LoginRequest();

        // Use reflection to set private fields
        java.lang.reflect.Field usernameField = LoginRequest.class.getDeclaredField("username");
        usernameField.setAccessible(true);
        usernameField.set(loginRequest, "azdjerou@gmail.com");

        java.lang.reflect.Field passwordField = LoginRequest.class.getDeclaredField("password");
        passwordField.setAccessible(true);
        passwordField.set(loginRequest, "Admin123");

        java.lang.reflect.Field loginAsField = LoginRequest.class.getDeclaredField("loginAs");
        loginAsField.setAccessible(true);
        loginAsField.set(loginRequest, "ADMIN");

        LoginResponse response = authenticationService.login(loginRequest);

        assertTrue(response.isSuccess());
        assertNotNull(response.getToken());
        assertEquals("ADMIN", response.getRole());
    }
}