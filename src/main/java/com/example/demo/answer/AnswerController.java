package com.example.demo.answer;

import com.example.demo.question.Question;
import com.example.demo.question.QuestionService;
import com.example.demo.user.SiteUser;
import com.example.demo.user.UserRepository;
import com.example.demo.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@RequestMapping("/answer")
@RequiredArgsConstructor
@Controller
public class AnswerController {

    private final QuestionService questionService;
    private final AnswerService answerService;
    private final UserService userService;
    @PostMapping("/create/{id}")
    @PreAuthorize("isAuthenticated()")
    public String createAnswer(Model model, @PathVariable("id") Integer id,
                               //bindgResult는 폼 검증 결과로 AnswerForm 검증한다. Principal은 인증된 사용자 정보 담고있는 것
                               @Valid AnswerForm answerForm, BindingResult bindingResult, Principal principal) {
        // 1. 질문 아이디로 질문을 조회
        Question question = this.questionService.getQuestion(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        // 2. AnswerForm 유효성 검사 결과를 체크
        if (bindingResult.hasErrors()) {
            // 2.1 검사 결과에 오류가 있는 경우, 해당 오류를 model에 담아 질문 페이지로 리턴
            model.addAttribute("question", question);
            return "question/question_detail";
        }
        // 3. 검사 결과에 오류가 없는 경우, 답변을 생성
        this.answerService.create(question, answerForm.getContent(), siteUser);
        // 4. 생성된 답변이 포함된 질문 상세 페이지로 리다이렉트
        return String.format("redirect:/question/detail/%s", id);
    }

}