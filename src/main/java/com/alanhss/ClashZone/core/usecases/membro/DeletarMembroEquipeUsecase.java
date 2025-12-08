package com.alanhss.ClashZone.core.usecases.membro;

import com.alanhss.ClashZone.core.enums.Role;

public interface DeletarMembroEquipeUsecase {

    void execute(Long equipeId, Long membroId, Long usuarioAutenticadoId, Role roleUsuario);
}
