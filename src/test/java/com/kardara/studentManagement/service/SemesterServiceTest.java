package com.kardara.studentManagement.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kardara.studentManagement.model.Semester;
import com.kardara.studentManagement.model.enumeration.ESemesterName;
import com.kardara.studentManagement.model.enumeration.ESemesterStatus;

@SpringBootTest
public class SemesterServiceTest {

    @Autowired
    private SemesterService semesterService;

    @Test
    public void testGetCurrentSemester() {
        Semester currentSemester = semesterService.getCurrentSemester();

        assertNotNull(currentSemester);
        assertEquals(ESemesterName.FALL, currentSemester.getName());
        assertEquals(2025, currentSemester.getYear());
        assertEquals(ESemesterStatus.ACTIVE, currentSemester.getStatus());
    }
}