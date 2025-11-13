package com.alanhss.ClashZone.infra.dtos.AuthDtos;

import com.alanhss.ClashZone.core.enums.Role;

import java.time.LocalDateTime;

public record RegisterResponseDto(String token,
                                  String tipo,  // "Bearer"
                                  UsuarioInfoDto usuario)
{
    public RegisterResponseDto(String token, UsuarioInfoDto usuario) {
        this(token, "Bearer", usuario);
    }

    public record UsuarioInfoDto(
            Long id,
            String nome,
            String nickname,
            String email,
            LocalDateTime dataCriacao,
            Role role
    ) {}
}
