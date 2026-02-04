package com.alanhss.ClashZone.infra.presentation;

import com.alanhss.ClashZone.core.domain.AuthDomain;
import com.alanhss.ClashZone.core.usecases.auth.LoginUsecase;
import com.alanhss.ClashZone.core.usecases.auth.RegisterUsecase;
import com.alanhss.ClashZone.infra.dtos.AuthDtos.LoginRequestDto;
import com.alanhss.ClashZone.infra.dtos.AuthDtos.LoginResponseDto;
import com.alanhss.ClashZone.infra.dtos.AuthDtos.RegisterRequestDto;
import com.alanhss.ClashZone.infra.dtos.AuthDtos.RegisterResponseDto;
import com.alanhss.ClashZone.infra.mappers.AuthMappers.AuthRequestMapper;
import com.alanhss.ClashZone.infra.mappers.AuthMappers.AuthResponseMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("clashzone/auth/")
@RequiredArgsConstructor
public class AuthController {

    private final RegisterUsecase registerUsecase;
    private final LoginUsecase loginUsecase;
    private final AuthRequestMapper requestMapper;
    private final AuthResponseMapper responseMapper;

    @PostMapping("register")
    public ResponseEntity<RegisterResponseDto> register(@Valid @RequestBody RegisterRequestDto request){

        RegisterRequestDto requestValidado = requestMapper.validarEPrepararRegister(request);

        AuthDomain.RegisterData registerData = requestMapper.toRegisterData(requestValidado);

        AuthDomain authDomain = registerUsecase.execute(registerData);

        RegisterResponseDto response = responseMapper.toRegisterResponse(authDomain);

        return ResponseEntity.ok(response);
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto request){

        LoginRequestDto requestValidado = requestMapper.validarEPrepararLogin(request);

        AuthDomain.LoginData loginData = requestMapper.toLoginData(requestValidado);

        AuthDomain authDomain = loginUsecase.execute(loginData);

        LoginResponseDto response = responseMapper.toLoginResponse(authDomain);

        return ResponseEntity.ok(response);
    }
}
