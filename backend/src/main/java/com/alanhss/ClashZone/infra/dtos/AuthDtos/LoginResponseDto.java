package com.alanhss.ClashZone.infra.dtos.AuthDtos;

import com.alanhss.ClashZone.core.enums.Role;

public record LoginResponseDto(String token,
                               String tipo,
                               UsuarioInfoDto usuario)
{

    public LoginResponseDto(String token, UsuarioInfoDto usuario) {
        this(token, "Bearer", usuario);
    }

    public record UsuarioInfoDto(
            Long id,
            String nome,
            String nickname,
            String email,
            Role role
    ) {}
}
