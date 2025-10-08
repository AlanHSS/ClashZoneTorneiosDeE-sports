package com.alanhss.ClashZone.core.gateway;

import com.alanhss.ClashZone.core.entities.TorneioDomain;
import com.alanhss.ClashZone.infra.dtos.FiltroTorneioDto;

import java.util.List;

public interface TorneioGateway {
    TorneioDomain criarTorneio(TorneioDomain torneioDomain);

    List<TorneioDomain> listarTorneios();

    List<TorneioDomain> filtrarTorneios(FiltroTorneioDto filtroTorneioDto);
}
