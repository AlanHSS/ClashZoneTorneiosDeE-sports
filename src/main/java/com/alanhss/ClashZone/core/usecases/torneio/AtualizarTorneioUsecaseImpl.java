package com.alanhss.ClashZone.core.usecases.torneio;

import com.alanhss.ClashZone.core.entities.TorneioDomain;
import com.alanhss.ClashZone.core.gateway.TorneioGateway;

public class AtualizarTorneioUsecaseImpl implements AtualizarTorneioUsecase{

    private final TorneioGateway torneioGateway;

    public AtualizarTorneioUsecaseImpl(TorneioGateway torneioGateway) {
        this.torneioGateway = torneioGateway;
    }

    @Override
    public TorneioDomain execute(Long id, TorneioDomain torneioDomain) {
        return torneioGateway.atualizarTorneio(id, torneioDomain);
    }

}
