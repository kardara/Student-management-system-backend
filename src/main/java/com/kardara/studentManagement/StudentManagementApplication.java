package com.kardara.studentManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.kardara.studentManagement.model.Semester;
import com.kardara.studentManagement.model.Staff;
import com.kardara.studentManagement.model.enumeration.ESemesterName;
import com.kardara.studentManagement.model.enumeration.ESemesterStatus;
import com.kardara.studentManagement.model.enumeration.EStaffRole;
import com.kardara.studentManagement.repository.SemesterRepository;
import com.kardara.studentManagement.repository.StaffRepository;

import java.time.LocalDate;

@SpringBootApplication
public class StudentManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentManagementApplication.class, args);
	}

	@Bean
	public CommandLineRunner createDefaultAdmin(@Autowired StaffRepository staffRepository) {
		return args -> {
			if (staffRepository.findByEmail("azdjerou@gmail.com").isEmpty()) {
				BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(8);
				Staff admin = new Staff();
				admin.setFirstName("Abdoulaye");
				admin.setLastName("Zakaria");
				admin.setEmail("azdjerou@gmail.com");
				admin.setPhone("0791375009");
				admin.setAddress("Kigali");
				admin.setRole(EStaffRole.ADMIN);
				admin.setPassword(encoder.encode("Admin123"));
				staffRepository.save(admin);
				System.out.println("Default admin created: azdjerou@gmail.com / Admin123");
			}
		};
	}

	@Bean
	public CommandLineRunner createDefaultSemester(@Autowired SemesterRepository semesterRepository) {
		return args -> {
			if (semesterRepository.count() == 0) {
				Semester semester = new Semester();
				semester.setName(ESemesterName.FALL);
				semester.setYear(2025);
				semester.setStartDate(LocalDate.of(2025, 1, 1));
				semester.setEndDate(LocalDate.of(2025, 6, 30));
				semester.setStatus(ESemesterStatus.ACTIVE);
				semesterRepository.save(semester);
				System.out.println("Default semester created: Semester 1 2025");
			}
		};
	}

}
