package com.alanhss.ClashZone.core.usecases.equipe;

import com.alanhss.ClashZone.core.domain.EquipeDomain;
import com.alanhss.ClashZone.core.enums.Role;

public interface AtualizarEquipeUsecase {

    public EquipeDomain execute(Long id, EquipeDomain equipeDomain, Long usuarioAutenticadoId, Role roleUsuario);
}
