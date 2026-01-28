package com.alanhss.ClashZone.core.gateway;

import com.alanhss.ClashZone.core.domain.UsuariosDomain;

import java.util.List;
import java.util.Optional;

public interface UsuariosGateway {

    List<UsuariosDomain> listarUsuarios();

    UsuariosDomain atualizarUsuario(Long id, UsuariosDomain usuariosDomain);

    boolean existeNickname(String nickname);

    Optional<UsuariosDomain> buscarPorId(Long id);

    boolean existeEmail(String email);

    Optional<UsuariosDomain> buscarPorEmail(String email);

    void deletarUsuario(Long id);

}
