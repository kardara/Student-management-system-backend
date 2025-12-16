package com.kardara.studentManagement.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kardara.studentManagement.DTO.OtpData;
import com.kardara.studentManagement.DTO.UserData;

@Service
public class OtpService {

  @Value("${spring.mail.username}")
  private String adminEmail;

  @Value("${front.end.ip}")
  private String frontEndIp;

  // In-memory storage for OTPs with user identifiers as keys
  // In production, consider using Redis or another distributed cache
  private final Map<UUID, OtpData> otpStorage = new ConcurrentHashMap<>();

  // Configurable parameters
  private static final int OTP_LENGTH = 6;
  private static final int OTP_EXPIRY_MINUTES = 2;
  private static final int OTP_EXPIRY_MINUTES_PASWORD = 5;
  private static final int MAX_VERIFICATION_ATTEMPTS = 3;

  @Autowired
  private EmailService emailService;

  public boolean generateAndSendOtp(UUID otpID, String userId, String userType, UserData userData) {

    String otp = generateOtp(OTP_LENGTH);

    LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES);
    otpStorage.put(otpID, new OtpData(otp, expiryTime, userType, userId, userData));

    return sendOtpEmail(adminEmail, otp, userData);
  }

  public boolean generateAndSendPasswordResetOtp(UUID otpID, String userId, String userType, UserData userData) {

    String otp = generateOtp(OTP_LENGTH);
    String FrontEnd = frontEndIp + "/auth/reset-password";

    LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES_PASWORD);
    otpStorage.put(otpID, new OtpData(otp, expiryTime, userType, userId, userData));

    return sendPasswordResetOtp(adminEmail, otp, otpID.toString(), userData);
  }

  public OtpValidationResponse<UserData> validateOtp(UUID otpID, String userProvidedOtp) {
    OtpData storedOtpData = otpStorage.get(otpID);

    // Check if OTP exists for this user
    if (storedOtpData == null) {
      return new OtpValidationResponse<UserData>(OtpValidationResult.NO_OTP_FOUND);
    }

    // Check if OTP is expired
    if (LocalDateTime.now().isAfter(storedOtpData.getExpiryTime())) {
      // Remove expired OTP
      otpStorage.remove(otpID);
      return new OtpValidationResponse<UserData>(OtpValidationResult.EXPIRED);
    }

    storedOtpData.incrementAttempt();

    if (storedOtpData.getAttempts() > MAX_VERIFICATION_ATTEMPTS) {
      otpStorage.remove(otpID);
      return new OtpValidationResponse<UserData>(OtpValidationResult.MAX_ATTEMPTS_EXCEEDED);
    }

    if (storedOtpData.getOtp().equals(userProvidedOtp)) {
      otpStorage.remove(otpID);
      return new OtpValidationResponse<UserData>(OtpValidationResult.SUCCESS, storedOtpData.getUserType(),
          storedOtpData.getUserId(), storedOtpData.getUserData());
    }

    return new OtpValidationResponse<UserData>(OtpValidationResult.INVALID);
  }

  private String generateOtp(int length) {
    SecureRandom random = new SecureRandom();
    StringBuilder otp = new StringBuilder();

    for (int i = 0; i < length; i++) {
      otp.append(random.nextInt(10)); // Append digit between 0-9
    }

    return otp.toString();
  }

  private boolean sendOtpEmail(String email, String otp, UserData userData) {
    String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMMM d, yyyy"));
    int year = LocalDateTime.now().getYear();
    System.out.println(date);

    String fullName = userData.getFirstName() + " " + userData.getLastName();

    String htmlBody = """
        <!DOCTYPE html>
        <html>
        <head>
          <meta charset="UTF-8">
          <meta name="viewport" content="width=device-width, initial-scale=1.0">
          <title>Your Authentication Code</title>
        </head>
        <body style="font-family: 'Segoe UI', Arial, sans-serif; line-height: 1.6; max-width: 600px; margin: 0 auto; padding: 20px; color: #333333;">
          <div style="background-color: #ffffff; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); overflow: hidden;">
            <!-- Header -->
            <div style="background-color: #0066cc; color: white; padding: 20px; text-align: center;">
              <h1 style="margin: 0; font-size: 24px;">Authentication Code</h1>
            </div>

            <!-- Content -->
            <div style="padding: 30px 20px;">
              <p style="font-size: 16px;">Hello %s,</p>

              <p style="font-size: 16px;">
                To complete your login to the Student Management System, please use the following verification code:
              </p>

              <!-- OTP Box -->
              <div style="background-color: #f5f5f5; border-radius: 5px; padding: 20px; text-align: center; margin: 25px 0;">
                <p style="font-size: 32px; font-weight: bold; letter-spacing: 5px; margin: 0; color: #0066cc; font-family: monospace;">%s</p>
              </div>

              <p style="font-size: 16px;">This code will expire in <strong>2 minutes</strong>.</p>

              <p style="margin-top: 25px; font-size: 14px;">
                If you did not request this code, please ignore this email and consider changing your password.
              </p>
            </div>

            <!-- Security Note -->
            <div style="background-color: #f9f9f9; padding: 15px 20px; border-top: 1px solid #eeeeee;">
              <p style="margin: 0; font-size: 14px; color: #666666;">
                <strong>Security Tip:</strong> Never share this code with anyone, including staff members or support.
              </p>
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
        """
        .formatted(fullName, otp, year);

    try {
      emailService.sendHtmlEmail(email, "Your Authentication Code - Student Management System", htmlBody);
      return true;
    } catch (Exception e) {
      // Log the exception
      return false;
    }
  }

private boolean sendPasswordResetOtp(String email, String otp, String otpID, UserData userData){
    String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMMM d, yyyy"));
    int year = LocalDateTime.now().getYear();
    System.out.println(date);

    String fullName = userData.getFirstName() + " " + userData.getLastName();

    // Generate reset password URL
    String resetUrl = frontEndIp + "/auth/reset-password?token=" + otp + "&otpID=" + otpID;

    String htmlBody = """
        <!DOCTYPE html>
        <html>
        <head>
          <meta charset="UTF-8">
          <meta name="viewport" content="width=device-width, initial-scale=1.0">
          <title>Password Reset Code</title>
        </head>
        <body style="font-family: 'Segoe UI', Arial, sans-serif; line-height: 1.6; max-width: 600px; margin: 0 auto; padding: 20px; color: #333333;">
          <div style="background-color: #ffffff; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); overflow: hidden;">
            <!-- Header -->
            <div style="background-color: #0066cc; color: white; padding: 20px; text-align: center;">
              <h1 style="margin: 0; font-size: 24px;">Password Reset Code</h1>
            </div>

            <!-- Content -->
            <div style="padding: 30px 20px;">
              <p style="font-size: 16px;">Hello %s,</p>

              <p style="font-size: 16px;">
                You have requested to reset your password for the Student Management System.
              </p>

                            
              <!-- Reset Password Link -->
              <p style="font-size: 16px; margin-top: 20px;">
              You can reset your password directly using the following link:
              </p>
              <p style="text-align: center; margin: 20px 0;">
              <a href="%s" style="display: inline-block; background-color: #0066cc; color: white; padding: 10px 20px; border-radius: 5px; text-decoration: none; font-size: 16px;">
              Reset Password
              </a>
              </p>
              
              <p style="font-size: 16px;">This link will expire in <strong>5 minutes</strong>.</p>
              <p style="margin-top: 25px; font-size: 14px;">
                If you did not request to reset your password, please ignore this email and ensure your account is secure.
              </p>
            </div>

            <!-- Security Note -->
            <div style="background-color: #f9f9f9; padding: 15px 20px; border-top: 1px solid #eeeeee;">
              <p style="margin: 0; font-size: 14px; color: #666666;">
                <strong>Security Tip:</strong> Never share this code with anyone, including staff members or support.
              </p>
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
        """
        .formatted(fullName, resetUrl, year);

    try {
      emailService.sendHtmlEmail(email, "Password Reset Code - Student Management System", htmlBody);
      return true;
    } catch (Exception e) {
      // Log the exception
      return false;
    }
}

  public enum OtpValidationResult {
    SUCCESS,
    INVALID,
    EXPIRED,
    NO_OTP_FOUND,
    MAX_ATTEMPTS_EXCEEDED
  }

  public class OtpValidationResponse<T> {
    private final OtpValidationResult result;
    private final String userRole;
    private final String userType;
    private final T data;

    public OtpValidationResponse(OtpValidationResult result) {
      this.result = result;
      this.userRole = null;
      this.userType = null;
      this.data = null;

    }

    public OtpValidationResponse(OtpValidationResult result, String userRole, String userType, T data) {
      this.result = result;
      this.userRole = userRole;
      this.userType = userType;
      this.data = data;
    }

    public OtpValidationResult getResult() {
      return result;
    }

    public T getData() {
      return data;
    }

    public String getUserRole() {
      return this.userRole;
    }

    public String getUserType() {
      return this.userType;
    }
  }

}
