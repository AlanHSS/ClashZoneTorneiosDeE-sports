package com.alanhss.ClashZone.core.entities;

public record UsuariosDomain(Long id,
                             String nomeDoUsuario,
                             String nickname,
                             String emailDoUsuario,
                             String senhaDoUsuario) {
}
