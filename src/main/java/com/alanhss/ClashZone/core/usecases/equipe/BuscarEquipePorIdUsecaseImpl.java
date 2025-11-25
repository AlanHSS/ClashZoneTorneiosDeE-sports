package com.alanhss.ClashZone.core.usecases.equipe;

import com.alanhss.ClashZone.core.domain.EquipeDomain;
import com.alanhss.ClashZone.core.exceptions.NaoEncontradoPorIdException;
import com.alanhss.ClashZone.core.gateway.EquipeGateway;

public class BuscarEquipePorIdUsecaseImpl implements BuscarEquipePorIdUsecase{
    
    private final EquipeGateway equipeGateway;

    public BuscarEquipePorIdUsecaseImpl(EquipeGateway equipeGateway) {
        this.equipeGateway = equipeGateway;
    }

    @Override
    public EquipeDomain execute(Long id) {
        return equipeGateway.buscarPorId(id)
                .orElseThrow(() -> new NaoEncontradoPorIdException(id, "equipe"));
    }
}
