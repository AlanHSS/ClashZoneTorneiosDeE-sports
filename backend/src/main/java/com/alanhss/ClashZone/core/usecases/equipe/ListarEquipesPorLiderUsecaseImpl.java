package com.alanhss.ClashZone.core.usecases.equipe;

import com.alanhss.ClashZone.core.domain.EquipeDomain;
import com.alanhss.ClashZone.core.gateway.EquipeGateway;

import java.util.List;

public class ListarEquipesPorLiderUsecaseImpl implements ListarEquipesPorLiderUsecase{

    private final EquipeGateway equipeGateway;

    public ListarEquipesPorLiderUsecaseImpl(EquipeGateway equipeGateway) {
        this.equipeGateway = equipeGateway;
    }

    @Override
    public List<EquipeDomain> execute(Long id) {
        return equipeGateway.listarEquipesPorLider(id);
    }
}
