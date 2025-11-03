package com.alanhss.ClashZone.core.usecases.equipe;

import com.alanhss.ClashZone.core.domain.EquipeDomain;
import com.alanhss.ClashZone.core.gateway.EquipeGateway;

import java.util.List;

public class ListarEquipesUsecaseImpl implements ListarEquipesUsecase{

    private final EquipeGateway equipeGateway;

    public ListarEquipesUsecaseImpl(EquipeGateway equipeGateway) {
        this.equipeGateway = equipeGateway;
    }

    @Override
    public List<EquipeDomain> execute() {
        return equipeGateway.listarEquipes();
    }
}
