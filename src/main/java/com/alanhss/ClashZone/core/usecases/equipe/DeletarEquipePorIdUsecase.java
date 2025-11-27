package com.alanhss.ClashZone.core.usecases.equipe;

import com.alanhss.ClashZone.core.enums.Role;

public interface DeletarEquipePorIdUsecase {

    public void execute(Long id, Long usuarioAutenticadoId, Role roleUsuario);
}
