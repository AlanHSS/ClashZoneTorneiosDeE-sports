package com.alanhss.ClashZone.infra.mappers.AuthMappers;

import com.alanhss.ClashZone.core.domain.AuthDomain;
import com.alanhss.ClashZone.infra.dtos.AuthDtos.LoginRequestDto;
import com.alanhss.ClashZone.infra.dtos.AuthDtos.RegisterRequestDto;
import org.springframework.stereotype.Component;

@Component
public class AuthRequestMapper {

    public AuthDomain.RegisterData toRegisterData(RegisterRequestDto request) {
        return new AuthDomain.RegisterData(
                request.nomeDoUsuário(),
                request.nickname(),
                request.emailDoUsuario(),
                request.senhaDoUsuario()
        );
    }

    public AuthDomain.LoginData toLoginData(LoginRequestDto request) {
        return new AuthDomain.LoginData(
                request.emailDoUsuario(), // Apesar do nome, é o email
                request.senhaDoUsuario()
        );
    }

    public RegisterRequestDto validarEPrepararRegister(RegisterRequestDto request) {
        String nomeNormalizado = request.nomeDoUsuário() != null
                ? request.nomeDoUsuário().trim()
                : null;

        String nicknameNormalizado = request.nickname() != null
                ? request.nickname().trim()
                : null;

        String emailNormalizado = request.emailDoUsuario() != null
                ? request.emailDoUsuario().trim().toLowerCase()
                : null;

        return new RegisterRequestDto(
                nomeNormalizado,
                nicknameNormalizado,
                emailNormalizado,
                request.senhaDoUsuario() // Senha não normaliza
        );
    }

    public LoginRequestDto validarEPrepararLogin(LoginRequestDto request) {
        String emailNormalizado = request.emailDoUsuario() != null
                ? request.emailDoUsuario().trim().toLowerCase()
                : null;

        return new LoginRequestDto(
                emailNormalizado,
                request.senhaDoUsuario()
        );
    }
}
