package com.alanhss.ClashZone.core.usecases.usuario;

import com.alanhss.ClashZone.core.domain.UsuariosDomain;

public interface AtualizarUsuarioUsecase {

    public UsuariosDomain execute(Long id, UsuariosDomain usuariosDomain);

}
