package com.alanhss.ClashZone.infra.dtos;

public record UsuariosDto(Long id,
                          String nomeDoUsuario,
                          String nickname,
                          String emailDoUsuario,
                          String senhaDoUsuario) {
}
