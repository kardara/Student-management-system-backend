package com.kardara.studentManagement.DTO;

import java.time.LocalDateTime;

import lombok.Getter;


@Getter
public  class OtpData {
    private final String otp;
    private final LocalDateTime expiryTime;
    private int attempts;
    private final String userType;
    private final String userId;
    private final UserData userData;

    public OtpData(String otp, LocalDateTime expiryTime, String userType, String userId, UserData userData)  {
      this.otp = otp;
      this.expiryTime = expiryTime;
      this.attempts = 0;
      this.userId = userId;
      this.userType = userType;
      this.userData = userData;
    }

    public void incrementAttempt() {
      this.attempts++;
    }
  }
