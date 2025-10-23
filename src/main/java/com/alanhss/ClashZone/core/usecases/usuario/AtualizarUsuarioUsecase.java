package com.alanhss.ClashZone.core.usecases.usuario;

import com.alanhss.ClashZone.core.entities.UsuariosDomain;

public interface AtualizarUsuarioUsecase {

    public UsuariosDomain execute(Long id, UsuariosDomain usuariosDomain);

}
