package com.devJ.certification_spring.modules.certifications.useCases;

import com.devJ.certification_spring.modules.students.entities.CertificationStudentEntity;
import com.devJ.certification_spring.modules.students.repositories.CertificationStudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class Top10RankingUseCase {

    @Autowired
    private CertificationStudentRepository certificationStudentRepository;

    public List<CertificationStudentEntity> execute() {
        var result = this.certificationStudentRepository.findTop10ByOrderByGradeDesc();
        return result;

    }
}
