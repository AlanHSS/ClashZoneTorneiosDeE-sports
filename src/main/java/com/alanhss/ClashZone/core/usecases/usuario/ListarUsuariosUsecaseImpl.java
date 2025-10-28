package com.alanhss.ClashZone.core.usecases.usuario;

import com.alanhss.ClashZone.core.entities.UsuariosDomain;
import com.alanhss.ClashZone.core.gateway.UsuariosGateway;

import java.util.List;

public class ListarUsuariosUsecaseImpl implements ListarUsuariosUsecase{

    private final UsuariosGateway usuariosGateway;

    public ListarUsuariosUsecaseImpl(UsuariosGateway usuariosGateway) {
        this.usuariosGateway = usuariosGateway;
    }

    @Override
    public List<UsuariosDomain> execute() {
        return usuariosGateway.listarUsuarios();
    }
}
