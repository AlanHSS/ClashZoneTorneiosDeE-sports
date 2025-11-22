package com.alanhss.ClashZone.core.gateway;

import com.alanhss.ClashZone.core.domain.TorneioDomain;

import java.util.List;
import java.util.Optional;

public interface TorneioGateway {
    TorneioDomain criarTorneio(TorneioDomain torneioDomain);

    List<TorneioDomain> listarTorneios();

    List<TorneioDomain> filtrarTorneios(TorneioDomain torneioDomain);

    TorneioDomain atualizarTorneio(Long id, TorneioDomain torneioDomain);

    Optional<TorneioDomain> buscarPorId(Long id);

    List<TorneioDomain> listarTorneiosPorCriador(Long id);
}
