package com.alanhss.ClashZone.core.usecases.usuario;

import com.alanhss.ClashZone.core.enums.Role;

public interface DeletarUsuarioPorIdUsecase {

    public void execute(Long id, Long usuarioAutenticadoId, Role roleUsuario);
}
