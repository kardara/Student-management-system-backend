package com.kardara.studentManagement.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kardara.studentManagement.DTO.OperationResult;
import com.kardara.studentManagement.model.Semester;
import com.kardara.studentManagement.model.enumeration.ESemesterStatus;
import com.kardara.studentManagement.repository.SemesterRepository;

@Service
public class SemesterService {

    @Autowired
    private SemesterRepository semesterRepository;

    public OperationResult add(Semester semester) {
        boolean exist = semesterRepository.existsByYearAndName(semester.getYear(), semester.getName());
        if (exist) {
            return new OperationResult(false, "Cannot save semester with same name in the same year");
        } else {
            semesterRepository.save(semester);
            // OperationResult res = new OperationResult();
            // res.setSuccess(true);
            // res.setMessage("Semester saved successfully");
            return new OperationResult(true, "Semester saved successfully");
            // return res;

        }
    }

    public List<Semester> get() {
        return semesterRepository.findAll();
    }

    public OperationResult update(Semester semester) {
        Optional<Semester> sem = semesterRepository.findById(semester.getId());
        if (sem.isPresent()) {
            Semester newSem = sem.get();

            newSem.setName(semester.getName());
            newSem.setStartDate(semester.getStartDate());
            newSem.setEndDate(semester.getEndDate());
            newSem.setYear(semester.getYear());
            newSem.setStatus(semester.getStatus());

            semesterRepository.save(newSem);

            return new OperationResult(true,
                    "Semester " + newSem.getName() + " of " + newSem.getYear() + " updated successfully");
        }
        return new OperationResult(false, "Could not update the semester");

    }

    public OperationResult delete(Semester semester) {
        Optional<Semester> sem = semesterRepository.findById(semester.getId());
        if (sem.isPresent()) {
            semesterRepository.delete(sem.get());
            return new OperationResult(true,
                    "Semester " + sem.get().getName() + " of " + sem.get().getYear() + " deleted successfully");
        }
        return new OperationResult(false, "Could not not the semester");
    }

    public Semester getCurrentSemester() {
        Optional<Semester> semester = semesterRepository.findFirstByStatusOrderByStartDateDesc(ESemesterStatus.ACTIVE);
        if (semester.isEmpty()) {
            semester = semesterRepository.findFirstByOrderByStartDateDesc();
        }
        return semester.get();
    }
}
