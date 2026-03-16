package com.alanhss.ClashZone.infra.dtos.UsuariosDtos;

// DTO publico: retorna apenas dados nao-sensiveis para exibicao (ex: organizador de torneio).
public record PublicUsuarioDto(
        Long id,
        String nomeDoUsuario,
        String nickname
) {
}

