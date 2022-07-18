package com.example.sbb.answer;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.example.sbb.question.Question;

import com.example.sbb.user.SiteUser;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity	//답변 엔티티
public class Answer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(columnDefinition = "TEXT")
	private String content;
	
	private LocalDateTime createDate;
	
	@ManyToOne //부모 자식 관계, 트리구조, question은 부모, answer은 자식
	private Question question;

	private LocalDateTime modifyDate;

	@ManyToOne
	private SiteUser author;


}
