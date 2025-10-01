package com.alanhss.ClashZone.core.usecases;

import com.alanhss.ClashZone.core.entities.TorneioDomain;
import com.alanhss.ClashZone.core.gateway.TorneioGateway;

import java.util.List;

public class ListarTorneiosUsecaseImpl implements ListarTorneiosUsecase {

    private final TorneioGateway torneioGateway;

    public ListarTorneiosUsecaseImpl(TorneioGateway torneioGateway) {
        this.torneioGateway = torneioGateway;
    }

    @Override
    public List<TorneioDomain> execute() {
        return torneioGateway.listarTorneios();
    }
}
