package com.example.demo;

import com.example.demo.question.QuestionForm;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;


import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public class QuestionTests {
    @Autowired
    private MockMvc mockMvc;



    //QuestionForm TestCode
    @Test
    void questionFormValidationTest() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        // 일부 유효하지 않은 케이스
        QuestionForm invalidFormA = new QuestionForm();
        invalidFormA.setSubject("");
        invalidFormA.setContent("Some content");

        Set<ConstraintViolation<QuestionForm>> violationsA = validator.validate(invalidFormA);
        assertThat(violationsA).isNotEmpty();

        QuestionForm invalidFormB = new QuestionForm();
        invalidFormB.setSubject("A valid subject");
        invalidFormB.setContent("");

        Set<ConstraintViolation<QuestionForm>> violationsB = validator.validate(invalidFormB);
        assertThat(violationsB).isNotEmpty();

        // 일부 유효한 케이스
        QuestionForm validForm = new QuestionForm();
        validForm.setSubject("A valid subject");
        validForm.setContent("This is a valid content");

        Set<ConstraintViolation<QuestionForm>> validViolations = validator.validate(validForm);
        assertThat(validViolations).isEmpty();
    }

}
