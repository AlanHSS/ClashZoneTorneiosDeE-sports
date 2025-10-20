package com.alanhss.ClashZone.core.usecases.usuario;

import com.alanhss.ClashZone.core.entities.UsuariosDomain;

import java.util.List;

public interface ListarUsuariosUsecase {

    public List<UsuariosDomain> execute();
}
