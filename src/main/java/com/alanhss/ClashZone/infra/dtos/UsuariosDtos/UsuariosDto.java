package com.alanhss.ClashZone.infra.dtos.UsuariosDtos;

import java.time.LocalDateTime;

public record UsuariosDto(Long id,
                          String nomeDoUsuario,
                          String nickname,
                          String emailDoUsuario,
                          String senhaDoUsuario,
                          LocalDateTime dataCriacao) {
}
