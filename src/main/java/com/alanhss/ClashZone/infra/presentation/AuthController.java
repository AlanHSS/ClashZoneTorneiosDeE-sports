package com.alanhss.ClashZone.infra.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("clashzone/auth/")
@RequiredArgsConstructor
public class AuthController {

    @PostMapping("register")
    public ResponseEntity<String> register(){
        return null;
    }

    @PostMapping("login")
    public ResponseEntity<String> login(){
        return null;
    }
}
