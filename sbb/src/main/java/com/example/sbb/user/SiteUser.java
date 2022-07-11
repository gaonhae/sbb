package com.example.sbb.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class SiteUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)  //@Column(unique = true) ==> 중복값을 허용하지 않음
    private String username;

    private String password;

    @Column(unique = true)  //@Column(unique = true) ==> 중복값을 허용하지 않음
    private String email;
}
