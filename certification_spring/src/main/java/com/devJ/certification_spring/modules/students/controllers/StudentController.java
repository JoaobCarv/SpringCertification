package com.devJ.certification_spring.modules.students.controllers;

import com.devJ.certification_spring.modules.students.dto.StudentCertificationAnswerDTO;
import com.devJ.certification_spring.modules.students.dto.VerifyHasCertificationDTO;
import com.devJ.certification_spring.modules.students.entities.CertificationStudentEntity;
import com.devJ.certification_spring.modules.students.useCases.StudentCertificationAnswersUseCase;
import com.devJ.certification_spring.modules.students.useCases.VerifyIfHasCertificationUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/students")
public class StudentController {



@Autowired
    private VerifyIfHasCertificationUseCase verifyIfHasCertificationUseCase;

@Autowired
private StudentCertificationAnswersUseCase studentCertificationAnswersUseCase;

    @PostMapping("/verifyIfHasCertification")
    public String verifyIfHasCertifications(@RequestBody VerifyHasCertificationDTO verifyHasCertificationDTO){
        var result = this.verifyIfHasCertificationUseCase.execute(verifyHasCertificationDTO);
        if (result) {
            return "Usuário já fez a prova";
        }
        return "Usuário pode fazer a prova";
    }

    @PostMapping("/certification/answer")
    public ResponseEntity<Object>certificationAnswer(
            @RequestBody StudentCertificationAnswerDTO studentCertificationAnswerDTO) throws Exception {
        try {
            var result = studentCertificationAnswersUseCase.execute(studentCertificationAnswerDTO);
            return ResponseEntity.ok().body(result);
            }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
