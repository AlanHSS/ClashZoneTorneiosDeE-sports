package com.alanhss.ClashZone.core.usecases.usuario;

import com.alanhss.ClashZone.core.entities.UsuariosDomain;
import com.alanhss.ClashZone.core.gateway.UsuariosGateway;

public class AtualizarUsuarioUsecaseImpl implements AtualizarUsuarioUsecase{

    private final UsuariosGateway usuariosGateway;

    public AtualizarUsuarioUsecaseImpl(UsuariosGateway usuariosGateway) {
        this.usuariosGateway = usuariosGateway;
    }

    @Override
    public UsuariosDomain execute(Long id, UsuariosDomain usuariosDomain) {
        return usuariosGateway.atualizarUsuario(id, usuariosDomain);
    }
}
