package com.example.sbb.answer;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//답변 폼
public class AnswerForm {
	
	@NotEmpty(message="내용은 필수항목입니다.")
	private String content;
	
}
