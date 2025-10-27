package com.alanhss.ClashZone.core.gateway;

import com.alanhss.ClashZone.core.entities.UsuariosDomain;

import java.util.List;

public interface UsuariosGateway {

    UsuariosDomain criarUsuario(UsuariosDomain usuariosDomain);

    List<UsuariosDomain> listarUsuarios();

    UsuariosDomain atualizarUsuario(Long id, UsuariosDomain usuariosDomain);

    boolean existeNickname(String nickname);

}
