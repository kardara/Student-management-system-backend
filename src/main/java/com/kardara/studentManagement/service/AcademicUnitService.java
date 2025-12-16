package com.kardara.studentManagement.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kardara.studentManagement.DTO.OperationResult;
import com.kardara.studentManagement.model.AcademicUnit;
import com.kardara.studentManagement.repository.AcademicUnitRepository;

@Service
public class AcademicUnitService {

    @Autowired
    private AcademicUnitRepository academicUnitRepository;

    public OperationResult add(AcademicUnit academicUnit) {
        if (academicUnitRepository.existsByCode(academicUnit.getCode())) {
            return new OperationResult(false, "Academic Unit code already exists");
        }
        academicUnitRepository.save(academicUnit);
        return new OperationResult(true, "Academic Unit " + academicUnit.getName() + " saved successfully");
    }

    public OperationResult add(AcademicUnit academicUnit, String parentCode) {
        if (academicUnitRepository.existsByCode(academicUnit.getCode())) {
            return new OperationResult(false, "Academic Unit code already exists");
        }

        Optional<AcademicUnit> parent = academicUnitRepository.findByCode(parentCode);
        if (parent.isPresent()) {
            academicUnit.setParent(parent.get());
            academicUnitRepository.save(academicUnit);
            return new OperationResult(true, "Academic Unit " + academicUnit.getName()
                    + " saved successfully, with parent: " + parent.get().getName());
        } else {
            return new OperationResult(false, "Parent not found");
        }

    }

    public List<AcademicUnit> get() {
        return academicUnitRepository.findAll();
    }

   

    public OperationResult update (AcademicUnit academicUnit, String parentCode){
        Optional<AcademicUnit> acad = academicUnitRepository.findById(academicUnit.getId());

        if (acad.isPresent()) {
            AcademicUnit newAcadUnit = acad.get();
            newAcadUnit.setCode(academicUnit.getCode());
            newAcadUnit.setName(academicUnit.getName());
            Optional<AcademicUnit> parent = academicUnitRepository.findByCode(parentCode);
            if(parent.isPresent()){
                newAcadUnit.setParent(parent.get());
                newAcadUnit.setType(academicUnit.getType());
            }

        academicUnitRepository.save(newAcadUnit);
        return new OperationResult(true, "Academic unit " + academicUnit.getName()+ " updated successfully");
    }

    return new OperationResult(false, "Academic unit not found");
}

public OperationResult delete(AcademicUnit academicUnit) {
    Optional<AcademicUnit> a = academicUnitRepository.findById(academicUnit.getId());
    if (a.isPresent()) {
        academicUnitRepository.deleteById(academicUnit.getId());
        return new OperationResult(true, "Academic unit"+a.get().getName()+" deleted successfully");
    }

    return new OperationResult(false, "Academic unit not found");

}

}
