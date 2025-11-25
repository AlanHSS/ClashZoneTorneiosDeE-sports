package com.alanhss.ClashZone.core.usecases.equipe;

import com.alanhss.ClashZone.core.domain.EquipeDomain;

public interface BuscarEquipePorIdUsecase {

    public EquipeDomain execute(Long id);
}
