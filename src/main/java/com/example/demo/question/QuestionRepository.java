package com.example.demo.question;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    Question findBySubject(String subject);
    Question findBySubjectAndContent(String subject, String content);
    List<Question> findBySubjectLike(String subject);
    Page<Question> findAll(Pageable pageable);
    Page<Question> findAll(Specification<Question> spec, Pageable pageable);


    //#쿼리
    @Query(
            "select "
                    + "distinct q " // 질문 검색. distinct는 중복 제거
                    + "from Question q " // Question entity에서 q 찾음
                    + "left outer join SiteUser u1 on q.author=u1 "
                    //LEFT OUTER JOIN을 사용하면 왼쪽 테이블의 모든 레코드를 포함하고 오른쪽 테이블의 일치하는 레코드들을 가져옴.
                    //오른쪽 테이블에서 일치하는 레코드가 없는 경우에는 NULL 리턴.
                    //한마디로 교집합.
                    + "left outer join Answer a on a.question=q " // 질문에 해당하는 Answer 엔티티를 가져옴.
                    + "left outer join SiteUser u2 on a.author=u2 " // 답변 작성자와 관련된 SiteUser 엔티티를 가져옴.
                    + "where " // 검색 키워드(%:kw%)와 관련된 조건을 적용합니다.
                    + "   q.subject like %:kw% " // 질문의 제목(subject)에 검색 키워드가 포함되어 있는 경우
                    + "   or q.content like %:kw% " // 또는 질문의 내용(content)에 검색 키워드가 포함되어 있는 경우
                    + "   or u1.userName like %:kw% " // 또는 질문 작성자의 사용자 이름(userName)에 검색 키워드가 포함되어 있는 경우
                    + "   or a.content like %:kw% " // 또는 답변의 내용(content)에 검색 키워드가 포함되어 있는 경우
                    + "   or u2.userName like %:kw% " // 또는 답변 작성자의 사용자 이름(userName)에 검색 키워드가 포함되어 있는 경우
    )
    Page<Question> findAllByKeyword(@Param("kw") String kw, Pageable pageable);

}