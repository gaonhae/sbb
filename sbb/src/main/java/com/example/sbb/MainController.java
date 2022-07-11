package com.example.sbb;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

	@RequestMapping("/")	//RootURL을 question/list로 설정
	public String root() {
		return "redirect:/question/list";
	}
}
