package com.alanhss.ClashZone.core.usecases.membro;

import com.alanhss.ClashZone.core.domain.MembroEquipeDomain;
import com.alanhss.ClashZone.core.gateway.EquipeGateway;
import com.alanhss.ClashZone.core.gateway.MembroEquipeGateway;
import com.alanhss.ClashZone.core.usecases.equipe.ListarEquipesPorLiderUsecase;

import java.util.List;

public class ListarMembrosPorEquipeUsecaseImpl implements ListarMembrosPorEquipeUsecase {

    private final MembroEquipeGateway membroEquipeGateway;

    public ListarMembrosPorEquipeUsecaseImpl(MembroEquipeGateway membroEquipeGateway) {
        this.membroEquipeGateway = membroEquipeGateway;
    }

    @Override
    public List<MembroEquipeDomain> execute(Long equipeId) {
        return membroEquipeGateway.buscarMembrosPorEquipe(equipeId);
    }
}
