package com.alanhss.ClashZone.core.domain;

import java.time.LocalDateTime;

public record UsuariosDomain(Long id,
                             String nomeDoUsuario,
                             String nickname,
                             String emailDoUsuario,
                             String senhaDoUsuario,
                             LocalDateTime dataCriacao) {
}
