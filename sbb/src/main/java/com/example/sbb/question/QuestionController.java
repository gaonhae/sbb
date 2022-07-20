package com.example.sbb.question;


import javax.validation.Valid;

import com.example.sbb.user.SiteUser;
import com.example.sbb.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.sbb.answer.AnswerForm;

import lombok.RequiredArgsConstructor;

import java.security.Principal;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.server.ResponseStatusException;

@RequestMapping("/question")
@Controller
@RequiredArgsConstructor
public class QuestionController {
	
	private final QuestionService questionService; 
	private final QuestionRepository questionRepository;
	private final UserService userService;
	
	@RequestMapping("/list")	//질문 리스트
	public String list(Model model, @RequestParam(value="page", defaultValue="0") int page, @RequestParam(value = "kw", defaultValue = "") String kw) {
		Page<Question> paging = this.questionService.getList(page, kw);
        model.addAttribute("paging", paging);	//템플릿에 페이징에 대한 내용을 넘겨줌
		model.addAttribute("kw", kw);
		return "questionList";
	}
	
	@RequestMapping(value = "/detail/{id}")	//질문 상세 내용
	public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm) {
		Question question = this.questionService.getQuestion(id);
		model.addAttribute("question", question);	//템플릿에 질문의 상세 내용을 넘겨줌
		return "questionDetail";
	}

	@PreAuthorize("isAuthenticated()")	//principal이 null일 경우 로그인 페이지로 진입
	@GetMapping("/create")	//GET으로 create 링크에 진입, 즉 질문 생성 폼 요청
	public String questionCreate(QuestionForm questionForm) {
		return "questionForm";
	}

	@PreAuthorize("isAuthenticated()")	//principal이 null일 경우 로그인 페이지로 진입
	@PostMapping("/create")	//POST로 create 링크에 진입, 즉 질문 생성 폼 내용을 Service의 create 메서드로 전달
	public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingresult, Principal principal) {
		if (bindingresult.hasErrors()) {
			return "questionForm";
		}
		SiteUser siteUser = this.userService.getUser(principal.getName());
		this.questionService.create(questionForm.getSubject(), questionForm.getContent(),siteUser);
		return "redirect:/question/list";
		
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modify/{id}")
	public String questionModify(QuestionForm questionForm, @PathVariable("id") Integer id, Principal principal) {
		Question question = this.questionService.getQuestion(id);
		if(!question.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
		}
		questionForm.setSubject(question.getSubject());
		questionForm.setContent(question.getContent());
		return "questionForm";
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modify/{id}")
	public String questionModify(@Valid QuestionForm questionForm, @PathVariable("id") Integer id, BindingResult bindingResult, Principal principal){
		if (bindingResult.hasErrors()) {
			return "questionForm";
		}
		Question question = this.questionService.getQuestion(id);
		if(!question.getAuthor().getUsername().equals(principal.getName())){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
		}
		this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent());
		return String.format("redirect:/question/detail/%s",id);
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{id}")
	public String questionDelete(Principal principal, @PathVariable("id") Integer id) {
		Question question = this.questionService.getQuestion(id);
		if (!question.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
		}
		this.questionService.delete(question);
		return "redirect:/";
	}
	

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/vote/{id}")
	public String quesitionVote(Principal principal, @PathVariable("id") Integer id){
		Question question = this.questionService.getQuestion(id);
		SiteUser siteUser = this.userService.getUser(principal.getName());
		this.questionService.vote(question, siteUser);
		return String.format("redirect:/question/detail/%s", id);
	}
}
