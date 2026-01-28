package com.alanhss.ClashZone.infra.mappers.AuthMappers;

import com.alanhss.ClashZone.core.domain.AuthDomain;
import com.alanhss.ClashZone.core.enums.Role;
import com.alanhss.ClashZone.infra.dtos.AuthDtos.LoginResponseDto;
import com.alanhss.ClashZone.infra.dtos.AuthDtos.RegisterResponseDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AuthResponseMapper {

    public RegisterResponseDto toRegisterResponse(AuthDomain authDomain) {
        RegisterResponseDto.UsuarioInfoDto usuarioInfo = new RegisterResponseDto.UsuarioInfoDto(
                authDomain.usuarioId(),
                authDomain.nomeDoUsuario(),
                authDomain.nickname(),
                authDomain.emailDoUsuario(),
                LocalDateTime.now(),
                Role.valueOf(authDomain.role())
        );

        return new RegisterResponseDto(authDomain.token(), usuarioInfo);
    }

    public LoginResponseDto toLoginResponse(AuthDomain authDomain) {
        LoginResponseDto.UsuarioInfoDto usuarioInfo = new LoginResponseDto.UsuarioInfoDto(
                authDomain.usuarioId(),
                authDomain.nomeDoUsuario(),
                authDomain.nickname(),
                authDomain.emailDoUsuario(),
                Role.valueOf(authDomain.role())
        );

        return new LoginResponseDto(authDomain.token(), usuarioInfo);
    }
}
