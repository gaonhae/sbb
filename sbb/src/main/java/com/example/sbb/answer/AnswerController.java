package com.example.sbb.answer;


import javax.validation.Valid;

import com.example.sbb.user.SiteUser;
import com.example.sbb.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.sbb.question.Question;
import com.example.sbb.question.QuestionService;

import lombok.RequiredArgsConstructor;

import java.security.Principal;
import org.springframework.security.access.prepost.PreAuthorize;

@RequestMapping("/answer")
@RequiredArgsConstructor
@Controller
public class AnswerController {
	
	private final QuestionService questionService;
	private final AnswerService answerservice;
	private final UserService userService;

	@PreAuthorize("isAuthenticated()")	//principal이 null일 경우 로그인 페이지로 진입
	@PostMapping("/create/{id}")	//질문 생성
	public String createAnswer(Model model, @PathVariable("id") Integer id, @Valid AnswerForm answerForm, BindingResult bindingResult, Principal principal) {
		Question question = this.questionService.getQuestion(id);
		SiteUser siteUser = this.userService.getUser(principal.getName());
		if(bindingResult.hasErrors()) {
            model.addAttribute("question", question);	//questionDetail 템플릿이 question 객체를 필요로 하므로
            return "questionDetail";
		}
		this.answerservice.create(question, answerForm.getContent(), siteUser);
		return String.format("redirect:/question/detail/%s", id);
	}

}
