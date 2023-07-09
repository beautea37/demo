package com.example.demo.question;

import com.example.demo.DataNotFoundException;
import com.example.demo.answer.Answer;
import com.example.demo.user.SiteUser;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;


    //검색 활용
    private Specification<Question> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                //자세한 내용은 Repository로
                //q를 통해 Question의 root 엔티티 객체 땡겨옴. (질문 제목, 내용 검색용)
                query.distinct(true);  // 중복을 제거
                Join<Question, SiteUser> u1 = q.join("author", JoinType.LEFT); //siteUser와 Question의 엔티티 중복값 author를 같이 조인함
                Join<Question, Answer> a = q.join("answerList", JoinType.LEFT); //Question과 Answer이 answerList로 교집합이기 때문에 같이 조인침
                Join<Answer, SiteUser> u2 = a.join("author", JoinType.LEFT); //비슷해
                return cb.or(cb.like(q.get("subject"), "%" + kw + "%"), // 제목
                        cb.like(q.get("content"), "%" + kw + "%"),      // 내용
                        cb.like(u1.get("username"), "%" + kw + "%"),    // 질문 작성자
                        cb.like(a.get("content"), "%" + kw + "%"),      // 답변 내용
                        cb.like(u2.get("username"), "%" + kw + "%"));   // 답변 작성자
            }
        };
    }

    public List<Question> getList() {
        return this.questionRepository.findAll();
    }


    public Question getQuestion(Integer id) {
        Optional<Question> question = this.questionRepository.findById(id);
        if (question.isPresent()) {
            return question.get();
        } else {
            throw new DataNotFoundException("question not found");
        }
    }

    //글 저장
    public void create(String subject, String content, SiteUser user) {
        Question question = Question.builder()
                .subject(subject)
                .content(content)
                .createDate(LocalDateTime.now())
                .author(user)
                .build();
        this.questionRepository.save(question);
    }

    //#페이징, #페이지 처리
    public Page<Question> getList(int page, String kw) {
        //페이지 역순하기 위해 Sort.Order로 리스트 추출, desc로 날짜 기준 역순
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Specification<Question> spec = search(kw);
        return this.questionRepository.findAllByKeyword(kw, pageable);
    }


    //#수정
public void modify(Question question, String subject, String content) {
    Question modifiedQuestion = question.toBuilder()
            .subject(subject)
            .content(content)
            .modifyDate(LocalDateTime.now())
            .build();
    //#tobuilder 써서 일부만 객체로 땡겨와서 수정되어야할 곳에 수정
    this.questionRepository.save(modifiedQuestion);
}

    //#삭제
    public void delete(Question question) {
        this.questionRepository.delete(question);
    }

    //#추천
    public void vote(Question question, SiteUser siteUser) {
        question.getVoter().add(siteUser);
        this.questionRepository.save(question);
    }
}

