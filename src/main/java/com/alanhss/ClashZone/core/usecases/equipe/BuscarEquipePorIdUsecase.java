package com.alanhss.ClashZone.core.usecases.equipe;

import com.alanhss.ClashZone.core.domain.EquipeDomain;
import com.alanhss.ClashZone.core.enums.Role;

public interface BuscarEquipePorIdUsecase {

    public EquipeDomain execute(Long id, Long usuarioAutenticadoId, Role roleUsuario);
}
