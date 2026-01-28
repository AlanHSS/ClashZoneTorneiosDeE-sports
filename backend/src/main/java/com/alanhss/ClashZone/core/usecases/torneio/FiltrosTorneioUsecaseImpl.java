package com.alanhss.ClashZone.core.usecases.torneio;

import com.alanhss.ClashZone.core.domain.TorneioDomain;
import com.alanhss.ClashZone.core.gateway.TorneioGateway;

import java.util.List;

public class FiltrosTorneioUsecaseImpl implements FiltrosTorneioUsecase{

    private final TorneioGateway torneioGateway;

    public FiltrosTorneioUsecaseImpl(TorneioGateway torneioGateway) {
        this.torneioGateway = torneioGateway;
    }

    @Override
    public List<TorneioDomain> execute(TorneioDomain torneioDomain) {
        return torneioGateway.filtrarTorneios(torneioDomain);
    }
}
