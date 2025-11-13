package com.alanhss.ClashZone.core.gateway;

import com.alanhss.ClashZone.core.domain.AuthDomain;
import com.alanhss.ClashZone.core.domain.UsuariosDomain;

import java.util.Optional;

public interface AuthGateway {

    boolean existeNickname(String nickname);

    boolean existeEmail(String email);

    Optional<UsuariosDomain> buscarPorEmail(String email);

    AuthDomain registerUser(UsuariosDomain usuariosDomain);

    AuthDomain loginUser(String email, String senha);
}
