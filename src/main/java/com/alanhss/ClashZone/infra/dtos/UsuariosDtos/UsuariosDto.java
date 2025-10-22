package com.alanhss.ClashZone.infra.dtos.UsuariosDtos;

public record UsuariosDto(Long id,
                          String nomeDoUsuario,
                          String nickname,
                          String emailDoUsuario,
                          String senhaDoUsuario) {
}
