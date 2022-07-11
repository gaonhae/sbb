package com.example.sbb.user;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    //GET방식으로 호출, 회원가입 템플릿 클라이언트에게
    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm){
        return "signupForm";
    }


    //POST방식으로 호출되었을 경우, 회원가입 템플릿 서버로
    @PostMapping("/signup")
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult){
        //입력값에 오류가 있을 경우
        if (bindingResult.hasErrors()){
            return "signupForm";
        }

        //비밀번호와 비밀번호 재입력이 다를 경우
        if(!userCreateForm.getPassword().equals(userCreateForm.getPassword2())){
            bindingResult.rejectValue("password2", "passwordInCorrect", "입력값이 일치하지 않습니다.");
            return "signupForm";
        }

        //새로운 user 생성
        userService.create(userCreateForm.getUsername(), userCreateForm.getEmail(), userCreateForm.getPassword());
        return "redirect:/";
    }

    //GET 방식으로 호출되었을 경우, 로그인 템플릿 클라이언트에게
    @GetMapping("/login")
    public String login(){
        return "loginForm";
    }


}
