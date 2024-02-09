package com.devJ.certification_spring.modules.students.repositories;

import com.devJ.certification_spring.modules.students.entities.CertificationStudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CertificationStudentRepository extends JpaRepository<CertificationStudentEntity, UUID> {

    @Query("SELECT c FROM certifications c INNER JOIN c.studentEntity std WHERE std.email = :email AND c.technology = :technology")
    List<CertificationStudentEntity> findByStudentEmailAndTechnology (String email, String technology);

    @Query("SELECT c FROM certifications c ORDER BY c.grate DESC LIMIT 10")
    List<CertificationStudentEntity> findTop10ByOrderByGradeDesc();

}
