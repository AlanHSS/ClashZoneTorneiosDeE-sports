package com.alanhss.ClashZone.core.usecases.torneio;

import com.alanhss.ClashZone.core.domain.TorneioDomain;

import java.util.List;

public interface ListarTorneiosPorCriador {

    public List<TorneioDomain> execute(Long id);
}
