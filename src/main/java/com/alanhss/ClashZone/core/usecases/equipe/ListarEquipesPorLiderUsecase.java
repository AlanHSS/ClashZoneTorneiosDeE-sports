package com.alanhss.ClashZone.core.usecases.equipe;

import com.alanhss.ClashZone.core.domain.EquipeDomain;

import java.util.List;

public interface ListarEquipesPorLiderUsecase {

    public List<EquipeDomain> execute(Long id);
}
