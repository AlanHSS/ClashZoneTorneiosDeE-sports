package com.alanhss.ClashZone.core.usecases.membro;

import com.alanhss.ClashZone.core.domain.EquipeDomain;
import com.alanhss.ClashZone.core.domain.MembroEquipeDomain;
import com.alanhss.ClashZone.core.exceptions.NaoEncontradoPorIdException;
import com.alanhss.ClashZone.core.gateway.EquipeGateway;
import com.alanhss.ClashZone.core.gateway.MembroEquipeGateway;
import com.alanhss.ClashZone.core.usecases.equipe.ListarEquipesPorLiderUsecase;

import java.util.List;

public class ListarMembrosPorEquipeUsecaseImpl implements ListarMembrosPorEquipeUsecase {

    private final MembroEquipeGateway membroEquipeGateway;
    private final EquipeGateway equipeGateway;

    public ListarMembrosPorEquipeUsecaseImpl(MembroEquipeGateway membroEquipeGateway, EquipeGateway equipeGateway) {
        this.membroEquipeGateway = membroEquipeGateway;
        this.equipeGateway = equipeGateway;
    }

    @Override
    public List<MembroEquipeDomain> execute(Long equipeId) {
        EquipeDomain equipe = equipeGateway.buscarPorId(equipeId)
                .orElseThrow(() -> new NaoEncontradoPorIdException(equipeId, "equipe"));

        return membroEquipeGateway.buscarMembrosPorEquipe(equipeId);
    }
}
