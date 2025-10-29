package com.alanhss.ClashZone.core.usecases.usuario;

import com.alanhss.ClashZone.core.domain.UsuariosDomain;
import com.alanhss.ClashZone.core.gateway.UsuariosGateway;
import com.alanhss.ClashZone.core.exceptions.NaoEncontradoPorIdException;

public class AtualizarUsuarioUsecaseImpl implements AtualizarUsuarioUsecase{

    private final UsuariosGateway usuariosGateway;

    public AtualizarUsuarioUsecaseImpl(UsuariosGateway usuariosGateway) {
        this.usuariosGateway = usuariosGateway;
    }

    @Override
    public UsuariosDomain execute(Long id, UsuariosDomain usuariosDomain) {
        usuariosGateway.buscarPorId(id)
                .orElseThrow(() -> new NaoEncontradoPorIdException(id));
        return usuariosGateway.atualizarUsuario(id, usuariosDomain);
    }
}
