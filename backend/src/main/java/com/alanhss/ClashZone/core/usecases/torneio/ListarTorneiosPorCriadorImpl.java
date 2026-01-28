package com.alanhss.ClashZone.core.usecases.torneio;

import com.alanhss.ClashZone.core.domain.TorneioDomain;
import com.alanhss.ClashZone.core.gateway.TorneioGateway;

import java.util.List;

public class ListarTorneiosPorCriadorImpl implements ListarTorneiosPorCriador {

    private final TorneioGateway torneioGateway;

    public ListarTorneiosPorCriadorImpl(TorneioGateway torneioGateway) {
        this.torneioGateway = torneioGateway;
    }

    @Override
    public List<TorneioDomain> execute(Long id) {
        return torneioGateway.listarTorneiosPorCriador(id);
    }
}
