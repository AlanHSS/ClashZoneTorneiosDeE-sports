package com.alanhss.ClashZone.core.usecases.usuario;

import com.alanhss.ClashZone.core.domain.UsuariosDomain;
import com.alanhss.ClashZone.core.enums.Role;

public interface AtualizarUsuarioUsecase {

    public UsuariosDomain execute(Long id, UsuariosDomain usuariosDomain, Long usuarioAutenticadoId, Role roleUsuario);

}
