package com.alanhss.ClashZone.core.usecases;

import com.alanhss.ClashZone.core.entities.TorneioDomain;
import com.alanhss.ClashZone.core.gateway.TorneioGateway;
import com.alanhss.ClashZone.infra.dtos.FiltroTorneioDto;

import java.util.List;

public class FiltrosTorneioUsecaseImpl implements FiltrosTorneioUsecase{

    private final TorneioGateway torneioGateway;

    public FiltrosTorneioUsecaseImpl(TorneioGateway torneioGateway) {
        this.torneioGateway = torneioGateway;
    }

    @Override
    public List<TorneioDomain> execute(FiltroTorneioDto filtroTorneioDto) {
        return torneioGateway.filtrarTorneios(filtroTorneioDto);
    }
}
