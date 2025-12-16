package com.kardara.studentManagement.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.kardara.studentManagement.DTO.UserData;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String adminEmail;

    @Autowired
    private JavaMailSender emailSender;

    public void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    public void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        emailSender.send(message);
    }

    public boolean adminLoginMailAlert(UserData userData) {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM d, yyyy")); // More readable date format
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("h:mm a")); // 12-hour format with AM/PM
        int year = LocalDate.now().getYear();

        // Get full name and format properly
        String fullName = userData.getFirstName() + " " + userData.getLastName();

        // Get role information for more personalized message
        String roleInfo = "";
        if (userData.getRole() != null) {
            roleInfo = switch (userData.getRole()) {
                case "STUDENT" ->
                    userData.getProgram() != null ? " - " + userData.getProgram() + " Student" : " - Student";
                case "TEACHER" ->
                    userData.getQualification() != null ? " - Teacher (" + userData.getQualification() + ")"
                            : " - Teacher";
                case "ADMIN" -> " - Administrator";
                default -> "";
            };
        }

        String htmlBody = """
                <!DOCTYPE html>
                <html>
                <head>
                  <meta charset="UTF-8">
                  <meta name="viewport" content="width=device-width, initial-scale=1.0">
                  <title>Login Notification</title>
                </head>
                <body style="font-family: 'Segoe UI', Arial, sans-serif; line-height: 1.6; max-width: 600px; margin: 0 auto; padding: 20px; color: #333333;">
                  <div style="background-color: #ffffff; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); overflow: hidden;">
                    <!-- Header -->
                    <div style="background-color: #0066cc; color: white; padding: 20px; text-align: center;">
                      <h1 style="margin: 0; font-size: 24px;">Login Notification</h1>
                    </div>

                    <!-- Content -->
                    <div style="padding: 30px 20px;">
                      <p style="font-size: 16px;">
                        Hello,
                      </p>
                      <p style="font-size: 16px;">
                        We detected a new login to the Student Management System for:
                      </p>

                      <!-- User Info Box -->
                      <div style="background-color: #f9f9f9; border-left: 4px solid #0066cc; padding: 15px; margin: 20px 0;">
                        <p style="margin: 0; font-size: 18px; font-weight: bold;">%s%s</p>
                        <p style="margin: 5px 0 0; color: #666666; font-size: 14px;">ID: %s</p>
                        %s
                      </div>

                      <!-- Login Details -->
                      <div style="background-color: #f5f5f5; padding: 15px; border-radius: 5px; margin-top: 20px;">
                        <p style="margin: 0 0 10px; font-size: 16px; font-weight: bold;">Login Details:</p>
                        <table style="width: 100%%; border-collapse: collapse; font-size: 14px;">
                          <tr>
                            <td style="padding: 8px 5px; width: 80px;"><strong>Date:</strong></td>
                            <td style="padding: 8px 5px;">%s</td>
                          </tr>
                          <tr>
                            <td style="padding: 8px 5px;"><strong>Time:</strong></td>
                            <td style="padding: 8px 5px;">%s</td>
                          </tr>
                        </table>
                      </div>

                      <!-- Security Note removed because it is for admin
                      <p style="margin-top: 25px; font-size: 14px; color: #666666;">
                        If this wasn't you, please contact the system administrator immediately.
                      </p> 
                      -->
                    </div>

                    <!-- Footer -->
                    <div style="background-color: #f0f0f0; padding: 15px; text-align: center; font-size: 12px; color: #666666;">
                      <p style="margin: 0;">
                        Student Management System &copy; %d Ange Buhendwa
                      </p>
                      <p style="margin: 5px 0 0;">
                        <a href="#" style="color: #0066cc; text-decoration: none;">Privacy Policy</a> |
                        <a href="#" style="color: #0066cc; text-decoration: none;">Contact Support</a>
                      </p>
                    </div>
                  </div>
                </body>
                </html>
                """;

        // Department and faculty info if available (for students)
        String deptInfo = "";
        if (userData.getRole() != null && userData.getRole().equals("STUDENT")) {
            if (userData.getDepartment() != null && userData.getFaculty() != null) {
                deptInfo = String.format("""
                        <p style="margin: 5px 0 0; color: #666666; font-size: 14px;">%s, %s</p>
                        """, userData.getDepartment(), userData.getFaculty());
            }
        }
        // Format the HTML with all the dynamic content
        htmlBody = String.format(htmlBody,
                fullName,
                roleInfo,
                userData.getId(),
                deptInfo,
                date,
                time,
                year);

        try {
            sendHtmlEmail(adminEmail, "Login Alert - Student Management System", htmlBody);
            return true;
        } catch (Exception e) {
            // Log the exception for debugging
            return false;
        }
    }
}
