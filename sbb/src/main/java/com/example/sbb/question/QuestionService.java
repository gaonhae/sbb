package com.example.sbb.question;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.sbb.answer.Answer;
import com.example.sbb.user.SiteUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.sbb.DataNotFoundException;

import lombok.RequiredArgsConstructor;

import javax.persistence.criteria.*;

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
	
	public Page<Question> getList(int page, String kw){	//questionList 템플릿에 넘겨 페이징을 위해 사용
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("createDate"));
		Pageable pageable = PageRequest.of(page, 30, Sort.by(sorts));
		Specification<Question> spec = search(kw);
		return this.questionRepository.findAll(spec, pageable);
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

	private Specification<Question> search(String kw) {
		return new Specification<>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
				query.distinct(true);  // 중복을 제거
				Join<Question, SiteUser> u1 = q.join("author", JoinType.LEFT);
				Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
				Join<Answer, SiteUser> u2 = a.join("author", JoinType.LEFT);
				return cb.or(cb.like(q.get("subject"), "%" + kw + "%"), // 제목
						cb.like(q.get("content"), "%" + kw + "%"),      // 내용
						cb.like(u1.get("username"), "%" + kw + "%"),    // 질문 작성자
						cb.like(a.get("content"), "%" + kw + "%"),      // 답변 내용
						cb.like(u2.get("username"), "%" + kw + "%"));   // 답변 작성자
			}
		};
	}

}
