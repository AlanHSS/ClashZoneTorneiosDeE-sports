package com.alanhss.ClashZone.core.domain;

public record UsuariosDomain(Long id,
                             String nomeDoUsuario,
                             String nickname,
                             String emailDoUsuario,
                             String senhaDoUsuario) {
}
