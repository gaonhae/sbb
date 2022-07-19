package com.example.sbb.question;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.sbb.user.SiteUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.sbb.DataNotFoundException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class QuestionService {
	
	private final QuestionRepository questionRepository;
	
	public Question getQuestion(Integer id) {	//questionRepository의 findById와 같은 기능. 보안을 위해 service에 메서드를 구현하여 이용
		Optional<Question> question = this.questionRepository.findById(id);
		if( question.isPresent()) {
			return question.get();
		}
		else {
			throw new DataNotFoundException("question not found");		}
	}
	
	public void create(String subject, String content, SiteUser siteUser) {	//질문 생성을 위한 메서드
		Question q = new Question();
		q.setSubject(subject);
		q.setContent(content);
		q.setCreateDate(LocalDateTime.now());
		q.setAuthor(siteUser);
		this.questionRepository.save(q);
	}
	
	public Page<Question> getList(int page){	//questionList 템플릿에 넘겨 페이징을 위해 사용
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("createDate"));
		Pageable pageable = PageRequest.of(page, 30, Sort.by(sorts));
		return this.questionRepository.findAll(pageable);
	}

	public void modify(Question question, String subject, String content){
		question.setContent(content);
		question.setSubject(subject);
		question.setModifyDate(LocalDateTime.now());
		this.questionRepository.save(question);
	}

	public void delete(Question question) {
		this.questionRepository.delete(question);
	}

	public void vote(Question question, SiteUser siteUser){
		question.getVoter().add(siteUser);
		this.questionRepository.save(question);
	}

}
