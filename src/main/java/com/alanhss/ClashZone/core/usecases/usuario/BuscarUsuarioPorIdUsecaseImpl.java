package com.alanhss.ClashZone.core.usecases.usuario;

import com.alanhss.ClashZone.core.domain.UsuariosDomain;
import com.alanhss.ClashZone.core.exceptions.NaoEncontradoPorIdException;
import com.alanhss.ClashZone.core.gateway.UsuariosGateway;

public class BuscarUsuarioPorIdUsecaseImpl implements BuscarUsuarioPorIdUsecase {

    private final UsuariosGateway usuariosGateway;

    public BuscarUsuarioPorIdUsecaseImpl(UsuariosGateway usuariosGateway) {
        this.usuariosGateway = usuariosGateway;
    }

    @Override
    public UsuariosDomain execute(Long id) {
        return usuariosGateway.buscarPorId(id)
                .orElseThrow(() -> new NaoEncontradoPorIdException(id, "usuário"));
    }
}
