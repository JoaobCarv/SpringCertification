package com.devJ.certification_spring.modules.students.useCases;

import com.devJ.certification_spring.modules.questions.entities.QuestionEntity;
import com.devJ.certification_spring.modules.questions.repositories.QuestionRepository;
import com.devJ.certification_spring.modules.students.dto.StudentCertificationAnswerDTO;
import com.devJ.certification_spring.modules.students.dto.VerifyHasCertificationDTO;
import com.devJ.certification_spring.modules.students.entities.AnswersCertificationEntity;
import com.devJ.certification_spring.modules.students.entities.CertificationStudentEntity;
import com.devJ.certification_spring.modules.students.entities.StudentEntity;
import com.devJ.certification_spring.modules.students.repositories.CertificationStudentRepository;
import com.devJ.certification_spring.modules.students.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class StudentCertificationAnswersUseCase {


    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private CertificationStudentRepository certificationStudentRepository;

    @Autowired
    private VerifyIfHasCertificationUseCase verifyIfHasCertificationUseCase;


    public CertificationStudentEntity execute(StudentCertificationAnswerDTO dto) throws Exception {

       var hasCertification = this.verifyIfHasCertificationUseCase.execute(new VerifyHasCertificationDTO(dto.getEmail(), dto.getTechnology()));

       if (hasCertification){
           throw new Exception("Certificação já entregue!");
       }


       List<QuestionEntity> questionsEntity = questionRepository.findByTechnology(dto.getTechnology());

       AtomicInteger correctAnswers = new AtomicInteger(0);

        dto.getQuestionAnswers().stream().forEach(questionAnswer -> {
           var question = questionsEntity.stream()
                   .filter(q -> q.getId().equals(questionAnswer.getQuestionID()))
                   .findFirst().get();

           var findCorrectAlternative = question.getAlternatives().stream()
                   .filter(alternative -> alternative.isCorrect()).findFirst().get();

           if(findCorrectAlternative.getId().equals(questionAnswer.getAlternativeID())) {
               questionAnswer.setCorrect(isCorrect:true);
               correctAnswers.incrementAndGet();
           } else {
               questionAnswer.setCorrect(isCorrect:false);
           }

           var answersCertificationEntity = AnswersCertificationEntity.builder()
                   .answerID(questionAnswer.getAlternativeID())
                   .questionID(questionAnswer.getQuestion())
                   .isCorrect(questionAnswer.isCorrect()).build();

           answersCertification.add(answersCertificationEntity);
        });

        var student = studentRepository.findByEmail(dto.getEmail());
        UUID studentID ;
        if (student.isEmpty()) {
           var studentCreated = StudentEntity.builder().email(dto.getEmail()).build();
            studentCreated = studentRepository.save(studentCreated);
            studentID = studentCreated.getId();
        } else {
            studentID = student.get().getId();
        }

        List<AnswersCertificationEntity> answersCertifications = new ArrayList<>();

        CertificationStudentEntity certificationStudentEntity = CertificationStudentEntity.builder()
                .technology(dto.getTechnology())
                .studentID(studentID)
                .grate(correctAnswers.get())
                .build();

        var certificationStudentCreated =  certificationStudentRepository.save(certificationStudentEntity);

        answersCertifications.stream().forEach(answersCertification -> {
            answersCertification.setCertificationID(certificationStudentEntity.getId());
            answersCertification.setCertificationStudentEntity(certificationStudentEntity);
        });

        certificationStudentEntity.setAnswersCertificationEntities(answersCertifications);

        certificationStudentRepository.save(certificationStudentEntity);

        return certificationStudentCreated;

    }

}
