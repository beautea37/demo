package com.example.demo;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.example.demo.question.Question;
import com.example.demo.question.QuestionRepository;
import com.example.demo.question.QuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
class DemoApplicationTests {


    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionService questionService;



    //Question Test
    @Test
    void testJpa() {
        Question q1 = Question.builder()
                .subject("q1subject")
                .content("q1content")
                .createDate(LocalDateTime.now())
                .build();
        this.questionRepository.save(q1); // 첫번째 질문 저장

        Question q2 = Question.builder()
                .subject("q2subject")
                .content("q2content")
                .createDate(LocalDateTime.now())
                .build();
        this.questionRepository.save(q2); // 두번째 질문 저장
    }


    @Test
    void findAllJpa() {
        // 'Question' 테이블의 모든 레코드를 반환하는 'findAll' 메서드 호출
        List<Question> all = this.questionRepository.findAll();

        // 반환된 'Question' 객체 목록의 길이 확인. ID값. 즉 저장된 행의 개수 확인.
        assertEquals(6, all.size());

        // 반환된 목록의 첫 번째 'Question' 객체 참조. Q 안에서 몇 번째 열을 확인할 것인지
        Question q = all.get(2);

        // 첫 번째 'Question' 객체의 Subject 확인 (예상값: "Q1 테스트?").
        assertEquals("Q1 테스트?", q.getSubject());
        System.out.println("--------------" + q.getSubject());
    }

    @Test
    void findId() {
        // id 값이 2인 질문 찾기
        Optional<Question> foundQuestion = this.questionRepository.findById(2);

        // 테스트 결과를 확인하기 위한 assertTrue를 사용한 코드
        assertTrue(foundQuestion.isPresent());

        // 찾은 질문의 내용 확인
        if (foundQuestion.isPresent()) {
            Question questionWithId2 = foundQuestion.get();
            System.out.println("질문 ID: " + questionWithId2.getId());
            System.out.println("질문 제목: " + questionWithId2.getSubject());
            System.out.println("질문 내용: " + questionWithId2.getContent());
        } else {
            System.out.println("실패!!!!");
        }
    }

    @Test
    void findId2() {
        //Optional은 null 처리를 유연하게 처리하기 위해 사용하는 클래스로 아래와 같이 isPresent로 null이 아닌지를 확인한 후에 get으로 실제 Question 객체 값을 얻어야 한다.
        Optional<Question> oq = this.questionRepository.findById(3);

        if (oq.isPresent()) {
            Question q = oq.get();
            assertEquals("Q1 테스트?", q.getSubject());
        }
    }

    @Test
    void findSubject() {
//        Question q = this.questionRepository.findBySubject("Q1 테스트?");
//        System.out.println(q);

        //대체 왜 안되는지 모르겠는 부분. 나중에 확인 요망. #q
//        Question q = this.questionRepository.findBySubject("Q1 테스트?");
//        assertEquals(4, q.getId());

        Question q = this.questionRepository.findBySubject("subject q");
        assertEquals(1, q.getId());
        System.out.println("----------------" + q);
    }

    @Test
    void findSubjectAndContent() {
        Question q = this.questionRepository.findBySubjectAndContent("subject", "asdfaasdfadsf");
        assertEquals(1, q.getId());
    }

    @Test
    void findBySubjectList() {
        List<Question> qList = this.questionRepository.findBySubjectLike("asdf");   //시작이 asdf
        Question q = qList.get(0);
        assertEquals("asdfsafdas", q.getSubject());
    }

    @Test
    void modifySubject() {
        Optional<Question> mq = this.questionRepository.findById(1);

        Question q = Question.builder()
                .subject("수정 테스트1").build();
        this.questionRepository.save(q);
    }

    @Test
    void deleteData() {
        assertEquals(5, this.questionRepository.count());
        Optional<Question> oq = this.questionRepository.findById(1);

        Question q = oq.get();
        this.questionRepository.delete(q);
        assertEquals(4, this.questionRepository.count());

    }


    @Test
    void pagingTest() {
        for (int i = 1; i <= 111; i++) {
            String subject = String.format("테스트 데이터 : [%03d]", i);
            String content = "내용무";
            this.questionService.create(subject, content, null);
        }
    }
}

