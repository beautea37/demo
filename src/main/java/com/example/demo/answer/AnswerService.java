package com.example.demo.answer;

import com.example.demo.DataNotFoundException;
import com.example.demo.question.Question;
import com.example.demo.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AnswerService {

    private final AnswerRepository answerRepository;
    public Answer create(Question question, String content, SiteUser author) {
        Answer answer = new Answer();
        answer.setContent(content);
        answer.setCreateDate(LocalDateTime.now());
        answer.setQuestion(question);
        answer.setAuthor(author);
        this.answerRepository.save(answer);
        return answer;
    }


    // id를 사용하여 Answer 가져옴
    public Answer getAnswer(Integer id) {
        // findById를 사용하여 DB에 Answerrk있는지 확인
        Optional<Answer> answer = this.answerRepository.findById(id);

        // Optional 객체에 Answer가 포함되어 있을 경우, 해당 Answer를 리턴
        if (answer.isPresent()) {
            return answer.get();
        } else {
            // Answer가 없을 경우, DataNotFoundException로 경고메시지 갈김
            throw new DataNotFoundException("answer not found");
        }
    }

    // #수정. Answer 객체의 내용을 수정하고 저장소에 변경 사항을 저장
    public void modify(Answer answer, String content) {
        // 새로운 내용으로 Answer의 내용을 변경
        answer.setContent(content);
        // 수정 날짜를 현재 시간으로 설정
        answer.setModifyDate(LocalDateTime.now());
        // 변경된 Answer 객체를 저장소에 저장
        this.answerRepository.save(answer);
    }

    public void delete(Answer answer) {
        this.answerRepository.delete(answer);
    }

    //#추천
    public void vote(Answer answer, SiteUser siteUser) {
        answer.getVoter().add(siteUser);
        this.answerRepository.save(answer);
    }
}
