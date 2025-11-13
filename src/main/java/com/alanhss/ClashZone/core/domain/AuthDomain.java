package com.alanhss.ClashZone.core.domain;

public record AuthDomain(Long usuarioId,
                         String nomeDoUsuario,
                         String nickname,
                         String emailDoUsuario,
                         String role,
                         String token) {
    public record RegisterData(
            String nomeDoUsuario,
            String nickname,
            String emailDoUsuario,
            String senhaDoUsuario
    ) {}

    public record LoginData(
            String emailDoUsuario,
            String senhaDoUsuario
    ) {}
}
