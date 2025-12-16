package com.kardara.studentManagement.DTO;

import com.kardara.studentManagement.model.Semester;
import com.kardara.studentManagement.model.Staff;
import com.kardara.studentManagement.model.Student;
import com.kardara.studentManagement.model.Teacher;

import lombok.Getter;

@Getter
public class LoginResponse {

    private String token;
    private String role;
    private boolean success;
    private String message;
    private UserData user;
    private Semester semester;

    public LoginResponse(boolean success, String message) { 
        this.success = success;
        this.message = message;
    }

    public LoginResponse(boolean success, String message, String token) { 
        this.success = success;
        this.message = message;
        this.token = token;
    }

    public LoginResponse(boolean success, String token, String role, String message, Semester semester) {
        this.token = token;
        this.role = role;
        this.success = success;
        this.message = message;
        this.semester = semester;
    }

    public LoginResponse(boolean success, String token, String role, String message, Semester semester, Student st) {
        this.token = token;
        this.role = role;
        this.success = success;
        this.message = message;
        this.semester = semester;
        this.user = new UserData(st);

    }

    public LoginResponse(boolean success, String token, String role, String message, Semester semester, Staff st) {
        this.token = token;
        this.role = role;
        this.success = success;
        this.message = message;
        this.semester = semester;
        this.user = new UserData(st);

    }

    public LoginResponse(boolean success, String token, String role, String message, Semester semester, Teacher teach) {
        this.token = token;
        this.role = role;
        this.success = success;
        this.message = message;
        this.semester = semester;
        this.user = new UserData(teach);

    }

    public LoginResponse(boolean success, String token, String role, String message, Semester semester, UserData user) {
        this.token = token;
        this.role = role;
        this.success = success;
        this.message = message;
        this.semester = semester;
        this.user = user;

    }
}
