package com.example.sbb.question;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.*;

import com.example.sbb.answer.Answer;

import com.example.sbb.user.SiteUser;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity // 질문 엔티티
public class Question {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)  //GenerationType.IDENTITY == 일정 순서의 고유번호를 가지기 위한 설정
	private Integer id;
	
	@Column(length = 200)
	private String subject;
	
	@Column(columnDefinition = "TEXT")
	private String content;
	
	private LocalDateTime createDate;
	
	@OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)	//CascadeType.REMOVE 질문을 삭제하면 답변들도 모두 삭제
	private List<Answer> answerList;

	private LocalDateTime modifyDate;

	@ManyToOne
	private SiteUser author;

}
