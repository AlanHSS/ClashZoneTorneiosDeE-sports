package com.alanhss.ClashZone.core.usecases.torneio;

import com.alanhss.ClashZone.core.domain.TorneioDomain;
import com.alanhss.ClashZone.core.exceptions.NaoEncontradoPorIdException;
import com.alanhss.ClashZone.core.gateway.TorneioGateway;

public class BuscarTorneioPorIdUsecaseImpl implements BuscarTorneioPorIdUsecase {

    private final TorneioGateway torneioGateway;

    public BuscarTorneioPorIdUsecaseImpl(TorneioGateway torneioGateway) {
        this.torneioGateway = torneioGateway;
    }

    @Override
    public TorneioDomain execute(Long id) {
        return torneioGateway.buscarPorId(id)
                .orElseThrow(() -> new NaoEncontradoPorIdException(id, "torneio"));
    }
}
