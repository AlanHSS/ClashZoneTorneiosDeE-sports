package com.alanhss.ClashZone.core.usecases.torneio;

import com.alanhss.ClashZone.core.entities.TorneioDomain;

import java.util.List;

public interface FiltrosTorneioUsecase {

    public List<TorneioDomain> execute(TorneioDomain torneioDomain);
}
