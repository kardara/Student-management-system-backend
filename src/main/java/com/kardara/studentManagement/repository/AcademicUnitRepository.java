package com.kardara.studentManagement.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kardara.studentManagement.model.AcademicUnit;
import java.util.Optional;
import java.util.List;

public interface AcademicUnitRepository extends JpaRepository<AcademicUnit, UUID> {

    boolean existsByCode(String code);

    Optional<AcademicUnit> findByCode(String code);

    List<AcademicUnit> findByParent(AcademicUnit parent);

}
