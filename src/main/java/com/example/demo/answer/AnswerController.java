package com.example.demo.answer;

import com.example.demo.question.Question;
import com.example.demo.question.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/answer")
@RequiredArgsConstructor
@Controller
public class AnswerController {

    private final QuestionService questionService;

    private final AnswerService answerService;
    @PostMapping("/create/{id}")
    public String createAnswer(Model model, @PathVariable("id") Integer id,

                               @Valid AnswerForm answerForm, BindingResult bindingResult) {
        // 1. 질문 아이디로 질문을 조회
        Question question = this.questionService.getQuestion(id);
        // 2. AnswerForm 유효성 검사 결과를 체크
        if (bindingResult.hasErrors()) {
            // 2.1 검사 결과에 오류가 있는 경우, 해당 오류를 model에 담아 질문 페이지로 리턴
            model.addAttribute("question", question);
            return "question/question_detail";
        }
        // 3. 검사 결과에 오류가 없는 경우, 답변을 생성
        this.answerService.create(question, answerForm.getContent());
        // 4. 생성된 답변이 포함된 질문 상세 페이지로 리다이렉트
        return String.format("redirect:/question/detail/%s", id);
    }

}