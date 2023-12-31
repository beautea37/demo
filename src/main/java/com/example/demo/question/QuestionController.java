package com.example.demo.question;

import com.example.demo.answer.AnswerForm;
import com.example.demo.question.Question;
import com.example.demo.question.QuestionService;
import com.example.demo.user.SiteUser;
import com.example.demo.user.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RequestMapping("/question")
@RequiredArgsConstructor
@Controller
public class QuestionController {

    private final QuestionService questionService;
    private final UserService userService;

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value="page", defaultValue="0") int page,     //페이징 정보 받는 파라미터
                       @RequestParam(value="kw", defaultValue="") String kw) {      //#검색. 검색할 때 KeyWord를 받아놓음.
        Page<Question> paging = this.questionService.getList(page, kw);
        model.addAttribute("paging", paging);       //#페이지 #페이징 정보를 받아오는 곳
        model.addAttribute("kw", kw);       //#검색 키워드 모델에다가 저장.
        return "question/question_list";
    }

    @GetMapping(value = "/detail/{id}")
    //AnswerForm을 객체로 안에 넣어놓는 이유는, #유효성검사를 위해서
    //당장에 객체로 사용하지 않지만 'question_detail'에 들어갈 때 thymeleaf 안에 있는 th:object 객체 참조가 안돼서 form 태그에서 thymeleaf에러가 뜸
    //둘다 뺼거 아니면 걍 입닫고 넣어라.
    public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm) {
        Question question = this.questionService.getQuestion(id);
        model.addAttribute("question", question);
        return "question/question_detail";
    }


    @GetMapping("/create")
    @PreAuthorize("isAuthenticated()")      //로그인 안한 상태에서 글 쓸라면 로그인시키는 어노테이션
    public String questionCreate(QuestionForm questionForm) {
        return "question/question_form";
    }


    // 질문 저장후 질문목록으로 리턴
    //Valid를 통해 QuestionForm의 유효성검사 컨펌함. thymeleaf에서 th:object 연결해 줘야함.
    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal) {
        // @Valid 애너테이션을 적용하면 QuestionForm의 @NotEmpty, @Size 등으로 설정한 검증 기능이 동작
        // BindingResult 매개변수는 @Valid 애너테이션으로 인해 검증이 수행된 결과를 의미하는 객체.
        // BindingResult는 그렇기 떄문에 @Valid바로 뒤에 위치해야 한다.
        if (bindingResult.hasErrors()) {
            return "question/question_form";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUser);
        return "redirect:/question/list";
    }


    //수정하기
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String questionModify(QuestionForm questionForm, @PathVariable("id") Integer id, Principal principal) {
        Question question = this.questionService.getQuestion(id);
        if(!question.getAuthor().getUserName().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());
        return "question/question_form";
    }

    
    
    //#수정. AnswerController와 같은 패턴
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult,
                                 Principal principal, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "question/question_form";
        }
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUserName().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent());
        return String.format("redirect:/question/detail/%s", id);
    }

    //#삭제. AnswerController와 같은 패턴
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String questionDelete(Principal principal, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUserName().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "니가 뭔데 삭제해");
        }
        this.questionService.delete(question);
        return "redirect:/";
    }

    //추천
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String questionVote(Principal principal, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.questionService.vote(question, siteUser);
        return String.format("redirect:/question/detail/%s", id);
    }
}