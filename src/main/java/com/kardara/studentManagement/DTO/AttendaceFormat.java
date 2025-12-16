package com.kardara.studentManagement.DTO;
import com.kardara.studentManagement.model.enumeration.EAttendanceStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttendaceFormat {

    private int id;
    private EAttendanceStatus status;


}
