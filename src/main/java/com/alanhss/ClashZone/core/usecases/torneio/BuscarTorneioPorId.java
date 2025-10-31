package com.alanhss.ClashZone.core.usecases.torneio;

import com.alanhss.ClashZone.core.domain.TorneioDomain;

public interface BuscarTorneioPorId {

    public TorneioDomain execute(Long id);
}
