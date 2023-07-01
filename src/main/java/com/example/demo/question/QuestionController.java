package com.example.demo.question;

import com.example.demo.answer.AnswerForm;
import com.example.demo.question.Question;
import com.example.demo.question.QuestionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/question")
@RequiredArgsConstructor
@Controller
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value="page", defaultValue="0") int page) {
        Page<Question> paging = this.questionService.getList(page);
        model.addAttribute("paging", paging);
        return "question/question_list";
    }

    @GetMapping(value = "/detail/{id}")
    //AnswerForm을 객체로 안에 넣어놓는 이유는, 유효성검사를 위해서임.
    //당장에 객체로 사용하지 않지만 'question_detail'에 들어갈 때 thymeleaf 안에 있는 th:object 객체 참조가 안돼서 form 태그에서 thymeleaf에러가 뜸
    //둘다 뺼거 아니면 걍 입닫고 넣어라.
    public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm) {
        Question question = this.questionService.getQuestion(id);
        model.addAttribute("question", question);
        return "question/question_detail";
    }


    @GetMapping("/create")
    public String questionCreate(QuestionForm questionForm) {
        return "question/question_form";
    }

    // 질문 저장후 질문목록으로 리턴
    //Valid를 통해 QuestionForm의 유효성검사 컨펌함. thymeleaf에서 th:object 연결해 줘야함.
    @PostMapping("/create")
    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult) {
        // @Valid 애너테이션을 적용하면 QuestionForm의 @NotEmpty, @Size 등으로 설정한 검증 기능이 동작
        // BindingResult 매개변수는 @Valid 애너테이션으로 인해 검증이 수행된 결과를 의미하는 객체.
        // BindingResult는 그렇기 떄문에 @Valid바로 뒤에 위치해야 한다.
        if (bindingResult.hasErrors()) {
            return "question/question_form";
        }
        this.questionService.create(questionForm.getSubject(), questionForm.getContent());
        return "redirect:/question/list";
    }
}