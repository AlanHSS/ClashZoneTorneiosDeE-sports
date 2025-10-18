package com.alanhss.ClashZone.core.entities;

public record UsuariosDomain(Long id,
                             String nomeDoUsuario,
                             String emailDoUsuario,
                             String senhaDoUsuario) {
}
