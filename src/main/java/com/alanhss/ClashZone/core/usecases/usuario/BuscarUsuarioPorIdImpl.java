package com.alanhss.ClashZone.core.usecases.usuario;

import com.alanhss.ClashZone.core.domain.UsuariosDomain;
import com.alanhss.ClashZone.core.exceptions.NaoEncontradoPorIdException;
import com.alanhss.ClashZone.core.gateway.UsuariosGateway;

public class BuscarUsuarioPorIdImpl implements BuscarUsuarioPorId{

    private final UsuariosGateway usuariosGateway;

    public BuscarUsuarioPorIdImpl(UsuariosGateway usuariosGateway) {
        this.usuariosGateway = usuariosGateway;
    }

    @Override
    public UsuariosDomain execute(Long id) {
        return usuariosGateway.buscarPorId(id)
                .orElseThrow(() -> new NaoEncontradoPorIdException(id, "usuário"));
    }
}
