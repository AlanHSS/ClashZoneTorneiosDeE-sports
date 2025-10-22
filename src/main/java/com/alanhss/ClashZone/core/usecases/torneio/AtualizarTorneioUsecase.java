package com.alanhss.ClashZone.core.usecases.torneio;

import com.alanhss.ClashZone.core.entities.TorneioDomain;

public interface AtualizarTorneioUsecase {

    public TorneioDomain execute(Long id, TorneioDomain torneioDomain);

}
